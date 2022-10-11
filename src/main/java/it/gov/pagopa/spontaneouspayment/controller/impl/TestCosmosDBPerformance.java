package it.gov.pagopa.spontaneouspayment.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.gov.pagopa.spontaneouspayment.model.AppInfo;
import it.gov.pagopa.spontaneouspayment.model.ProblemJson;
import it.gov.pagopa.spontaneouspayment.service.PaymentsService;

@RestController()
public class TestCosmosDBPerformance {
	
	@Autowired
    private PaymentsService paymentsService;
	
	@Operation(summary = "test performance", description = "", security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")}, tags = {"Home"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/testcosmos/{organizationfiscalcode}/{serviceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testCosmosDB(@PathVariable("organizationfiscalcode") String organizationFiscalCode, @PathVariable("serviceId") String serviceId) {
		it.gov.pagopa.spontaneouspayment.entity.Service s = new it.gov.pagopa.spontaneouspayment.entity.Service();
		s.setId(serviceId);
		paymentsService.checkServiceOrganization(organizationFiscalCode, s);
		return new ResponseEntity<>(
                "\"The call for " + organizationFiscalCode + "and" + serviceId + " was successfull\"",
                HttpStatus.OK);
    }

}
