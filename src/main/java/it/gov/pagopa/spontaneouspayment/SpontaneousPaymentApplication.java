package it.gov.pagopa.spontaneouspayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.DependsOn;
import org.springframework.retry.annotation.EnableRetry;

import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

@SpringBootApplication
@EnableCosmosRepositories("it.gov.pagopa.spontaneouspayment.repository")
@EnableCosmosAuditing
@EnableFeignClients
@EnableRetry
@DependsOn("expressionResolver")
public class SpontaneousPaymentApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(SpontaneousPaymentApplication.class, args);
    }
    
}
