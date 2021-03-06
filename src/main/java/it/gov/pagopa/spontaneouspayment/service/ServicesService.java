package it.gov.pagopa.spontaneouspayment.service;

import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
}
