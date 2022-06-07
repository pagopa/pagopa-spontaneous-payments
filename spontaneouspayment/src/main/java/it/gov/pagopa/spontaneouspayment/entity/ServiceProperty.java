package it.gov.pagopa.spontaneouspayment.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Container(containerName = "service_properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProperty {
	
	private String name;
	private String type;
	private boolean isRequired;
}
