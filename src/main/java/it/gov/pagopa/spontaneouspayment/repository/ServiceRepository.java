package it.gov.pagopa.spontaneouspayment.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends CosmosRepository<Service, String> {

}
