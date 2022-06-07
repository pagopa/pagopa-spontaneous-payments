package it.gov.pagopa.spontaneouspayment.payments.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import feign.FeignException;
import it.gov.pagopa.spontaneouspayment.config.FeignConfig;
import it.gov.pagopa.spontaneouspayment.payments.model.response.PaymentPositionModel;


@FeignClient(value = "gpd", url = "${service.gpd.host}", configuration = FeignConfig.class)
public interface GpdClient {

    @Retryable(exclude = FeignException.FeignClientException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    @GetMapping(value = "/organizations/{organizationfiscalcode}/debtpositions")
    PaymentPositionModel createDebtPosition(@PathVariable("organizationfiscalcode") String organizationFiscalCode,
    		@RequestBody PaymentPositionModel paymentPositionModel);

}
