package it.gov.pagopa.spontaneouspayment.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends CosmosRepository<Organization, String> {

    @Query("select TOP 1 * from c where c.fiscalCode = @organizationFiscalCode and EXISTS (SELECT VALUE t FROM t IN c.services WHERE t.id = @serviceId)")
    List<Organization> getCreditInstitutionByOrgFiscCodeAndServiceId(@Param("organizationFiscalCode") String organizationFiscalCode, @Param("serviceId") String serviceId);
    
    Optional<Organization> findByFiscalCode(String organizationFiscalCode);
}
