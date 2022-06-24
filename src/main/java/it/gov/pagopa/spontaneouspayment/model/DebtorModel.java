package it.gov.pagopa.spontaneouspayment.model;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtorModel implements Serializable {

    /**
     * generated serialVersionUID
     */

    private static final long serialVersionUID = 7372093886291859112L;

    @NotNull(message = "type is required")
    private Type type;

    @NotBlank(message = "fiscal code is required")
    private String fiscalCode;

    @NotBlank(message = "full name is required")
    private String fullName;

    private String streetName;

    private String civicNumber;

    private String postalCode;

    private String city;

    private String province;

    private String region;

    private String country;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String phone;

}
