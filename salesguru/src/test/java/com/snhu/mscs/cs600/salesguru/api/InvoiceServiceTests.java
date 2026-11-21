// .java
// Invoice Service Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.LocalDate;
import org.mockito.Mock;
import com.snhu.mscs.cs600.salesguru.repositories.*;
import com.snhu.mscs.cs600.salesguru.entities.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;
import java.util.Collections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

/**
 * Invoice Service Testing
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InvoiceServiceTests {
    /**
     * Constants
     */
    final LocalDate DATE = LocalDate.of(2004, 6, 30);

    /**
     * Services
     */
    @Mock
    CompanyRepository companyRepository;
    @Mock
    RepresentativeRepository representativeRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    LineItemRepository lineItemRepository;
    InvoiceService invoiceService;

    /**
     * Invoice Service Tests
     */
    @BeforeEach
    void reset() {
        invoiceService = new InvoiceService(companyRepository, representativeRepository, itemRepository, invoiceRepository, lineItemRepository);
        when(companyRepository.existsById(1000))
                .thenReturn(true);
        when(representativeRepository.findById(1660))
                .thenReturn(Optional.of(makeRepresentative(1660, 1000)));
    }
    @Test
    void createInvoiceCreatesLineItems() {
        Invoice stored = new Invoice(1000, 1660, DATE, new Integer[0]);
        ReflectionTestUtils.setField(stored, "invoiceNumber", 10000);
        LineItem line = new LineItem(10000, 1, 3, new BigDecimal("0.10"));
        ReflectionTestUtils.setField(line, "lineItemId", 1);
        Item item = new Item("Planner", new BigDecimal("5.00"), new BigDecimal("10.00"));
        ReflectionTestUtils.setField(item, "itemNumber", 1);
        when(invoiceRepository.save(any(Invoice.class)))
                .thenReturn(stored);
        when(invoiceRepository.findById(10000))
                .thenReturn(Optional.of(stored));
        when(lineItemRepository.save(any(LineItem.class)))
                .thenReturn(line);
        when(lineItemRepository.findByInvoiceNumber(10000))
                .thenReturn(List.of(line));
        when(itemRepository.findAllById(any()))
                .thenReturn(List.of(item));
        InvoiceData data = invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, 3, new BigDecimal("0.10")));
        assertEquals(10000, data.invoiceNumber());
        assertEquals(1, data.lines().size());
        assertEquals(0, new BigDecimal("27.00")
                .compareTo(data.total()));
        assertArrayEquals(new Integer[]{1}, stored.getLineItems());
    }
    @Test
    void rejectNullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(null, 1660, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectNullRepresentative() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, null, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectNullDate() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, 1660, null, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectEmptyLines() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, 1660, DATE, List.of()));
    }
    @Test
    void rejectUnknownCompany() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(9999, 1660, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectUnknownRepresentative() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(1000, 5, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectMismatchRepresentative() {
        when(companyRepository.existsById(1010))
                .thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1010, 1660, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectOrphanRepresentative() {
        when(representativeRepository.findById(1670))
                .thenReturn(Optional.of(makeRepresentative(1670, null)));
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, 1670, DATE, makeLines(1, 1, BigDecimal.ZERO)));
    }
    @Test
    void rejectZeroQty() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, 0, BigDecimal.ZERO)));
    }
    @Test
    void rejectHighDiscount() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, 1, new BigDecimal("1.50"))));
    }
    @Test
    void rejectTooManyDiscountDecimals() {
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, 1, new BigDecimal("0.12345"))));
    }
    @Test
    void rejectUnknownItem() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(1000, 1660, DATE, makeLines(77, 1, BigDecimal.ZERO)));
    }
    @Test
    void throwOnMissingInvoice() {
        when(invoiceRepository.findById(1))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> invoiceService.getInvoice(1));
    }
    @Test
    void validationFailureAborts() {
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, -1, BigDecimal.ZERO)));
        verify(invoiceRepository, never())
                .save(any());
        verify(lineItemRepository, never())
                .save(any());
    }
    @Test
    void rejectNegativePage() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.listInvoices(-1, 10));
    }
    @Test
    void rejectZeroPageSize() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.listInvoices(0, 0));
    }
    @Test
    void rejectOversizePageSize() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.listInvoices(0, 201));
    }
    @Test
    void acceptMaxPageSize() {
        when(invoiceRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<Invoice>(List.of()));
        assertEquals(0, invoiceService.listInvoices(0, 200).size());
    }
    @Test
    void rejectNegativeDiscount() {
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.createInvoice(1000, 1660, DATE, makeLines(1, 1, new BigDecimal("-0.01"))));
    }
    @Test
    void rejectTooManyLines() {
        List<InvoiceData.Request> lines = Collections.nCopies(
                501,
                new InvoiceData.Request(1, 1, BigDecimal.ZERO)
        );
        assertThrows(IllegalArgumentException.class, () ->
                invoiceService.createInvoice(1000, 1660, DATE, lines));
    }
    @Test
    void nullRetailIsServerError() {
        makeInvoice(new BigDecimal("0.00"), null);
        assertThrows(IllegalStateException.class, () -> invoiceService.getInvoice(10000));
    }
    @Test
    void nullDiscountCountsAsZero() {
        makeInvoice(null, new BigDecimal("10.00"));
        InvoiceData data = invoiceService.getInvoice(10000);
        assertEquals(0, new BigDecimal("30.00").compareTo(data.total()));
    }

    /**
     * Helpers
     */
    Representative makeRepresentative(Integer id, Integer customer) {
        return new Representative(id, customer, "A", "B", "C", null, "Ms.", "1", "2");
    }
    List<InvoiceData.Request> makeLines(Integer item, Integer qty, BigDecimal discount) {
        return List.of(new InvoiceData.Request(item, qty, discount));
    }
    void makeInvoice(BigDecimal discount, BigDecimal retail) {
        Invoice stored = new Invoice(1000, 1660, DATE, new Integer[0]);
        ReflectionTestUtils.setField(stored, "invoiceNumber", 10000);
        LineItem line = new LineItem(10000, 1, 3, discount);
        Item item = new Item("Planner", new BigDecimal("5.00"), retail);
        ReflectionTestUtils.setField(item, "itemNumber", 1);
        when(invoiceRepository.findById(10000)).thenReturn(Optional.of(stored));
        when(lineItemRepository.findByInvoiceNumber(10000)).thenReturn(List.of(line));
        when(itemRepository.findAllById(any())).thenReturn(List.of(item));
    }
}
