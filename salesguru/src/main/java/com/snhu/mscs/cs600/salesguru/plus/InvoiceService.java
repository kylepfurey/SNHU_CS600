// .java
// Invoice API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
import com.snhu.mscs.cs600.salesguru.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * API Service for Invoices
 */
@Service
public class InvoiceService {
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
            Long customerNumber,
            Long representativeId,
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
        return findInvoice(invoice.getInvoiceNumber());
    }
    @Transactional(readOnly = true)
    public InvoiceData findInvoice(Integer invoiceNumber) {
        Invoice invoice = invoiceRepository.findById(invoiceNumber)
                .orElseThrow(() ->
                        new NoSuchElementException("invoiceNumber not found: " + invoiceNumber)
                );
        List<InvoiceData.Line> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : lineItemRepository.findByInvoiceNumber(invoiceNumber)) {
            if (lineItem.getQty() == null || lineItem.getItemNumber() == null) {
                throw new IllegalStateException("Null LineItem field: " + invoiceNumber);
            }
            Item item = itemRepository.findById(lineItem.getItemNumber())
                    .orElseThrow(() ->
                            new NoSuchElementException("itemNumber not found: " + lineItem.getItemNumber())
                    );
            if (item.getRetail() == null) {
                throw new IllegalStateException("Null retail");
            }
            BigDecimal price = floatToMoney(item.getRetail());
            BigDecimal discount = lineItem.getDiscount() == null ?
                    BigDecimal.ZERO :
                    new BigDecimal(Float.toString(lineItem.getDiscount()));
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(lineItem.getQty()))
                    .multiply(BigDecimal.ONE.subtract(discount))
                    .setScale(2, RoundingMode.HALF_UP);
            lines.add(new InvoiceData.Line(
                    item.getItemNumber(),
                    item.getDescription(),
                    lineItem.getQty(),
                    price,
                    discount,
                    lineTotal
            ));
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
    public List<Invoice> findInvoicesByCustomerNumber(Long customerNumber) {
        return invoiceRepository.findByCustomerNumber(customerNumber);
    }
    @Transactional(readOnly = true)
    public List<Invoice> findInvoicesByRepresentativeId(Long representativeId) {
        return invoiceRepository.findByRepresentativeId(representativeId);
    }
    @Transactional(readOnly = true)
    public List<Invoice> findInvoicesByDateOfSale(LocalDate dateOfSale) {
        return invoiceRepository.findByDateOfSale(dateOfSale);
    }

    /**
     * Helpers
     */
    void validate(
            Long customerNumber,
            Long representativeId,
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
        if (!companyRepository.existsById(customerNumber.intValue())) {
            throw new NoSuchElementException("customerNumber not found: " + customerNumber);
        }
        Representative representative = representativeRepository.findById(representativeId.intValue())
                .orElseThrow(() ->
                        new NoSuchElementException("representativeId not found: " + representativeId)
                );
        if (representative.getCustomer_number() != customerNumber) {
            throw new IllegalArgumentException(
                    "representative.customer_number does not match customerNumber: " +
                            representative.getCustomer_number() + " != " + customerNumber
            );
        }
        for (InvoiceData.Request request : requests) {
            if (request == null ||
                    request.itemNumber() == null ||
                    request.qty() == null ||
                    request.discount() == null) {
                throw new IllegalArgumentException("Null InvoiceData.Line or field");
            }
            if (request.qty() <= 0) {
                throw new IllegalArgumentException("Negative InvoiceData.Line qty: " + request.qty());
            }
            float discount = request.discount();
            if (!(discount >= 0.0f && discount <= 1.0f)) {
                throw new IllegalArgumentException("discount out of range: " + discount);
            }
            if (!itemRepository.existsById(request.itemNumber())) {
                throw new NoSuchElementException("InvoiceData.Line.itemNumber not found: " + request.itemNumber());
            }
        }
    }
    BigDecimal floatToMoney(Float value) {
        return new BigDecimal(Float.toString(value)).setScale(2, RoundingMode.HALF_UP);
    }
}
