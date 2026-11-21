// .java
// Sales Guru API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.stereotype.Service;
import com.snhu.mscs.cs600.salesguru.repositories.CompanyRepository;
import com.snhu.mscs.cs600.salesguru.repositories.RepresentativeRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import com.snhu.mscs.cs600.salesguru.entities.Representative;

/**
 * API Service for Sales Guru
 */
@Service
public class SalesGuruService {
    /**
     * Repositories
     */
    final CompanyRepository companyRepository;
    final RepresentativeRepository representativeRepository;

    /**
     * Constructor
     */
    public SalesGuruService(
            CompanyRepository companyRepository,
            RepresentativeRepository representativeRepository
    ) {
        this.companyRepository = companyRepository;
        this.representativeRepository = representativeRepository;
    }

    /**
     * API
     */
    @Transactional(readOnly = true)
    public List<Company> listCompanies() {
        return companyRepository.listCompanies();
    }
    @Transactional(readOnly = true)
    public List<Representative> listRepresentatives() {
        return representativeRepository.listRepresentatives();
    }
    @Transactional(readOnly = true)
    public List<Representative> listRepresentativesForCompany(Integer customerNumber) {
        return representativeRepository.listRepresentativesForCompany(customerNumber);
    }
}
