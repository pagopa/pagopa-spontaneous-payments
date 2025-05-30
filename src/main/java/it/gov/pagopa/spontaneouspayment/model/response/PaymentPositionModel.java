package it.gov.pagopa.spontaneouspayment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.spontaneouspayment.model.enumeration.DebtPositionStatus;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPositionModel implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = 1509046053787358148L;


    @NotBlank(message = "iupd is required")
    private String iupd;

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

    @Schema(description = "feature flag to enable the debt position to expire after the due date", example = "false", defaultValue = "false")
    @NotBlank(message = "switchToExpired is required")
    private Boolean switchToExpired;

    // Payment Position properties
    @NotBlank(message = "company name is required")
    private String companyName; // es. Comune di Roma
    private String officeName; // es. Ufficio Tributi
    private LocalDateTime validityDate;
    @JsonProperty(access = Access.READ_ONLY)
    private DebtPositionStatus status;

    @Builder.Default
    private List<PaymentOptionModel> paymentOption = new ArrayList<>();

    public void addPaymentOptions(PaymentOptionModel paymentOpt) {
        paymentOption.add(paymentOpt);
    }

    public void removePaymentOptions(PaymentOptionModel paymentOpt) {
        paymentOption.remove(paymentOpt);
    }
}
