package it.gov.pagopa.spontaneouspayment.controller.impl;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.gov.pagopa.spontaneouspayment.controller.IServicesController;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceDetailModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.SevicesModelResponse;

@Controller
public class ServicesController implements IServicesController{

	@Override
	public ResponseEntity<ServiceDetailModelResponse> getServiceDetails(@NotBlank String serviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<SevicesModelResponse> getServices() {
		// TODO Auto-generated method stub
		return null;
	}

}
