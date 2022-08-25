package it.gov.pagopa.spontaneouspayment.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<it.gov.pagopa.spontaneouspayment.entity.Service> getServices() {

        Iterable<it.gov.pagopa.spontaneouspayment.entity.Service> allServices = serviceRepository.findAll();

        return StreamSupport.stream(allServices.spliterator(), false)
                .collect(Collectors.toList());
    }

    public it.gov.pagopa.spontaneouspayment.entity.Service getServiceDetails(String serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(AppError.SERVICE_NOT_FOUND, serviceId));
    }
    
    public it.gov.pagopa.spontaneouspayment.entity.Service createService(it.gov.pagopa.spontaneouspayment.entity.Service serviceEntity) {
    	// check if service already exists
        if (serviceRepository.existsById(serviceEntity.getId())) {
            throw new AppException(AppError.ENTITY_DUPLICATED, "Already exists an entity with id " + serviceEntity.getId());
        }
        return serviceRepository.save(serviceEntity);
    }
    
    public it.gov.pagopa.spontaneouspayment.entity.Service updateService(it.gov.pagopa.spontaneouspayment.entity.Service serviceEntity) {
    	this.checkServiceExistence(serviceEntity.getId());
        return serviceRepository.save(serviceEntity);
    }
    
    public void deleteService(String serviceId) {
    	it.gov.pagopa.spontaneouspayment.entity.Service s = this.checkServiceExistence(serviceId);
        serviceRepository.delete(s);
    }
    
    private it.gov.pagopa.spontaneouspayment.entity.Service checkServiceExistence(String serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(AppError.SERVICE_NOT_FOUND, serviceId));
    }
    
    
    
    
}
