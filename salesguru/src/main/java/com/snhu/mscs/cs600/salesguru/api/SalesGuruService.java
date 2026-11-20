// .java
// Sales Guru API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.snhu.mscs.cs600.salesguru.repository.CompanyRepository;
import com.snhu.mscs.cs600.salesguru.repository.RepresentativeRepository;
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
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    RepresentativeRepository representativeRepository;

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
    public List<Company> listCompanies() {
        return companyRepository.listCompanies();
    }
    public List<Representative> listRepresentatives() {
        return representativeRepository.listRepresentatives();
    }
    public List<Representative> listRepresentativesForCompany(Integer customerNumber) {
        return representativeRepository.listRepresentativesForCompany(customerNumber);
    }
}
