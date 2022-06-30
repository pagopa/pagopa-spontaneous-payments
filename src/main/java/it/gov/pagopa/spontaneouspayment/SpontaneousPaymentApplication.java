package it.gov.pagopa.spontaneouspayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

@SpringBootApplication
@EnableCosmosRepositories("it.gov.pagopa.spontaneouspayment.repository")
@EnableCosmosAuditing
@DependsOn("expressionResolver")
public class SpontaneousPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpontaneousPaymentApplication.class, args);
    }
}
