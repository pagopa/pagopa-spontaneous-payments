package it.gov.pagopa.spontaneouspayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.DependsOn;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
@DependsOn("expressionResolver")
public class SpontaneousPaymentApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(SpontaneousPaymentApplication.class, args);
    }
    
}
