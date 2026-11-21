// .java
// Database Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.sql.DataSource;
import com.snhu.mscs.cs600.salesguru.api.InvoiceService;
import com.snhu.mscs.cs600.salesguru.api.ItemService;
import org.springframework.jdbc.core.JdbcTemplate;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import org.springframework.transaction.annotation.Transactional;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import com.snhu.mscs.cs600.salesguru.api.InvoiceData;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

/**
 * Database Testing
 */
@SpringBootTest
class DatabaseTests {
    /**
     * Services
     */
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SalesGuruService salesGuruService;
    @Autowired
    @Qualifier("authDataSource")
    DataSource authDataSource;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    ItemService itemService;

    /**
     * Database Tests
     */
    @Test
    void noDuplicateCustomerNumbers() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM (SELECT customer_number FROM company GROUP BY 1 HAVING COUNT(*) > 1) d")
        );
    }
    @Test
    void noDuplicateRepresentativeIds() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM (SELECT representative_id FROM representative GROUP BY 1 HAVING COUNT(*) > 1) d")
        );
    }
    @Test
    void everyRepresentativeHasCompany() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM representative r WHERE r.customer_number IS NULL " +
                        "OR NOT EXISTS (SELECT 1 FROM company c WHERE c.customer_number = r.customer_number)")
        );
    }
    @Test
    void listCompaniesIsFast() {
        assertTimeout(
                Duration.ofSeconds(2),
                () -> salesGuruService.listCompanies()
        );
    }
    @Test
    void invoiceHasValidCompanyAndRepresentative() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM invoice i WHERE NOT EXISTS " +
                        "(SELECT 1 FROM company c WHERE c.customer_number = i.customer_number) " +
                        "OR NOT EXISTS (SELECT 1 FROM representative r WHERE r.representative_id = i.representative_id " +
                        "AND r.customer_number = i.customer_number)")
        );
    }
    @Test
    void lineItemsHaveInvoice() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM line_items l WHERE NOT EXISTS " +
                        "(SELECT 1 FROM invoice i WHERE i.invoice_number = l.invoice_number)")
        );
    }
    @Test
    void invoiceLineItemsAllExist() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM invoice i, unnest(i.line_items) AS li(id) WHERE NOT EXISTS " +
                        "(SELECT 1 FROM line_items l WHERE l.line_item_id = li.id AND l.invoice_number = i.invoice_number)")
        );
    }
    @Test
    void listCompaniesMatches() {
        assertEquals(
                querySQL("SELECT COUNT(*) FROM company"),
                salesGuruService.listCompanies()
                        .size()
        );
    }
    @Test
    void representativesMatchCompanies() {
        List<Representative> representatives = salesGuruService.listRepresentativesForCompany(1050);
        assertEquals(
                querySQL("SELECT COUNT(*) FROM representative WHERE customer_number = 1050"),
                representatives.size()
        );
        assertTrue(representatives.stream().allMatch(r -> r.getCustomerNumber() == 1050));
    }
    @Test
    void noMoneyIsNegative() {
        assertEquals(
                0,
                querySQL("SELECT COUNT(*) FROM items WHERE cost < 0 OR retail < 0")
        );
    }
    @Test
    void authUsesSeparateDatabase() throws Exception {
        try (var connection = authDataSource.getConnection()) {
            assertTrue(connection.getMetaData().getURL().contains("salesguru_auth"));
        }
    }
    @Test
    void storedPasswordsAreHashed() {
        Integer plaintext = new JdbcTemplate(authDataSource)
                .queryForObject("SELECT COUNT(*) FROM users WHERE password NOT LIKE '$2%'", Integer.class);
        assertEquals(0, plaintext);
    }
    @Test
    void listInvoicesIsFast() {
        assertTimeout(Duration.ofSeconds(2), () -> invoiceService.listInvoices(0, 100));
    }
    @Test
    @Transactional
    void createAndFetchInvoiceEndToEnd() {
        Item item = itemService.createItem("Test item", new BigDecimal("5.00"), new BigDecimal("10.00"));
        InvoiceData invoice = invoiceService.createInvoice(1000, 1660, LocalDate.of(2026, 11, 20),
                List.of(new InvoiceData.Request(item.getItemNumber(), 3, new BigDecimal("0.10"))));
        assertTimeout(Duration.ofSeconds(2), () -> invoiceService.getInvoice(invoice.invoiceNumber()));
        assertEquals(0, new BigDecimal("27.00")
                .compareTo(invoice.total()));
        assertEquals(1, invoice.lines()
                .size());
    }
    @Test
    @Transactional
    void largeInvoiceIsAcceptable() {
        Item item = itemService.createItem("Bulk item", new BigDecimal("1.00"), new BigDecimal("2.00"));
        List<InvoiceData.Request> lines = Collections.nCopies(500, new InvoiceData.Request(item.getItemNumber(), 1, BigDecimal.ZERO));
        var created = assertTimeout(Duration.ofSeconds(5), () ->
                invoiceService.createInvoice(1000, 1660, LocalDate.of(2026, 11, 20), lines));
        assertEquals(500, created.lines().size());
        assertEquals(0, new BigDecimal("1000.00").compareTo(created.total()));
    }

    /**
     * Helpers
     */
    int querySQL(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
