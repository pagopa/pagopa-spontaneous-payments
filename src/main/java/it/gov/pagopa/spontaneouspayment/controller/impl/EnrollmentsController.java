package it.gov.pagopa.spontaneouspayment.controller.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import it.gov.pagopa.spontaneouspayment.controller.IEnrollmentsController;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.model.response.EnrollmentModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.OrganizationModelResponse;
import it.gov.pagopa.spontaneouspayment.service.EnrollmentsService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EnrollmentsController implements IEnrollmentsController{
	
	private static final String LOG_BASE_HEADER_INFO = "[RequestMethod: %s] - [ClassMethod: %s] - [MethodParamsToLog: %s]";
    private static final String LOG_BASE_PARAMS_DETAIL = "organizationFiscalCode= %s";
    
    @Autowired
    private EnrollmentsService enrollmentsService;
    
    @Autowired
    private ModelMapper modelMapper;

	@Override
	public ResponseEntity<OrganizationModelResponse> createEC(@NotBlank String organizationFiscalCode,
			@Valid OrganizationEnrollmentModel organizationEnrollmentModel) {
		log.info(String.format(LOG_BASE_HEADER_INFO, "POST", "createEC", String.format(LOG_BASE_PARAMS_DETAIL, organizationFiscalCode)));
		 // flip model to entity
		Organization orgEntity = modelMapper.map(organizationEnrollmentModel, Organization.class);
		orgEntity.setFiscalCode(organizationFiscalCode);
		orgEntity.setStatus(Status.ENABLED);
		
		return new ResponseEntity<>(
				modelMapper.map(enrollmentsService.createEC(orgEntity), OrganizationModelResponse.class), 
				HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<OrganizationModelResponse> createECEnrollment(@NotBlank String organizationFiscalCode,
			@NotBlank String serviceId, @Valid EnrollmentModel enrollmentModel) {
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
		log.info(String.format(LOG_BASE_HEADER_INFO, "GET", "getECEnrollments", String.format(LOG_BASE_PARAMS_DETAIL, organizationFiscalCode)));
		return new ResponseEntity<>(
				modelMapper.map(enrollmentsService.getECEnrollments(organizationFiscalCode), OrganizationModelResponse.class), 
				HttpStatus.OK);
	}

}
