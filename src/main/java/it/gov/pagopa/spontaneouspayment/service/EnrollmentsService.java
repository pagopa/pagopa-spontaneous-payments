package it.gov.pagopa.spontaneouspayment.service;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;

@Service
public class EnrollmentsService {

    @Autowired
    private OrganizationRepository orgRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;

  
    public Organization createEC(Organization orgEntity) {
    	Iterable<it.gov.pagopa.spontaneouspayment.entity.Service> allServices = serviceRepository.findAll();
    	
    	// check if all services the EC wants to enroll are configured in the database
    	for (ServiceRef servRef : orgEntity.getEnrollments()) {
    		boolean exist = StreamSupport.stream(allServices.spliterator(), false).anyMatch(s -> s.getId().equals(servRef.getServiceId()));
    		if (!exist) {
                throw new AppException(AppError.SERVICE_NOT_FOUND, servRef.getServiceId());
            }
    	}
		
    	// check if organization fiscal code already exists
    	if (orgRepository.existsById(orgEntity.getFiscalCode())) {
    		throw new AppException(AppError.ENTITY_DUPLICATED, "Already exists an entity with id " + orgEntity.getFiscalCode());
    	}
    	
    	return orgRepository.save(orgEntity);
	}
    
    public Organization getECEnrollments (String organizationFiscalCode) {
    	Optional<Organization> org = orgRepository.findByFiscalCode(organizationFiscalCode);
    	
    	if (org.isEmpty()) {
    		throw new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode);
    	}
    	
    	return org.get();
    }


	

}
