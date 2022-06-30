package it.gov.pagopa.spontaneouspayment.controller.impl;

import it.gov.pagopa.spontaneouspayment.controller.IEnrollmentsController;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.model.response.EnrollmentModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.OrganizationModelResponse;
import it.gov.pagopa.spontaneouspayment.service.EnrollmentsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Controller
public class EnrollmentsController implements IEnrollmentsController {

    @Autowired
    private EnrollmentsService enrollmentsService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<OrganizationModelResponse> createEC(@NotBlank String organizationFiscalCode,
                                                              @Valid OrganizationEnrollmentModel organizationEnrollmentModel) {

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
        return new ResponseEntity<>(
                modelMapper.map(enrollmentsService.createECEnrollment(organizationFiscalCode, serviceId, enrollmentModel),
                        OrganizationModelResponse.class),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrganizationModelResponse> updateECEnrollment(@NotBlank String organizationFiscalCode,
                                                                        @NotBlank String serviceId, @Valid EnrollmentModel enrollmentModel) {
        return new ResponseEntity<>(
                modelMapper.map(enrollmentsService.updateECEnrollment(organizationFiscalCode, serviceId, enrollmentModel),
                        OrganizationModelResponse.class),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrganizationModelResponse> updateEC(@NotBlank String organizationFiscalCode,
                                                              @Valid OrganizationModel organizationModel) {
        return new ResponseEntity<>(
                modelMapper.map(enrollmentsService.updateEC(organizationFiscalCode, organizationModel),
                        OrganizationModelResponse.class),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteECEnrollment(@NotBlank String organizationFiscalCode,
                                                     @NotBlank String serviceId) {
        enrollmentsService.deleteECEnrollment(organizationFiscalCode, serviceId);
        return new ResponseEntity<>(
                "\"The enrollment to service " + serviceId + " for " + organizationFiscalCode + " was successfully removed\"",
                HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> deleteEC(@NotBlank String organizationFiscalCode) {
        enrollmentsService.deleteEC(organizationFiscalCode);
        return new ResponseEntity<>(
                "\"The organization " + organizationFiscalCode + " was successfully removed\"",
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EnrollmentModelResponse> getSingleEnrollment(@NotBlank String organizationFiscalCode,
                                                                       @NotBlank String serviceId) {
        return new ResponseEntity<>(
                modelMapper.map(enrollmentsService.getSingleEnrollment(organizationFiscalCode, serviceId), EnrollmentModelResponse.class),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrganizationModelResponse> getECEnrollments(@NotBlank String organizationFiscalCode) {
        return new ResponseEntity<>(
                modelMapper.map(enrollmentsService.getECEnrollments(organizationFiscalCode), OrganizationModelResponse.class),
                HttpStatus.OK);
    }


}
