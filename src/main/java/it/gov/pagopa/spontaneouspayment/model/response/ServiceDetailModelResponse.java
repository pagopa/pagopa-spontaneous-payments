package it.gov.pagopa.spontaneouspayment.model.response;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailModelResponse implements Serializable{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -3792257418379600493L;
	
	@NotBlank(message = "id is required")
	private String id;
	private String name;
	private String description;

	@NotBlank(message = "transfer category is required")
	private String transferCategory; // tassonomia

	@NotBlank(message = "remittance information is required")
	private String remittanceInformation; // causale

	@NotNull(message = "status is required")
	private Status status;

	@Valid
	private List<ServicePropertyModelResponse> properties;

}
