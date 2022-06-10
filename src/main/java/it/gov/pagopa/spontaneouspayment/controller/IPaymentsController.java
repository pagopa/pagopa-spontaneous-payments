package it.gov.pagopa.spontaneouspayment.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import it.gov.pagopa.spontaneouspayment.model.ProblemJson;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;


@Tag(name = "Payments API")
@RequestMapping
@Validated
public interface IPaymentsController {

	@Operation(summary = "The Organization creates a spontaneous payment.", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, operationId = "createSpontaneousPayment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Request created."),
            @ApiResponse(responseCode = "400", description = "Malformed request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Wrong or missing function key.", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "Conflict: duplicate debt position found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "500", description = "Service unavailable.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @PostMapping(value = "/organizations/{organizationfiscalcode}/spontaneouspayment",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentPositionModel> createSpontaneousPayment(
            @Parameter(description = "Organization fiscal code, the fiscal code of the Organization.", required = true)
            @PathVariable("organizationfiscalcode") String organizationFiscalCode,
            @Valid @RequestBody SpontaneousPaymentModel spontaneousPayment);
}
