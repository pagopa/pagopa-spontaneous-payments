package it.gov.pagopa.spontaneouspayment.controller.impl;

import it.gov.pagopa.spontaneouspayment.controller.IPaymentsController;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsController implements IPaymentsController {

    @Autowired
    private PaymentsService paymentsService;


    @Override
    public ResponseEntity<PaymentPositionModel> createSpontaneousPayment(String organizationFiscalCode, SpontaneousPaymentModel spontaneousPayment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentsService.createSpontaneousPayment(organizationFiscalCode, spontaneousPayment));
    }

}
