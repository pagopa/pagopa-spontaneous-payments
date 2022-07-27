package it.gov.pagopa.spontaneouspayment.config;

import com.azure.spring.data.cosmos.common.ExpressionResolver;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockitoConfig {

    @Bean
    @Primary
    public OrganizationRepository organizationRepository() {
        return Mockito.mock(OrganizationRepository.class);
    }


    @Bean
    @Primary
    public ServiceRepository serviceRepository() {
        return Mockito.mock(ServiceRepository.class);
    }

    @Bean
    @Primary
    public ExpressionResolver expressionResolver() {
        return Mockito.mock(ExpressionResolver.class);
    }

}
