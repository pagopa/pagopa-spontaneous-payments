package it.gov.pagopa.spontaneouspayment.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;

import it.gov.pagopa.spontaneouspayment.entity.CreditInstitution;

@Repository
public interface SpontaneousPaymentCIRepository extends CosmosRepository<CreditInstitution, String> {
	
	
	@Query("select TOP 1 * from c where c.organizationFiscalCode = @organizationFiscalCode and EXISTS (SELECT VALUE t FROM t IN c.services WHERE t.id = @serviceId)")
	List<CreditInstitution> getCreditInstitutionByOrgFiscCodeAndServiceId(@Param("organizationFiscalCode") String organizationFiscalCode, @Param("serviceId") String serviceId); 

}
