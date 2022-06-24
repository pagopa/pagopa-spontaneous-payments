package it.gov.pagopa.spontaneouspayment.service;

import java.util.stream.StreamSupport;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.cosmos.models.PartitionKey;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
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
    
    public Organization createECEnrollment(String organizationFiscalCode,
			@NotBlank String serviceId, EnrollmentModel enrollmentModel) {
    	Iterable<it.gov.pagopa.spontaneouspayment.entity.Service> allServices = serviceRepository.findAll();
    	
    	// check if the service the EC wants to enroll is configured in the database
    	boolean exists = StreamSupport.stream(allServices.spliterator(), false).anyMatch(s -> s.getId().equals(serviceId));
    	if (!exists) {
    		throw new AppException(AppError.SERVICE_NOT_FOUND, serviceId);
        }
    	
    	// check if the organization fiscal code exists
    	Organization orgEntity = this.checkOrganizationFiscalCode (organizationFiscalCode);
    	
    	// check if the enroll to service already exist for the organization fiscal code 
    	exists = orgEntity.getEnrollments()
                .parallelStream()
                .anyMatch(s -> s.getServiceId().equals(serviceId));
    	if (exists) {
    		throw new AppException(AppError.ENROLLMENT_TO_SERVICE_DUPLICATED, organizationFiscalCode, serviceId);
    	}
    	
    	// creates the new enrollment and adds it to the organization
    	orgEntity.getEnrollments().add(		
    	ServiceRef.builder()
    	.serviceId(serviceId)
    	.officeName(enrollmentModel.getOfficeName())
    	.iban(enrollmentModel.getIban()).build()
    	);
    	
    	return orgRepository.save(orgEntity);
	}

	
    public Organization updateECEnrollment(String organizationFiscalCode,
			String serviceId, EnrollmentModel enrollmentModel) {
    	
    	// check if the organization fiscal code exists
    	Organization orgEntity = this.checkOrganizationFiscalCode (organizationFiscalCode);
    	
    	// check if the enroll to service exist for the organization fiscal code 
    	ServiceRef enrollToUpdate = orgEntity.getEnrollments().stream().filter(s -> s.getServiceId().equals(serviceId)).findFirst()
    			.orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceId, organizationFiscalCode));
    	
    	// removes old enrollment
    	orgEntity.getEnrollments().remove(enrollToUpdate);
    	
    	// adds updated enrollment
    	orgEntity.getEnrollments().add(
    		ServiceRef.builder()
    		.serviceId(serviceId)
    		.officeName(enrollmentModel.getOfficeName())
    		.iban(enrollmentModel.getIban()).build()
    	);
    	
    	return orgRepository.save(orgEntity);
	}
    
    public Organization updateEC(String organizationFiscalCode,
    		OrganizationModel organizationModel) {
    	
    	// check if the organization fiscal code exists
    	Organization orgEntity = this.checkOrganizationFiscalCode (organizationFiscalCode);
    	
    	// update EC info
    	orgEntity.setCompanyName(organizationModel.getCompanyName());
    	orgEntity.setStatus(organizationModel.getStatus());
    	
    	
    	return orgRepository.save(orgEntity);
	}
    
    
    public void deleteECEnrollment(String organizationFiscalCode,
			String serviceId) {
    	// check if the organization fiscal code exists
    	Organization orgEntity = this.checkOrganizationFiscalCode (organizationFiscalCode);
    	
    	// check if the enroll to service exist for the organization fiscal code 
    	ServiceRef enrollToRemove = orgEntity.getEnrollments().stream().filter(s -> s.getServiceId().equals(serviceId)).findFirst()
    			.orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceId, organizationFiscalCode));
    	
    	// removes the enrollment
    	orgEntity.getEnrollments().remove(enrollToRemove);
    	
    	orgRepository.save(orgEntity);
    }
    
    
    public ServiceRef getSingleEnrollment (String organizationFiscalCode,
			String serviceId) {
    	// check if the organization fiscal code exists
    	Organization orgEntity = this.checkOrganizationFiscalCode (organizationFiscalCode);
    	
    	// if the enroll to service exists is returned 
    	return orgEntity.getEnrollments().stream().filter(s -> s.getServiceId().equals(serviceId)).findFirst()
    			.orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceId, organizationFiscalCode));
    	
    }
    
    public Organization getECEnrollments (String organizationFiscalCode) {
    	return orgRepository.findByFiscalCode(organizationFiscalCode)
    			.orElseThrow(() -> new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode));
    }
    
    
    private Organization checkOrganizationFiscalCode(String organizationFiscalCode) {
    	return orgRepository.findById(organizationFiscalCode, new PartitionKey(organizationFiscalCode))
    			.orElseThrow(() -> new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode)); 
	}

}
