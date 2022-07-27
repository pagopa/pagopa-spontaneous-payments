package it.gov.pagopa.spontaneouspayment.service.client;

import feign.FeignException;
import it.gov.pagopa.spontaneouspayment.config.FeignConfig;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;


@FeignClient(value = "externalservice", url = "runtime_url", configuration = FeignConfig.class)
public interface ExternalServiceClient {

    @Retryable(exclude = FeignException.FeignClientException.class, maxAttemptsExpression = "${retry.ext-service.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.ext-service.maxDelay}"))
    @PostMapping(value = "", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    PaymentOptionsModel getPaymentOption(URI baseUrl, @RequestBody String properties);
    

}
