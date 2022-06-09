package it.gov.pagopa.spontaneouspayment.entity;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRef {
	private String id;
	private String officeName; // es. Ufficio Tributi
	@NotBlank(message = "iban is required")
	private String iban;
    private String postalIban;
	
}
