package it.gov.pagopa.spontaneouspayment.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import it.gov.pagopa.spontaneouspayment.controller.IServicesController;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.model.ServiceDetailModel;
import it.gov.pagopa.spontaneouspayment.model.ServiceDetailUpdModel;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceDetailModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceModelResponse;
import it.gov.pagopa.spontaneouspayment.service.ServicesService;

@RestController
public class ServicesController implements IServicesController {

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ServiceDetailModelResponse> getServiceDetails(@NotBlank String serviceId) {
        return new ResponseEntity<>(
                modelMapper.map(servicesService.getServiceDetails(serviceId), ServiceDetailModelResponse.class),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ServiceModelResponse>> getServices() {

        return new ResponseEntity<>(servicesService.getServices().stream()
                .map(entity -> modelMapper.map(entity, ServiceModelResponse.class)).collect(Collectors.toList()),
                HttpStatus.OK);
    }

	@Override
	public ResponseEntity<ServiceDetailModelResponse> createService(@Valid ServiceDetailModel serviceModel) {
		// flip model to entity
		Service serviceEntity = modelMapper.map(serviceModel, Service.class);
		return new ResponseEntity<>(
                modelMapper.map(servicesService.createService(serviceEntity), ServiceDetailModelResponse.class),
                HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ServiceDetailModelResponse> updateService(@NotBlank String serviceId, @Valid ServiceDetailUpdModel serviceUpdateModel) {
		// flip model to entity
		Service serviceEntity = modelMapper.map(serviceUpdateModel, Service.class);
		serviceEntity.setId(serviceId);
		return new ResponseEntity<>(
                modelMapper.map(servicesService.updateService(serviceEntity), ServiceDetailModelResponse.class),
                HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteService(@NotBlank String serviceId) {
		servicesService.deleteService(serviceId);
        return new ResponseEntity<>(
                "\"The service with id " + serviceId + " was successfully removed\"",
                HttpStatus.OK);
	}

}
