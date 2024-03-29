package it.gov.pagopa.spontaneouspayment.controller;

import java.util.List;

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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.spontaneouspayment.model.ProblemJson;
import it.gov.pagopa.spontaneouspayment.model.ServiceDetailModel;
import it.gov.pagopa.spontaneouspayment.model.ServiceDetailUpdModel;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceDetailModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.ServiceModelResponse;


@Tag(name = "Services API")
@RequestMapping
@Validated
public interface IServicesController {

    @Operation(summary = "Return the single service details.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "getServiceDetails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained single service details.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "ServiceDetailModelResponse", implementation = ServiceDetailModelResponse.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "No service found.", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDetailModelResponse> getServiceDetails(
            @Parameter(description = "The service id for which to have the details.", required = true)
            @NotBlank @PathVariable("serviceId") String serviceId);

    @Operation(summary = "Return all services.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "getServices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained all services.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(name = "ServiceModelResponse", implementation = ServiceModelResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/services",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ServiceModelResponse>> getServices();
    
    @Operation(summary = "The user creates a service.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "createService")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "ServiceDetailModelResponse", implementation = ServiceDetailModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "The service to create already exists.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PostMapping(value = "/services",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDetailModelResponse> createService(
            @Valid @RequestBody ServiceDetailModel serviceModel);
    
    @Operation(summary = "The user updates a service.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "updateService")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request updated.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "ServiceDetailModelResponse", implementation = ServiceDetailModelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PutMapping(value = "/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceDetailModelResponse> updateService(
    		@Parameter(description = "The service id to update.", required = true) @NotBlank @PathVariable("serviceId") String serviceId,
            @Valid @RequestBody ServiceDetailUpdModel serviceUpdateModel);
    
    @Operation(summary = "The user deletes a service.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "deleteService")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request deleted.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "StringResponse", implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @DeleteMapping(value = "/services/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteService(@Parameter(description = "The service id to delete.", required = true) @NotBlank @PathVariable("serviceId") String serviceId);
}
