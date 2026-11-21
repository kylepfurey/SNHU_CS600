// .java
// Sales Guru Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import com.snhu.mscs.cs600.salesguru.repositories.CompanyRepository;
import static org.mockito.Mockito.*;
import com.snhu.mscs.cs600.salesguru.repositories.RepresentativeRepository;
import org.junit.jupiter.api.Test;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.snhu.mscs.cs600.salesguru.entities.Company;

/**
 * Sales Guru Service Testing
 */
class SalesGuruServiceTests {
    /**
     * Services
     */
    final CompanyRepository companyRepository = mock(CompanyRepository.class);
    final RepresentativeRepository representativeRepository = mock(RepresentativeRepository.class);
    final SalesGuruService service = new SalesGuruService(companyRepository, representativeRepository);

    /**
     * Sales Guru Service Tests
     */
    @Test
    void listsRepresentativesForCompany() {
        Representative representative = new Representative(
                1660,
                1000,
                "A",
                "B",
                "C",
                null,
                "Ms.",
                "1",
                "2"
        );
        when(representativeRepository.listRepresentativesForCompany(1000))
                .thenReturn(List.of(representative));
        assertEquals(List.of(representative), service.listRepresentativesForCompany(1000));
        verify(representativeRepository)
                .listRepresentativesForCompany(1000);
    }
    @Test
    void listsAllCompanies() {
        Company company = new Company(
                1000,
                "Acme",
                null,
                null,
                null,
                null,
                null
        );
        when(companyRepository.listCompanies())
                .thenReturn(List.of(company));
        assertEquals(List.of(company), service.listCompanies());
    }
    @Test
    void unknownCompanyHasNoRepresentatives() {
        when(representativeRepository.listRepresentativesForCompany(5))
                .thenReturn(List.of());
        assertEquals(0, service.listRepresentativesForCompany(5).size());
    }
}
