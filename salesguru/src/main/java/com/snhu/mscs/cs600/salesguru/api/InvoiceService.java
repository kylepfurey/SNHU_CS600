// .java
// Invoice API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.stereotype.Service;
import com.snhu.mscs.cs600.salesguru.repositories.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Invoice;
import com.snhu.mscs.cs600.salesguru.entities.LineItem;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import java.util.Objects;
import java.util.HashMap;

/**
 * API Service for Invoices
 */
@Service
public class InvoiceService {
    /**
     * Constants
     */
    static final Integer MAX_LINES = 500;
    static final Integer MAX_PAGE_SIZE = 200;

    /**
     * Repositories
     */
    final CompanyRepository companyRepository;
    final RepresentativeRepository representativeRepository;
    final ItemRepository itemRepository;
    final InvoiceRepository invoiceRepository;
    final LineItemRepository lineItemRepository;

    /**
     * Constructor
     */
    public InvoiceService(
            CompanyRepository companyRepository,
            RepresentativeRepository representativeRepository,
            ItemRepository itemRepository,
            InvoiceRepository invoiceRepository,
            LineItemRepository lineItemRepository
    ) {
        this.companyRepository = companyRepository;
        this.representativeRepository = representativeRepository;
        this.itemRepository = itemRepository;
        this.invoiceRepository = invoiceRepository;
        this.lineItemRepository = lineItemRepository;
    }

    /**
     * API
     */
    @Transactional
    public InvoiceData createInvoice(
            Integer customerNumber,
            Integer representativeId,
            LocalDate dateOfSale,
            List<InvoiceData.Request> requests
    ) {
        validate(customerNumber, representativeId, dateOfSale, requests);
        Invoice invoice = invoiceRepository.save(
                new Invoice(
                        customerNumber,
                        representativeId,
                        dateOfSale,
                        new Integer[0]
                )
        );
        for (InvoiceData.Request request : requests) {
            LineItem lineItem = lineItemRepository.save(
                    new LineItem(
                            invoice.getInvoiceNumber(),
                            request.itemNumber(),
                            request.qty(),
                            request.discount()
                    )
            );
            invoice.addLineItem(lineItem.getLineItemId());
        }
        invoiceRepository.save(invoice);
        return getInvoice(invoice.getInvoiceNumber());
    }
    @Transactional(readOnly = true)
    public List<Invoice> listInvoices() {
        return invoiceRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Invoice> listInvoices(Integer page, Integer size) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0: " + page);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("size must be between 1 and " + MAX_PAGE_SIZE + ": " + size);
        }
        return invoiceRepository.findAll(PageRequest.of(page, size, Sort.by("invoiceNumber"))).getContent();
    }
    @Transactional(readOnly = true)
    public InvoiceData getInvoice(Integer invoiceNumber) {
        Invoice invoice = invoiceRepository.findById(invoiceNumber).orElseThrow(() ->
                new NoSuchElementException("invoiceNumber not found: " + invoiceNumber));
        List<LineItem> lineItems = lineItemRepository.findByInvoiceNumber(invoiceNumber);
        Set<Integer> itemNumbers = new LinkedHashSet<>();
        for (LineItem lineItem : lineItems) {
            if (lineItem.getQty() == null || lineItem.getItemNumber() == null) {
                throw new IllegalStateException("Null LineItem field: " + invoiceNumber);
            }
            itemNumbers.add(lineItem.getItemNumber());
        }
        Map<Integer, Item> items = loadItems(itemNumbers);
        List<InvoiceData.Line> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : lineItems) {
            Item item = items.get(lineItem.getItemNumber());
            if (item.getRetail() == null) {
                throw new IllegalStateException("Null retail");
            }
            BigDecimal price = item.getRetail().setScale(2, RoundingMode.HALF_UP);
            BigDecimal discount = lineItem.getDiscount() == null ? BigDecimal.ZERO : lineItem.getDiscount();
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(lineItem.getQty()))
                    .multiply(BigDecimal.ONE.subtract(discount))
                    .setScale(2, RoundingMode.HALF_UP);
            lines.add(
                    new InvoiceData.Line(
                            item.getItemNumber(),
                            item.getDescription(),
                            lineItem.getQty(),
                            price,
                            discount,
                            lineTotal
                    )
            );
            total = total.add(lineTotal);
        }
        return new InvoiceData(
                invoice.getInvoiceNumber(),
                invoice.getCustomerNumber(),
                invoice.getRepresentativeId(),
                invoice.getDateOfSale(),
                lines,
                total
        );
    }
    @Transactional(readOnly = true)
    public List<Invoice> listInvoicesForCompany(Integer customerNumber) {
        return invoiceRepository.findByCustomerNumber(customerNumber);
    }
    @Transactional(readOnly = true)
    public List<Invoice> listInvoicesForRepresentative(Integer representativeId) {
        return invoiceRepository.findByRepresentativeId(representativeId);
    }
    @Transactional(readOnly = true)
    public List<Invoice> listInvoicesForDate(LocalDate dateOfSale) {
        return invoiceRepository.findByDateOfSale(dateOfSale);
    }

    /**
     * Helpers
     */
    void validate(
            Integer customerNumber,
            Integer representativeId,
            LocalDate dateOfSale,
            List<InvoiceData.Request> requests
    ) {
        if (customerNumber == null) {
            throw new IllegalArgumentException("Null customerNumber");
        }
        if (representativeId == null) {
            throw new IllegalArgumentException("Null representativeId");
        }
        if (dateOfSale == null) {
            throw new IllegalArgumentException("Null dateOfSale");
        }
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Null requests array");
        }
        if (requests.size() > MAX_LINES) {
            throw new IllegalArgumentException("Too many lines (max " + MAX_LINES + "): " + requests.size());
        }
        if (!companyRepository.existsById(customerNumber)) {
            throw new NoSuchElementException("customerNumber not found: " + customerNumber);
        }
        Representative representative = representativeRepository.findById(representativeId)
                .orElseThrow(() ->
                        new NoSuchElementException("representativeId not found: " + representativeId));
        if (!Objects.equals(representative.getCustomerNumber(), customerNumber)) {
            throw new IllegalArgumentException("representative.customerNumber does not match customerNumber: " +
                    representative.getCustomerNumber() + " != " + customerNumber);
        }
        Set<Integer> itemNumbers = new LinkedHashSet<>();
        for (InvoiceData.Request request : requests) {
            if (request == null || request.itemNumber() == null || request.qty() == null || request.discount() == null) {
                throw new IllegalArgumentException("Null InvoiceData.Line or field");
            }
            if (request.qty() <= 0) {
                throw new IllegalArgumentException("Non-positive InvoiceData.Line qty: " + request.qty());
            }
            BigDecimal discount = request.discount();
            if (discount.signum() < 0 || discount.compareTo(BigDecimal.ONE) > 0) {
                throw new IllegalArgumentException("discount out of range: " + discount);
            }
            if (discount.stripTrailingZeros().scale() > 4) {
                throw new IllegalArgumentException("discount allows at most 4 decimal places: " + discount);
            }
            itemNumbers.add(request.itemNumber());
        }
        loadItems(itemNumbers);
    }
    Map<Integer, Item> loadItems(Set<Integer> itemNumbers) {
        Map<Integer, Item> found = new HashMap<>();
        for (Item item : itemRepository.findAllById(itemNumbers)) {
            found.put(item.getItemNumber(), item);
        }
        for (Integer itemNumber : itemNumbers) {
            if (!found.containsKey(itemNumber)) {
                throw new NoSuchElementException("itemNumber not found: " + itemNumber);
            }
        }
        return found;
    }
}
