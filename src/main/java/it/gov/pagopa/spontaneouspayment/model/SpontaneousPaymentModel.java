package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpontaneousPaymentModel implements Serializable {
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 8257339606190246109L;
	
	@Valid
	private DebtorModel debtor;
	@Valid
	private ServiceModel service;
	
	

}
