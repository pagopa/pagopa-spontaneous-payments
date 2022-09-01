package it.gov.pagopa.spontaneouspayment.service;

import com.azure.cosmos.models.PartitionKey;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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

        // check if all services the EC wants to enroll are configured in the database and not in disabled state
        Optional.ofNullable(orgEntity.getEnrollments()).ifPresent(servRef -> servRef.forEach(e -> {
        	Optional<it.gov.pagopa.spontaneouspayment.entity.Service> serv = 
        			StreamSupport.stream(allServices.spliterator(), true).filter(s -> s.getId().equals(e.getServiceId())).findAny();
        	// check the existence of the service in the database
        	if (serv.isEmpty()) {throw new AppException(AppError.SERVICE_NOT_FOUND, e.getServiceId());}
        	// check the service status
        	if (serv.get().getStatus().equals(Status.DISABLED)) {throw new AppException(AppError.SERVICE_NOT_ENABLED, serv.get().getId(), serv.get().getStatus());}
        }));
        			
        // check if organization fiscal code already exists
        if (orgRepository.existsById(orgEntity.getFiscalCode())) {
            throw new AppException(AppError.ENTITY_DUPLICATED, "Already exists an entity with id " + orgEntity.getFiscalCode());
        }

        return orgRepository.save(orgEntity);
    }

    public Organization createECEnrollment(String organizationFiscalCode,
                                           @NotBlank String serviceId, EnrollmentModel enrollmentModel) {
    	
    	// check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);
    	
        Iterable<it.gov.pagopa.spontaneouspayment.entity.Service> allServices = serviceRepository.findAll();

        Optional<it.gov.pagopa.spontaneouspayment.entity.Service> serv = 
        		StreamSupport.stream(allServices.spliterator(), true).filter(s -> s.getId().equals(serviceId)).findAny();
        // check if the service the EC wants to enroll is configured in the database
        if (serv.isEmpty()) {throw new AppException(AppError.SERVICE_NOT_FOUND, serviceId);}
        // check the service status
    	if (serv.get().getStatus().equals(Status.DISABLED)) {throw new AppException(AppError.SERVICE_NOT_ENABLED, serv.get().getId(), serv.get().getStatus());}

        // check if the enroll to service already exist for the organization fiscal code
        boolean exists = Optional.ofNullable(orgEntity.getEnrollments()).orElseGet(Collections::emptyList)
                .parallelStream()
                .anyMatch(s -> s.getServiceId().equals(serviceId));
        if (exists) {
            throw new AppException(AppError.ENROLLMENT_TO_SERVICE_DUPLICATED, organizationFiscalCode, serviceId);
        }

        // creates the new enrollment and adds it to the organization
        List<ServiceRef> enrollments = Optional.ofNullable(orgEntity.getEnrollments()).orElseGet(ArrayList::new);
        enrollments.add(ServiceRef.builder()
                        .serviceId(serviceId)
                        .officeName(enrollmentModel.getOfficeName())
                        .iban(enrollmentModel.getIban())
                        .segregationCode(enrollmentModel.getSegregationCode())
                        .remittanceInformation(enrollmentModel.getRemittanceInformation())
                        .postalIban(enrollmentModel.getPostalIban())
                        .build());
        
        orgEntity.setEnrollments(enrollments);

        return orgRepository.save(orgEntity);
    }


    public Organization updateECEnrollment(String organizationFiscalCode,
                                           String serviceId, EnrollmentModel enrollmentModel) {

        // check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);

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
                        .iban(enrollmentModel.getIban())
                        .segregationCode(enrollmentModel.getSegregationCode())
                        .remittanceInformation(enrollmentModel.getRemittanceInformation())
                        .postalIban(enrollmentModel.getPostalIban())
                        .build()
        );

        return orgRepository.save(orgEntity);
    }

    public Organization updateEC(String organizationFiscalCode,
                                 OrganizationModel organizationModel) {

        // check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);

        // update EC info
        orgEntity.setCompanyName(organizationModel.getCompanyName());
        orgEntity.setStatus(organizationModel.getStatus());


        return orgRepository.save(orgEntity);
    }


    public void deleteECEnrollment(String organizationFiscalCode,
                                   String serviceId) {
        // check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);

        // check if the enroll to service exist for the organization fiscal code
        ServiceRef enrollToRemove = orgEntity.getEnrollments().stream().filter(s -> s.getServiceId().equals(serviceId)).findFirst()
                .orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceId, organizationFiscalCode));

        // removes the enrollment
        orgEntity.getEnrollments().remove(enrollToRemove);

        orgRepository.save(orgEntity);
    }

    public void deleteEC(String organizationFiscalCode) {
        // check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);

        orgRepository.delete(orgEntity);
    }


    public ServiceRef getSingleEnrollment(String organizationFiscalCode,
                                          String serviceId) {
        // check if the organization fiscal code exists
        Organization orgEntity = this.checkOrganizationFiscalCode(organizationFiscalCode);

        // if the enroll to service exists is returned
        return orgEntity.getEnrollments().stream().filter(s -> s.getServiceId().equals(serviceId)).findFirst()
                .orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceId, organizationFiscalCode));

    }

    public Organization getECEnrollments(String organizationFiscalCode) {
        return orgRepository.findByFiscalCode(organizationFiscalCode)
                .orElseThrow(() -> new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode));
    }


    private Organization checkOrganizationFiscalCode(String organizationFiscalCode) {
        return orgRepository.findById(organizationFiscalCode, new PartitionKey(organizationFiscalCode))
                .orElseThrow(() -> new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode));
    }

}
