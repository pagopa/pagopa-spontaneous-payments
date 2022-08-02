package it.gov.pagopa.spontaneouspayment.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentModelResponse implements Serializable {
    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -6830998393310794316L;

    @NotBlank(message = "service ID is required")
    private String serviceId;
    @NotBlank(message = "iban is required")
    private String iban;
    private String officeName;
    @NotBlank(message = "segregation code is required")
    private String segregationCode;
    @NotBlank(message = "remittance information is required")
    private String remittanceInformation;
    private String postalIban;
}
