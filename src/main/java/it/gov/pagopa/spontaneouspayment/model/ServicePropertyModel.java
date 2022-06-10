package it.gov.pagopa.spontaneouspayment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServicePropertyModel implements Serializable {

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -4881284106551118200L;

	@NotBlank
	private String name;

	private String value;
}
