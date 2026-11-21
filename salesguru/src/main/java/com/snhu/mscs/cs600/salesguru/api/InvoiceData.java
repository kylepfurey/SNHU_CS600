// .java
// Invoice Data Records
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Data for an Invoice
 */
public record InvoiceData(
        Integer invoiceNumber,
        Integer customerNumber,
        Integer representativeId,
        LocalDate dateOfSale,
        List<Line> lines,
        BigDecimal total
) {
    /**
     * A Line in an Invoice
     */
    public record Line(
            Integer itemNumber,
            String description,
            Integer qty,
            BigDecimal unitPrice,
            BigDecimal discount,
            BigDecimal lineTotal
    ) { }

    /**
     * Request for an Invoice
     */
    public record Request(
            Integer itemNumber,
            Integer qty,
            BigDecimal discount
    ) { }
}
