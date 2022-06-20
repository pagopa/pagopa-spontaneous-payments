package it.gov.pagopa.spontaneouspayment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;

@SpringBootApplication
@EnableCosmosRepositories("it.gov.pagopa.spontaneouspayment.repository")
@EnableCosmosAuditing
@DependsOn("expressionResolver")
public class SpontaneousPaymentApplication implements CommandLineRunner {

    @Autowired
    private OrganizationRepository ciRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpontaneousPaymentApplication.class, args);
    }

    public void run(String... var1) {
        Organization ci = new Organization();
        ci.setFiscalCode("organizationTest");
        ci.setCompanyName("Comune di Roma");
        ci.setStatus(Status.ENABLED);


        Service s1 = new Service();
        s1.setId("id-servizio-1");
        s1.setTransferCategory("tassonomia-1");
        s1.setRemittanceInformation("causale-1");
        s1.setBasePath("base-path-1");
        s1.setEndpoint("endpont-1");

        ServiceProperty sp1 = new ServiceProperty("propName1", "number", true);
        List<ServiceProperty> properties1 = new ArrayList<>();
        properties1.add(sp1);
        s1.setProperties(properties1);

        Service s2 = new Service();
        s2.setId("id-servizio-2");
        s2.setTransferCategory("tassonomia-2");
        s2.setRemittanceInformation("causale-2");
        s2.setBasePath("base-path-2");
        s2.setEndpoint("endpont-2");

        ServiceProperty sp2 = new ServiceProperty("propName2", "string", true);
        List<ServiceProperty> properties2 = new ArrayList<>();
        properties2.add(sp2);
        s2.setProperties(properties2);

        ServiceRef ref1 = new ServiceRef();
        ref1.setServiceId("id-servizio-1");
        ref1.setIban("iban-1");
        ServiceRef ref2 = new ServiceRef();
        ref2.setServiceId("id-servizio-2");
        ref2.setIban("iban-2");
        List<ServiceRef> servicesRef = new ArrayList<>();
        servicesRef.add(ref1);
        servicesRef.add(ref2);


        ci.setEnrollments(servicesRef);


        ciRepository.deleteAll();
        ciRepository.save(ci);
        serviceRepository.deleteAll();
        serviceRepository.save(s1);
        serviceRepository.save(s2);

    }

}
