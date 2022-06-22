package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationEnrollmentModel implements Serializable{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = 3009315407053476788L;
	
	@NotBlank(message = "company name is required")
	private String companyName;
	
	@Valid
	private List<CreateEnrollmentModel> enrollments;

}
