package it.gov.pagopa.spontaneouspayment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProperty {
	
	private String name;
	private String type;
	private boolean isRequired;
}
