package it.gov.pagopa.spontaneouspayment.model.response;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationModelResponse implements Serializable {
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 6474656487948357626L;
	
	@NotBlank(message = "organization fiscal code is required")
    private String fiscalCode;

    @NotBlank(message = "company name is required")
    private String companyName;
    
    @NotNull(message = "status is required")
    private Status status;

    @Valid
	private List<EnrollmentModelResponse> enrollments;
	

}
