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
import com.snhu.mscs.cs600.salesguru.repository.*;
import com.snhu.mscs.cs600.salesguru.entities.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

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
        when(itemRepository.existsById(1))
                .thenReturn(true);
    }
    @Test
    void createInvoiceCreatesLineItems() {
        Invoice stored = new Invoice(1000L, 1660L, DATE, new Integer[0]);
        ReflectionTestUtils.setField(stored, "invoiceNumber", 10000);
        LineItem line = new LineItem(10000, 1, 3, 0.1f);
        ReflectionTestUtils.setField(line, "lineItemId", 1);
        Item item = new Item("Planner", 5.0f, 10.0f);
        ReflectionTestUtils.setField(item, "itemNumber", 1);
        when(invoiceRepository.save(any(Invoice.class)))
                .thenReturn(stored);
        when(invoiceRepository.findById(10000))
                .thenReturn(Optional.of(stored));
        when(lineItemRepository.save(any(LineItem.class)))
                .thenReturn(line);
        when(lineItemRepository.findByInvoiceNumber(10000))
                .thenReturn(List.of(line));
        when(itemRepository.findById(1))
                .thenReturn(Optional.of(item));
        InvoiceData data = invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(1, 3, 0.1f));
        assertEquals(10000, data.invoiceNumber());
        assertEquals(1, data.lines().size());
        assertEquals(0, new BigDecimal("27.00")
                .compareTo(data.total()));
        assertArrayEquals(new Integer[]{1}, stored.getLineItems());
    }
    @Test
    void rejectNullCustomer() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(null, 1660L, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectNullRepresentative() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, null, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectNullDate() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1660L, null, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectEmptyLines() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1660L, DATE, List.of()));
    }
    @Test
    void rejectUnknownCompany() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(9999L, 1660L, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectUnknownRepresentative() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(1000L, 5L, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectMismatchRepresentative() {
        when(companyRepository.existsById(1010))
                .thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1010L, 1660L, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectOrphanRepresentative() {
        when(representativeRepository.findById(1670))
                .thenReturn(Optional.of(makeRepresentative(1670, null)));
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1670L, DATE, makeLines(1, 1, 0.0f)));
    }
    @Test
    void rejectZeroQty() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(1, 0, 0.0f)));
    }
    @Test
    void rejectHighDiscount() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(1, 1, 1.5f)));
    }
    @Test
    void rejectInvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(1, 1, Float.NaN)));
    }
    @Test
    void rejectUnknownItem() {
        assertThrows(NoSuchElementException.class, () -> invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(77, 1, 0.0f)));
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
                invoiceService.createInvoice(1000L, 1660L, DATE, makeLines(1, -1, 0.0f)));
        verify(invoiceRepository, never())
                .save(any());
        verify(lineItemRepository, never())
                .save(any());
    }

    /**
     * Helpers
     */
    Representative makeRepresentative(Integer id, Integer customer) {
        return new Representative(id, customer, "A", "B", "C", null, "Ms.", "1", "2");
    }
    List<InvoiceData.Request> makeLines(Integer item, Integer qty, Float discount) {
        return List.of(new InvoiceData.Request(item, qty, discount));
    }
}
