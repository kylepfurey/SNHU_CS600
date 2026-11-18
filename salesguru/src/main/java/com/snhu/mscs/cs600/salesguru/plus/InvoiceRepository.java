// .java
// Spring JPA Invoice Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

/**
 * Invoice Repository
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    /**
     * Queries
     */
    List<Invoice> findByCustomerNumber(Long customerNumber);
    List<Invoice> findByRepresentativeId(Long representativeId);
    List<Invoice> findByDateOfSale(LocalDate dateOfSale);
}
