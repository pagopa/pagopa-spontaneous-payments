package it.gov.pagopa.spontaneouspayment.payments.api.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.gov.pagopa.spontaneouspayment.payments.api.IPaymentsController;
import it.gov.pagopa.spontaneouspayment.payments.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.payments.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.payments.service.PaymentsService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PaymentsController implements IPaymentsController{
	
	private static final String LOG_BASE_HEADER_INFO = "[RequestMethod: %s] - [ClassMethod: %s] - [MethodParamsToLog: %s]";
    private static final String LOG_BASE_PARAMS_DETAIL = "organizationFiscalCode= %s";
    @Autowired
    private PaymentsService paymentsService;

	@Override
	public ResponseEntity<PaymentPositionModel> createSpontaneousPayment(String organizationFiscalCode,
			@Valid SpontaneousPaymentModel spontaneousPayment) {
		log.info(String.format(LOG_BASE_HEADER_INFO, "POST", "createSpontaneousPayment", String.format(LOG_BASE_PARAMS_DETAIL, organizationFiscalCode)));
		
		paymentsService.getServiceDetails(organizationFiscalCode, spontaneousPayment.getService());
		
		return null;
	}

}
