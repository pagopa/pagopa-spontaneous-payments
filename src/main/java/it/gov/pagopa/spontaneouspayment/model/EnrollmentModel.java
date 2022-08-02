package it.gov.pagopa.spontaneouspayment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentModel implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = 8505165905680276253L;

    @NotBlank(message = "iban is required")
    private String iban;
    private String officeName;
    @NotBlank(message = "segregation code is required")
    private String segregationCode;
    @NotBlank(message = "remittance information is required")
    private String remittanceInformation;
    private String postalIban;
    

}
