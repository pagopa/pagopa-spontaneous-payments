package it.gov.pagopa.spontaneouspayment.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePropertyModelResponse implements Serializable{
	
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 7156317962327532355L;

	@NotBlank
	private String name;

	private String type;

	private boolean isRequired;
}
