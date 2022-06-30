package it.gov.pagopa.spontaneouspayment.controller.impl;

import it.gov.pagopa.spontaneouspayment.controller.IServicesController;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceDetailModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceModelResponse;
import it.gov.pagopa.spontaneouspayment.service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Controller
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

}
