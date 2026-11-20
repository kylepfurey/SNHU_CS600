// .java
// Invoice API Controller
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import com.snhu.mscs.cs600.salesguru.entities.Invoice;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * REST API for Invoices
 */
@RestController
@RequestMapping("/api/v1")
public class InvoiceController {
    /**
     * Records
     */
    public record Request(
            Long customerNumber,
            Long representativeId,
            LocalDate dateOfSale,
            List<InvoiceData.Request> lines
    ) { }

    /**
     * Services
     */
    final InvoiceService invoiceService;

    /**
     * Constructor
     */
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * API
     */
    @GetMapping("/invoices")
    public List<Invoice> listInvoices() {
        return invoiceService.listInvoices();
    }
    @PostMapping("/invoices")
    public ResponseEntity<InvoiceData> createInvoice(
            @RequestBody
            Request request
    ) {
        InvoiceData invoiceData = invoiceService.createInvoice(
                request.customerNumber(),
                request.representativeId(),
                request.dateOfSale(),
                request.lines()
        );
        return ResponseEntity.created(URI.create("/api/v1/invoices/" + invoiceData.invoiceNumber()))
                .body(invoiceData);
    }
    @GetMapping("/invoices/{invoiceNumber}")
    public InvoiceData getInvoice(
            @PathVariable
            Integer invoiceNumber
    ) {
        return invoiceService.getInvoice(invoiceNumber);
    }
    @GetMapping("/invoices/forCompany/{customerNumber}")
    public List<Invoice> listInvoicesForCompany(
            @PathVariable
            Long customerNumber
    ) {
        return invoiceService.listInvoicesForCompany(customerNumber);
    }
    @GetMapping("/invoices/forRep/{representativeId}")
    public List<Invoice> listInvoicesForRepresentative(
            @PathVariable
            Long representativeId
    ) {
        return invoiceService.listInvoicesForRepresentative(representativeId);
    }
    @GetMapping("/invoices/forDate/{dateOfSale}")
    public List<Invoice> listInvoicesForDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfSale
    ) {
        return invoiceService.listInvoicesForDate(dateOfSale);
    }
}
