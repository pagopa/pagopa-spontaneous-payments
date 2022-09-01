package it.gov.pagopa.spontaneouspayment.service;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;

import it.gov.pagopa.spontaneouspayment.config.TestUtil;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Testcontainers
class ServicesServiceTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Autowired
	private OrganizationRepository ciRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Container
	private static final CosmosDBEmulatorContainer emulator = new CosmosDBEmulatorContainer(
			DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));

	private static ServicesService servicesService;
	
    private Organization ci;
	
	private Service s1;
	private Service s2;
	private Service s3;
	private Service s4;
	private Service s5;

	@BeforeAll
	public void setUp() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		servicesService = spy(new ServicesService(serviceRepository));

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
		ci = new Organization();
		ci.setFiscalCode("organizationTest");
		ci.setCompanyName("Comune di Roma");
		ci.setStatus(Status.ENABLED);

		s1 = TestUtil.getMockServiceById("id-servizio-1");

		ServiceProperty sp1 = new ServiceProperty("propName1", PropertyType.STRING, true);
		List<ServiceProperty> properties1 = new ArrayList<>();
		properties1.add(sp1);
		s1.setProperties(properties1);

		s2 = TestUtil.getMockServiceById("id-servizio-2");

		ServiceProperty sp2 = new ServiceProperty("propName2", PropertyType.STRING, true);
		List<ServiceProperty> properties2 = new ArrayList<>();
		properties2.add(sp2);
		s2.setProperties(properties2);
		
		s3 = TestUtil.getMockServiceById("id-servizio-3");

		ServiceProperty sp3 = new ServiceProperty("propName3", PropertyType.STRING, true);
		List<ServiceProperty> properties3 = new ArrayList<>();
		properties3.add(sp3);
		s3.setProperties(properties3);
		
		s4 = TestUtil.getMockServiceById("id-servizio-4");

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

		
		ciRepository.save(ci);
		
		serviceRepository.save(s1);
		serviceRepository.save(s2);
		serviceRepository.save(s3);
		serviceRepository.save(s4);
	}
	
	@AfterAll
	void teardown() {
        ciRepository.delete(ci);
		
		serviceRepository.delete(s2);
		serviceRepository.delete(s3);
		serviceRepository.delete(s4);
		serviceRepository.delete(s5);
		
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
	void getServices() {
		assertTrue(emulator.isRunning());
		List<Service> services = servicesService.getServices();
		assertTrue("A value greater or equal than <3> was expected", services.size() >= 3);
	}
	
	@Test
	void getServiceDetails() {
		assertTrue(emulator.isRunning());
		Service s = servicesService.getServiceDetails("id-servizio-1");
		assertEquals("id-servizio-1", s.getId());
		assertEquals("tassonomia_id-servizio-1", s.getTransferCategory());
		assertEquals("base-path_id-servizio-1", s.getBasePath());
		assertEquals("endpont_id-servizio-1", s.getEndpoint());
	}
	
	@Test
	void getServiceDetails_404() {
		assertTrue(emulator.isRunning());
		try {
			// non-existent service -> must raise a 404 exception
			servicesService.getServiceDetails("id-servizio-fake");
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void createService() {
		assertTrue(emulator.isRunning());
		ServiceProperty sp5 = new ServiceProperty("propName5", PropertyType.STRING, true);
		s5 = TestUtil.getMockServiceById("id-servizio-5");
		s5.setProperties(List.of(sp5));
		Service s = servicesService.createService(s5);
		assertEquals("id-servizio-5", s.getId());
		assertEquals(1, s.getProperties().size());
	}
	
	@Test
	void createService_409() {
		assertTrue(emulator.isRunning());
		ServiceProperty sp2 = new ServiceProperty("propName2", PropertyType.STRING, true);
		Service s5 = TestUtil.getMockServiceById("id-servizio-2");
		s5.setProperties(List.of(sp2));
		AppException thrown = assertThrows(AppException.class,
	            ()->{
	            	servicesService.createService(s5);
	            });
		assertTrue(thrown.getMessage().contains("Already exists an entity with id id-servizio-2"));
	}
	
	@Test
	void updateService() {
		assertTrue(emulator.isRunning());
		Service s = servicesService.getServiceDetails("id-servizio-1");
		s.setDescription("description-1 UPD");
		Service updServ = servicesService.updateService(s);
		assertEquals("id-servizio-1", updServ.getId());
		assertEquals("description-1 UPD", updServ.getDescription());
	}
	
	@Test
	void updateService_404() {
		assertTrue(emulator.isRunning());
		ServiceProperty sp6 = new ServiceProperty("propName5", PropertyType.STRING, true);
		Service s6 = TestUtil.getMockServiceById("id-servizio-6");
		s6.setProperties(List.of(sp6));
		AppException thrown = assertThrows(AppException.class,
	            ()->{
	            	servicesService.updateService(s6);
	            });
		assertTrue(thrown.getMessage().contains("Not found a service configuration for Service Id id-servizio-6"));
	}
	
	@Test
	void deleteService() {
		assertTrue(emulator.isRunning());
		servicesService.deleteService("id-servizio-1");
		// This line means the call was successful
		assertTrue(true);
		AppException thrown = assertThrows(AppException.class,
	            ()->{
	            	servicesService.getServiceDetails("id-servizio-1");
	            });
		assertTrue(thrown.getMessage().contains("Not found a service configuration for Service Id id-servizio-1"));
	}
	
	@Test
	void deleteService_404() {
		assertTrue(emulator.isRunning());
		AppException thrown = assertThrows(AppException.class,
	            ()->{
	            	servicesService.deleteService("id-servizio-6");
	            });
		assertTrue(thrown.getMessage().contains("Not found a service configuration for Service Id id-servizio-6"));
	}
	
	
	
	
	
}
