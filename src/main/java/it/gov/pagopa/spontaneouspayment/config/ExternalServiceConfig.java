package it.gov.pagopa.spontaneouspayment.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import static it.gov.pagopa.spontaneouspayment.utils.Constants.APIM_SUBSCRIPTION_KEY;

public class ExternalServiceConfig extends FeignConfig{
    @Value("${authorization.external.services.subscription-key}")
    private String externalServicesSubscriptionKey;

    @Bean
    public RequestInterceptor subscriptionKey() {
        return requestTemplate -> requestTemplate.header(APIM_SUBSCRIPTION_KEY, externalServicesSubscriptionKey);
    }
}
