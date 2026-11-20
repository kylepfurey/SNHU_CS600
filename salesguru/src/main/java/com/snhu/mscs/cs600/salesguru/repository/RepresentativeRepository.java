// .java
// Spring JPA Representative Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Representative Repository
 */
@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Integer>{
    /**
     * Queries
     */
    @Query(nativeQuery = true, value = "SELECT customer_number, first, middle, last, suffix, salutation, direct_line, mobile, representative_id FROM representative")
    List<Representative> listRepresentatives();
    @Query(
            nativeQuery = true, value = "SELECT customer_number, first, middle, last, suffix, salutation, direct_line, mobile, representative_id FROM representative where customer_number = :search")
    List<Representative> listRepresentativesForCompany(
            @Param("search")
            Integer customerNumber
    );
}
