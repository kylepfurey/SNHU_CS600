// .java
// Database Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

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

    /**
     * Helpers
     */
    int querySQL(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
