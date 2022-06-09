package it.gov.pagopa.spontaneouspayment.controller.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.gov.pagopa.spontaneouspayment.entity.CreditInstitution;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.controller.IPaymentsController;
import it.gov.pagopa.spontaneouspayment.model.ServicePropertyModel;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.service.PaymentsService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PaymentsController implements IPaymentsController {

	private static final String LOG_BASE_HEADER_INFO = "[RequestMethod: %s] - [ClassMethod: %s] - [MethodParamsToLog: %s]";
	private static final String LOG_BASE_PARAMS_DETAIL = "organizationFiscalCode= %s";
	@Autowired
	private PaymentsService paymentsService;

	@Override
	public ResponseEntity<PaymentPositionModel> createSpontaneousPayment(String organizationFiscalCode,
			@Valid SpontaneousPaymentModel spontaneousPayment) {
		log.info(String.format(LOG_BASE_HEADER_INFO, "POST", "createSpontaneousPayment",
				String.format(LOG_BASE_PARAMS_DETAIL, organizationFiscalCode)));

		CreditInstitution ec = paymentsService.getCreditInstitutionDetails(organizationFiscalCode,
				spontaneousPayment.getService());
		Service serviceConfiguration = paymentsService.getServiceDetails(spontaneousPayment.getService());

		return new ResponseEntity<>(this.createDebtPosition(ec, serviceConfiguration, spontaneousPayment), 
				HttpStatus.CREATED);
	}

	private PaymentPositionModel createDebtPosition(CreditInstitution ec, Service serviceConfiguration,
			SpontaneousPaymentModel spontaneousPayment) {
		
		for (ServiceProperty confProp : serviceConfiguration.getProperties()) {
			ServicePropertyModel prop = spontaneousPayment.getService().getProperties().stream().filter(o -> o.getName().equals(confProp.getName())).findAny()
					.orElse(null);
			if (null == prop) {
				throw new AppException(AppError.PROPERTY_NOT_FOUND, confProp.getName());
			}

		}
		
		this.callExternalService(serviceConfiguration);
		
		// TODO: finire di implementare la logica 
		return new PaymentPositionModel();

	}
	
	private void callExternalService(Service serviceConfiguration) {
		
	}

}
