package it.gov.pagopa.spontaneouspayment.payments.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpontaneousPaymentModel implements Serializable {
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 8257339606190246109L;
	
	private DebtorModel debtor;
	private ServiceModel service;
	
	

}
