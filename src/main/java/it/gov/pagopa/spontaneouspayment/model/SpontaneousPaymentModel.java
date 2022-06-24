package it.gov.pagopa.spontaneouspayment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpontaneousPaymentModel implements Serializable {
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 8257339606190246109L;

	@Valid
	@NotNull
	private DebtorModel debtor;

	@Valid
	@NotNull
	private ServiceModel service;


}
