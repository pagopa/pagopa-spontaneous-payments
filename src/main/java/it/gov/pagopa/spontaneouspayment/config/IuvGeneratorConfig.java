package it.gov.pagopa.spontaneouspayment.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import static it.gov.pagopa.spontaneouspayment.utils.Constants.APIM_SUBSCRIPTION_KEY;

public class IuvGeneratorConfig extends FeignConfig {

    @Value("${authorization.iuv.generator.subscription-key}")
    private String iuvGeneratorSubscriptionKey;

    @Bean
    public RequestInterceptor subscriptionKey() {
        return requestTemplate -> requestTemplate.header(APIM_SUBSCRIPTION_KEY, iuvGeneratorSubscriptionKey);
    }

}
