package it.gov.pagopa.spontaneouspayment.repository;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import it.gov.pagopa.spontaneouspayment.entity.Service;

@Repository
public interface SpontaneousPaymentServiceRepository extends CosmosRepository<Service, String> {

}
