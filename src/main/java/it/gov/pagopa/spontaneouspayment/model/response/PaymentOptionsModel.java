package it.gov.pagopa.spontaneouspayment.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOptionsModel {

    List<PaymentOptionModel> paymentOption;
}
