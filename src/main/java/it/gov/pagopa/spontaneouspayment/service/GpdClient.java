package it.gov.pagopa.spontaneouspayment.service;

import feign.FeignException;
import it.gov.pagopa.spontaneouspayment.config.FeignConfig;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "gpd", url = "${service.gpd.host}", configuration = FeignConfig.class)
public interface GpdClient {

    @Retryable(exclude = FeignException.FeignClientException.class, maxAttemptsExpression = "${retry.gpd.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.gpd.maxDelay}"))
    @GetMapping(value = "/organizations/{organizationfiscalcode}/debtpositions")
    PaymentPositionModel createDebtPosition(@PathVariable("organizationfiscalcode") String organizationFiscalCode,
                                            @RequestBody PaymentPositionModel paymentPositionModel);

}
