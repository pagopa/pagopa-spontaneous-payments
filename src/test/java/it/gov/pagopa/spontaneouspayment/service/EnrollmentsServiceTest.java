package it.gov.pagopa.spontaneouspayment.service;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.PropertyType;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Testcontainers
class EnrollmentsServiceTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@MockBean
	private OrganizationRepository ciRepository;

	@MockBean
	private ServiceRepository serviceRepository;

	@Container
	private static final CosmosDBEmulatorContainer emulator = new CosmosDBEmulatorContainer(
			DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));

	private static EnrollmentsService enrollmentsService;

	@BeforeAll
	public void setUp() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		enrollmentsService = spy(new EnrollmentsService(ciRepository, serviceRepository));

		tempFolder.create();
		Path keyStoreFile = tempFolder.newFile("azure-cosmos-emulator.keystore").toPath();
		KeyStore keyStore = emulator.buildNewKeyStore();
		keyStore.store(new FileOutputStream(keyStoreFile.toFile()), emulator.getEmulatorKey().toCharArray());

		System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
		System.setProperty("javax.net.ssl.trustStorePassword", emulator.getEmulatorKey());
		System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");

		CosmosAsyncClient client = new CosmosClientBuilder().gatewayMode().endpointDiscoveryEnabled(false)
				.endpoint(emulator.getEmulatorEndpoint()).key(emulator.getEmulatorKey()).buildAsyncClient();

		// creation of the database and containers
		CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists("db").block();

		assertEquals(201, databaseResponse.getStatusCode());
		CosmosContainerResponse containerResponse = client.getDatabase("db")
				.createContainerIfNotExists("creditor_institutions", "/fiscalCode").block();
		assertEquals(201, containerResponse.getStatusCode());
		containerResponse = client.getDatabase("db").createContainerIfNotExists("services", "/fiscalCode").block();
		assertEquals(201, containerResponse.getStatusCode());

		// loading the database with test data
		Organization ci = new Organization();
		ci.setFiscalCode("organizationTest");
		ci.setCompanyName("Comune di Roma");
		ci.setStatus(Status.ENABLED);

		Service s1 = new Service();
		s1.setId("id-servizio-1");
		s1.setTransferCategory("tassonomia-1");
		s1.setBasePath("base-path-1");
		s1.setEndpoint("endpont-1");

		ServiceProperty sp1 = new ServiceProperty("propName1", PropertyType.STRING, true);
		List<ServiceProperty> properties1 = new ArrayList<>();
		properties1.add(sp1);
		s1.setProperties(properties1);

		Service s2 = new Service();
		s2.setId("id-servizio-2");
		s2.setTransferCategory("tassonomia-2");
		s2.setBasePath("base-path-2");
		s2.setEndpoint("endpont-2");

		ServiceProperty sp2 = new ServiceProperty("propName2", PropertyType.STRING, true);
		List<ServiceProperty> properties2 = new ArrayList<>();
		properties2.add(sp2);
		s2.setProperties(properties2);
		
		Service s3 = new Service();
		s3.setId("id-servizio-3");
		s3.setTransferCategory("tassonomia-3");
		s3.setBasePath("base-path-3");
		s3.setEndpoint("endpont-3");

		ServiceProperty sp3 = new ServiceProperty("propName3", PropertyType.STRING, true);
		List<ServiceProperty> properties3 = new ArrayList<>();
		properties3.add(sp3);
		s3.setProperties(properties3);
		
		Service s4 = new Service();
		s4.setId("id-servizio-4");
		s4.setTransferCategory("tassonomia-4");
		s4.setBasePath("base-path-4");
		s4.setEndpoint("endpont-4");

		ServiceProperty sp4 = new ServiceProperty("propName4", PropertyType.STRING, true);
		List<ServiceProperty> properties4 = new ArrayList<>();
		properties4.add(sp4);
		s4.setProperties(properties4);

		ServiceRef ref1 = new ServiceRef();
		ref1.setServiceId("id-servizio-1");
		ref1.setIban("iban-1");
		ref1.setRemittanceInformation("causale-1");
		ServiceRef ref2 = new ServiceRef();
		ref2.setServiceId("id-servizio-2");
		ref2.setIban("iban-2");
		ref2.setRemittanceInformation("causale-2");
		List<ServiceRef> servicesRef = new ArrayList<>();
		servicesRef.add(ref1);
		servicesRef.add(ref2);

		ci.setEnrollments(servicesRef);

		ciRepository.deleteAll();
		ciRepository.save(ci);
		serviceRepository.deleteAll();
		serviceRepository.save(s1);
		serviceRepository.save(s2);
		serviceRepository.save(s3);
		serviceRepository.save(s4);
	}
	
	@AfterAll
	void teardown() {
		CosmosAsyncClient client = new CosmosClientBuilder().gatewayMode().endpointDiscoveryEnabled(false)
				.endpoint(emulator.getEmulatorEndpoint()).key(emulator.getEmulatorKey()).buildAsyncClient();
		client.getDatabase("db").delete();
		client.close();
		emulator.stop();
		emulator.close();
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");
		System.clearProperty("javax.net.ssl.trustStoreType");
	}
	

	@Test
	void getECEnrollments() {
		assertTrue(emulator.isRunning());
		Organization org = enrollmentsService.getECEnrollments("organizationTest");
		assertEquals("organizationTest", org.getFiscalCode());
		assertEquals("Comune di Roma", org.getCompanyName());
	}

	@Test
	void getECEnrollments_404() {
		assertTrue(emulator.isRunning());
		try {
			// get a non-existent organization -> must raise a 404 exception
			enrollmentsService.getECEnrollments("organizationFake");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void getSingleEnrollment() {
		assertTrue(emulator.isRunning());
		ServiceRef enrollment = enrollmentsService.getSingleEnrollment("organizationTest", "id-servizio-1");
		assertEquals("id-servizio-1", enrollment.getServiceId());
		assertNull(enrollment.getOfficeName());
		assertEquals("iban-1", enrollment.getIban());
	}
	
	@Test
	void getSingleEnrollment_404() {
		assertTrue(emulator.isRunning());
		try {
			// get a non-existent organization -> must raise a 404 exception
			enrollmentsService.getSingleEnrollment("organizationFake", "id-servizio-1");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		try {
			// get a non-existent enrollment -> must raise a 404 exception
			enrollmentsService.getSingleEnrollment("organizationTest", "id-servizio-fake");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void createEC() {
		assertTrue(emulator.isRunning());
		Organization ci = new Organization();
		ci.setFiscalCode("organizationNew");
		ci.setCompanyName("Comune di Milano");
		ci.setStatus(Status.ENABLED);
		ServiceRef ref1 = new ServiceRef();
		ref1.setServiceId("id-servizio-1");
		ref1.setIban("iban-1");
		List<ServiceRef> servicesRef = new ArrayList<>();
		servicesRef.add(ref1);
		ci.setEnrollments(servicesRef);
		Organization orgCreated = enrollmentsService.createEC(ci);
		assertEquals("organizationNew", orgCreated.getFiscalCode());
		assertEquals("Comune di Milano", orgCreated.getCompanyName());
		assertEquals(1, orgCreated.getEnrollments().size());
	}

	@Test
	void createEC_404() {
		assertTrue(emulator.isRunning());
		// creation of an organization with enroll to a service that does not exist
		Organization ci = new Organization();
		ci.setFiscalCode("organizationNoService");
		ci.setCompanyName("Comune di Milano");
		ci.setStatus(Status.ENABLED);
		ServiceRef ref1 = new ServiceRef();
		ref1.setServiceId("id-servizio-fake");
		ref1.setIban("iban-1");
		List<ServiceRef> servicesRef = new ArrayList<>();
		servicesRef.add(ref1);
		ci.setEnrollments(servicesRef);

		try {
			// must raise a 404 exception
			enrollmentsService.createEC(ci);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void createEC_409() {
		assertTrue(emulator.isRunning());
		// creation of an organization already present in the system
		Organization ci = new Organization();
		ci.setFiscalCode("organizationTest");
		ci.setCompanyName("Comune di Milano");
		ci.setStatus(Status.ENABLED);
		ServiceRef ref1 = new ServiceRef();
		ref1.setServiceId("id-servizio-1");
		ref1.setIban("iban-1");
		List<ServiceRef> servicesRef = new ArrayList<>();
		servicesRef.add(ref1);
		ci.setEnrollments(servicesRef);
		try {
			// must raise a 409 exception
			enrollmentsService.createEC(ci);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.CONFLICT, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void createECEnrollment() {
		assertTrue(emulator.isRunning());
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-3").officeName("Ufficio Tributario").build();
		Organization org = enrollmentsService.createECEnrollment("organizationTest", "id-servizio-3", enrollment);
		assertEquals("organizationTest", org.getFiscalCode());
		assertEquals("Comune di Roma", org.getCompanyName());
		// added an enrollment -> the size became 3
		assertEquals(3, org.getEnrollments().size());
	}
	
	@Test
	void createECEnrollment_404() {
		assertTrue(emulator.isRunning());
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-3").officeName("Ufficio Tributario").build();
		
		try {
			// non-existent organization -> must raise a 404 exception
			enrollmentsService.createECEnrollment("organizationFake", "id-servizio-1", enrollment);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		try {
			// non-existent service -> must raise a 404 exception
			enrollmentsService.createECEnrollment("organizationTest", "id-servizio-fake", enrollment);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void createECEnrollment_409() {
		assertTrue(emulator.isRunning());
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-3").officeName("Ufficio Tributario").build();
		
		try {
			// enrollment to service already exist -> must raise a 409 exception
			enrollmentsService.createECEnrollment("organizationTest", "id-servizio-1", enrollment);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.CONFLICT, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		
	}
	
	@Test
	void updateECEnrollment() {
		assertTrue(emulator.isRunning());
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-updated-2").officeName("Ufficio Tributario Updated").build();
		Organization org = enrollmentsService.updateECEnrollment("organizationTest", "id-servizio-2", enrollment);
		assertEquals("organizationTest", org.getFiscalCode());
		assertEquals("Comune di Roma", org.getCompanyName());
		ServiceRef updatedEnrollment = org.getEnrollments().stream().filter(s -> s.getServiceId().equals("id-servizio-2")).findFirst().get();
		assertEquals("iban-updated-2", updatedEnrollment.getIban());
		assertEquals("Ufficio Tributario Updated", updatedEnrollment.getOfficeName());
	}
	
	@Test
	void updateECEnrollment_404() {
		assertTrue(emulator.isRunning());
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-updated-2").officeName("Ufficio Tributario Updated").build();
		try {
			// non-existent organization -> must raise a 404 exception
			enrollmentsService.updateECEnrollment("organizationFake", "id-servizio-2", enrollment);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		try {
			// non-existent service -> must raise a 404 exception
			enrollmentsService.updateECEnrollment("organizationTest", "id-servizio-fake", enrollment);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test 
	void updateEC() {
		assertTrue(emulator.isRunning());
		OrganizationModel orgModel =  OrganizationModel.builder().companyName("Comune di Roma").status(Status.DISABLED).build();
		Organization org = enrollmentsService.updateEC("organizationTest", orgModel);
		assertEquals("organizationTest", org.getFiscalCode());
		assertEquals("Comune di Roma", org.getCompanyName());
		assertEquals(Status.DISABLED, org.getStatus());
	}
	
	@Test 
	void updateEC_404() {
		assertTrue(emulator.isRunning());
		OrganizationModel orgModel =  OrganizationModel.builder().companyName("Comune di Roma").status(Status.DISABLED).build();
		try {
			// non-existent organization -> must raise a 404 exception
			enrollmentsService.updateEC("organizationFake", orgModel);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void deleteECEnrollment() {
		assertTrue(emulator.isRunning());
		// Add a dummy enrollment 
		EnrollmentModel enrollment = 
				EnrollmentModel.builder().iban("iban-4").officeName("Ufficio Tributario 4").build();
		Organization org = enrollmentsService.createECEnrollment("organizationTest", "id-servizio-4", enrollment);
		assertEquals("organizationTest", org.getFiscalCode());
		assertEquals("Comune di Roma", org.getCompanyName());
		ServiceRef addedEnrollment = org.getEnrollments().stream().filter(s -> s.getServiceId().equals("id-servizio-4")).findFirst().get();
		assertEquals("iban-4", addedEnrollment.getIban());
		assertEquals("Ufficio Tributario 4", addedEnrollment.getOfficeName());
		
		// Remove the dummy enrollment
		enrollmentsService.deleteECEnrollment("organizationTest", "id-servizio-4");
		// This line means the call was successful
		assertTrue(true);
	}
	
	@Test
	void deleteECEnrollment_404() {
		assertTrue(emulator.isRunning());
		try {
			// non-existent organization -> must raise a 404 exception
			enrollmentsService.deleteECEnrollment("organizationFake", "id-servizio-4");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		try {
			// non-existent enrollment -> must raise a 404 exception
			enrollmentsService.deleteECEnrollment("organizationTest", "id-servizio-fake");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test 
	void deleteEC() {
		assertTrue(emulator.isRunning());
		// Creates a dummy organization
		Organization ci = new Organization();
		ci.setFiscalCode("organizationDummy");
		ci.setCompanyName("Comune di Napoli");
		ci.setStatus(Status.ENABLED);
		ServiceRef ref1 = new ServiceRef();
		ref1.setServiceId("id-servizio-1");
		ref1.setIban("iban-1");
		List<ServiceRef> servicesRef = new ArrayList<>();
		servicesRef.add(ref1);
		ci.setEnrollments(servicesRef);
		Organization orgCreated = enrollmentsService.createEC(ci);
		assertEquals("organizationDummy", orgCreated.getFiscalCode());
		assertEquals("Comune di Napoli", orgCreated.getCompanyName());
		assertEquals(1, orgCreated.getEnrollments().size());
		// Remove the dummy organization
		enrollmentsService.deleteEC("organizationDummy");
		// This line means the call was successful
		assertTrue(true);
	}
	
	@Test 
	void deleteEC_404() {
		assertTrue(emulator.isRunning());
		try {
			// non-existent organization -> must raise a 404 exception
			enrollmentsService.deleteEC("organizationFake");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

}
