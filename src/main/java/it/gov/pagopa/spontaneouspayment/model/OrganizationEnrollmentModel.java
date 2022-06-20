package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganizationEnrollmentModel implements Serializable{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 3009315407053476788L;
	
	@NotBlank(message = "company name is required")
	private String companyName;
	@NotBlank(message = "iban is required")
	private String iban;
	private String officeName;

}
