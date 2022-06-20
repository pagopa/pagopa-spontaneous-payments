package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateEnrollmentModel implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -3542093274149923876L;
	
	@NotBlank(message = "service id is required")
	private String serviceId;
	@NotBlank(message = "iban is required")
	private String iban;
	private String officeName;

}
