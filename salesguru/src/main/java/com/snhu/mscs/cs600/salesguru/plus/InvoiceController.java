// .java
// Invoice REST Controller
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
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
     * Invoice Request Record
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
    public InvoiceData findInvoice(
            @PathVariable
            Integer invoiceNumber
    ) {
        return invoiceService.findInvoice(invoiceNumber);
    }
    @GetMapping("/invoices/forCustomer/{customerNumber}")
    public List<Invoice> findByCustomerNumber(
            @PathVariable
            Long customerNumber
    ) {
        return invoiceService.findInvoicesByCustomerNumber(customerNumber);
    }
    @GetMapping("/invoices/forRepresentative/{representativeId}")
    public List<Invoice> findByRepresentativeId(
            @PathVariable
            Long representativeId
    ) {
        return invoiceService.findInvoicesByRepresentativeId(representativeId);
    }
    @GetMapping("/invoices/forDate/{dateOfSale}")
    public List<Invoice> findByDateOfSale(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfSale
    ) {
        return invoiceService.findInvoicesByDateOfSale(dateOfSale);
    }
}
