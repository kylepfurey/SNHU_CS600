// .java
// Invoice Controller Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.snhu.mscs.cs600.salesguru.config.ErrorHandler;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.NoSuchElementException;

/**
 * Invoice Controller Testing
 */
class InvoiceControllerTests {
    /**
     * Constants
     */
    final String json = """
            {"customerNumber":1000,"representativeId":1660,"dateOfSale":"2026-11-20",
             "lines":[{"itemNumber":1,"qty":3,"discount":0.1}]}
            """;

    /**
     * Services
     */
    InvoiceService invoiceService;
    MockMvc mockMvc;

    /**
     * Invoice Controller Tests
     */
    @BeforeEach
    void reset() {
        invoiceService = mock(InvoiceService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new InvoiceController(invoiceService))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }
    @Test
    void createReturns201WithLocation() throws Exception {
        when(invoiceService.createInvoice(any(), any(), any(), any()))
                .thenReturn(
                        new InvoiceData(
                                10000,
                                1000L,
                                1660L,
                                LocalDate.of(2026, 11, 20),
                                List.of(),
                                BigDecimal.ZERO
                        )
                );
        mockMvc.perform(post("/api/v1/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status()
                .isCreated())
                .andExpect(header()
                .string("Location", "/api/v1/invoices/10000"))
                .andExpect(jsonPath("$.invoiceNumber")
                        .value(10000));
    }
    @Test
    void validationErrorIs400() throws Exception {
        when(invoiceService.createInvoice(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("bad"));
        mockMvc.perform(post("/api/v1/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("bad"));
    }
    @Test
    void missingInvoiceIs404() throws Exception {
        when(invoiceService.getInvoice(1))
                .thenThrow(new NoSuchElementException("invoiceNumber not found: 1"));
        mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status()
                        .isNotFound());
    }
    @Test
    void malformedJsonIs400() throws Exception {
        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{nope"))
                .andExpect(status()
                        .isBadRequest());
    }
}
