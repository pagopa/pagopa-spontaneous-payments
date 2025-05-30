package it.gov.pagopa.spontaneouspayment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRef {

    private String serviceId;

    private String officeName; // es. Ufficio Tributi

    @NotBlank(message = "iban is required")
    private String iban;
    
    @NotBlank(message = "segregation code is required")
    private String segregationCode;
    
    @NotBlank(message = "remittance information is required")
    private String remittanceInformation; // causale

    private String postalIban;

    @NotBlank(message = "switchToExpired is required")
    private boolean switchToExpired;

}
