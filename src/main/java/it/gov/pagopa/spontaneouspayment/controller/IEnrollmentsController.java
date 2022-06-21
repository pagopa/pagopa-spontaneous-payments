package it.gov.pagopa.spontaneouspayment.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.ProblemJson;
import it.gov.pagopa.spontaneouspayment.model.response.EnrollmentModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.OrganizationModelResponse;


@Tag(name = "Enrollments API")
@RequestMapping
@Validated
public interface IEnrollmentsController {

    @Operation(summary = "The organization creates a creditor institution with possible enrollments to services.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "createEC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "OrganizationModelResponse", implementation = OrganizationModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "The organization to create already exists.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PostMapping(value = "/organizations/{organizationFiscalCode}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrganizationModelResponse> createEC(
            @Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Valid @RequestBody OrganizationEnrollmentModel organizationEnrollmentModel);
    
    @Operation(summary = "The organization creates an enrollment to a service for the creditor institution.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "createECEnrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "OrganizationModelResponse", implementation = OrganizationModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the creditor institution or the enroll service.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "409", description = "The enrollment to the service already exists.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PostMapping(value = "/organizations/{organizationFiscalCode}/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrganizationModelResponse> createECEnrollment(
            @Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Parameter(description = "The service id to enroll.", required = true)
            @NotBlank @PathVariable("serviceId") String serviceId,
            @Valid @RequestBody EnrollmentModel enrollmentModel);
    
    @Operation(summary = "The organization update an enrollment to a service for the creditor institution.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "updateECEnrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request updated.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "OrganizationModelResponse", implementation = OrganizationModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the creditor institution or the enroll service.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PutMapping(value = "/organizations/{organizationFiscalCode}/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrganizationModelResponse> updateECEnrollment(
            @Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Parameter(description = "The service id to enroll.", required = true)
            @NotBlank @PathVariable("serviceId") String serviceId,
            @Valid @RequestBody EnrollmentModel enrollmentModel);
    
    @Operation(summary = "The organization updates the creditor institution.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "updateEC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request updated.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "OrganizationModelResponse", implementation = OrganizationModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the creditor institution.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PutMapping(value = "/organizations/{organizationFiscalCode}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrganizationModelResponse> updateEC(
            @Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Valid @RequestBody OrganizationModel organizationModel);
    
    @Operation(summary = "The organization deletes the enrollment to service for the creditor institution.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "deleteECEnrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request deleted."),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the creditor institution or the enroll service.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @DeleteMapping(value = "/organizations/{organizationFiscalCode}/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteECEnrollment(
    		@Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Parameter(description = "The service id to enroll.", required = true)
            @NotBlank @PathVariable("serviceId") String serviceId);
    
    @Operation(summary = "Return the single enrollment to a service.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "getSingleEnrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained single enrollment.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "EnrollmentModelResponse", implementation = EnrollmentModelResponse.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the enroll service.", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/organizations/{organizationFiscalCode}/services/{serviceId}",
            produces = {"application/json"})
    ResponseEntity<EnrollmentModelResponse> getSingleEnrollment(
    		@Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode,
            @Parameter(description = "The service id to enroll.", required = true)
            @NotBlank @PathVariable("serviceId") String serviceId);
    
    @Operation(summary = "Return all enrollments for a creditor institution.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "getECEnrollments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained all enrollments for the creditor institution.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "OrganizationModelResponse", implementation = OrganizationModelResponse.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found the creditor institution.", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/organizations/{organizationFiscalCode}",
            produces = {"application/json"})
    ResponseEntity<OrganizationModelResponse> getECEnrollments(
    		@Parameter(description = "The fiscal code of the Organization.", required = true)
            @NotBlank @PathVariable("organizationFiscalCode") String organizationFiscalCode);
}
