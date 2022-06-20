package it.gov.pagopa.spontaneouspayment.controller.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.gov.pagopa.spontaneouspayment.controller.IEnrollmentsController;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.response.EnrollmentModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.OrganizationModelResponse;

@Controller
public class EnrollmentsController implements IEnrollmentsController{

	@Override
	public ResponseEntity<OrganizationModelResponse> createECAndFirstServiceEnrollment(
			@NotBlank String organizationFiscalCode, @NotBlank String serviceId,
			@Valid OrganizationEnrollmentModel organizationEnrollmentModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<OrganizationModelResponse> updateECEnrollment(@NotBlank String organizationFiscalCode,
			@NotBlank String serviceId, @Valid EnrollmentModel enrollmentModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<OrganizationModelResponse> updateEC(@NotBlank String organizationFiscalCode,
			@Valid OrganizationModel organizationModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> deleteECEnrollment(@NotBlank String organizationFiscalCode,
			@NotBlank String serviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<EnrollmentModelResponse> getSingleEnrollment(@NotBlank String organizationFiscalCode,
			@NotBlank String serviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<OrganizationModelResponse> getECEnrollments(@NotBlank String organizationFiscalCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
