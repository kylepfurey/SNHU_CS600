// .java
// Spring JPA Company Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Company Repository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{
    /**
     * Queries
     */
    @Query(nativeQuery = true, value = "SELECT customer_number, name, addr1, addr2, city, state, zip FROM company")
    List<Company> listCompanies();
}
