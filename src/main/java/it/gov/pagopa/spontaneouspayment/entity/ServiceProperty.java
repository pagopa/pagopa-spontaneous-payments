package it.gov.pagopa.spontaneouspayment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProperty {

	@NotBlank
	private String name;

	private String type;

	private boolean isRequired;
}
