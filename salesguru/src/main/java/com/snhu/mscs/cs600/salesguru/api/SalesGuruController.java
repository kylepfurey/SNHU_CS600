// .java
// Sales Guru API Controller
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import com.snhu.mscs.cs600.salesguru.entities.Representative;

/**
 * REST API for Sales Guru
 */
@RestController
@RequestMapping("/api/v1")
public class SalesGuruController {
    /**
     * Services
     */
    final SalesGuruService salesGuruService;

    /**
     * Constructor
     */
    public SalesGuruController(SalesGuruService salesGuruService) {
        this.salesGuruService = salesGuruService;
    }

    /**
     * API
     */
    @GetMapping("/version")
    public String getVersion() {
        return "Version-2.0";
    }
    @GetMapping("/companies")
    public List<Company> listCompanies() {
        return salesGuruService.listCompanies();
    }
    @GetMapping("/representatives")
    public List<Representative> listRepresentatives() {
        return salesGuruService.listRepresentatives();
    }
    @GetMapping("/repsForCompany/{customerNumber}")
    public List<Representative> listRepresentativesForCompany(
            @PathVariable
            Integer customerNumber
    ) {
        return salesGuruService.listRepresentativesForCompany(customerNumber);
    }
}
