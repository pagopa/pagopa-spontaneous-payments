package it.gov.pagopa.spontaneouspayment.model.response;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceModelResponse implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -6620381879835313811L;
	
	@NotBlank(message = "id is required")
	private String id;
	@NotBlank(message = "name is required")
	private String name;
	private String description;

}
