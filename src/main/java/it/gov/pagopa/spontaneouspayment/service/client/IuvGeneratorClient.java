package it.gov.pagopa.spontaneouspayment.service.client;

import it.gov.pagopa.spontaneouspayment.config.IuvGeneratorConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import feign.FeignException;
import it.gov.pagopa.spontaneouspayment.model.IuvGenerationModel;
import it.gov.pagopa.spontaneouspayment.model.response.IuvGenerationModelResponse;


@FeignClient(value = "iuvgenerator", url = "${service.iuv.generator.host}",configuration = IuvGeneratorConfig.class)
public interface IuvGeneratorClient {

    @Retryable(exclude = FeignException.FeignClientException.class, maxAttemptsExpression = "${retry.iuv-generator.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.iuv-generator.maxDelay}"))
    @PostMapping(value = "/organizations/{organizationfiscalcode}/iuv")
    IuvGenerationModelResponse generateIUV(@PathVariable("organizationfiscalcode") String organizationFiscalCode,
                                            @RequestBody IuvGenerationModel paymentPositionModel);

}
