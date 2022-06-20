package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganizationModel implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -1181181033127626459L;
	
	private String companyName;
	private Status status;
}
