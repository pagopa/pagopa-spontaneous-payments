package it.gov.pagopa.spontaneouspayment.service;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import feign.FeignException;
import it.gov.pagopa.spontaneouspayment.config.FeignConfig;


@FeignClient(value = "externalservice", url = "runtime_url", configuration = FeignConfig.class)
public interface ExternalServiceClient {

    @Retryable(exclude = FeignException.FeignClientException.class, maxAttemptsExpression = "${retry.ext-service.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.ext-service.maxDelay}"))
    @PostMapping(value = "/paymentoptions", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    String getPaymentOption(URI baseUrl,
                                            @RequestBody String properties);
    

}
