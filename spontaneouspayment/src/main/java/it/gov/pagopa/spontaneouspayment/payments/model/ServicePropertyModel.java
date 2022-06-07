package it.gov.pagopa.spontaneouspayment.payments.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServicePropertyModel implements Serializable {
	
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -4881284106551118200L;
	
	private String name;
	private String value;
}
