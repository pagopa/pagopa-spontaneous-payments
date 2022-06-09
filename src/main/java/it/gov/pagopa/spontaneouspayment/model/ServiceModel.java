package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceModel implements Serializable{

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 3545685764645620201L;
	@NotBlank (message = "service id is required")
	private String id;
    private List<ServicePropertyModel> properties;

}
