package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationModel implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -1181181033127626459L;
	
	private String companyName;
	private Status status;
}
