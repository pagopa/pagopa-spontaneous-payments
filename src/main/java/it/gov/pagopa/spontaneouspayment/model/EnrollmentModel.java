package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentModel implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 8505165905680276253L;
	
	private String iban;
	private String officeName;

}
