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
import it.gov.pagopa.spontaneouspayment.model.IuvGenerationModel;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.PropertyType;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.model.response.IuvGenerationModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionsModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import it.gov.pagopa.spontaneouspayment.service.client.ExternalServiceClient;
import it.gov.pagopa.spontaneouspayment.service.client.GpdClient;
import it.gov.pagopa.spontaneouspayment.service.client.IuvGeneratorClient;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import static it.gov.pagopa.spontaneouspayment.config.TestUtil.getMockPaymentOptionModel;
import static it.gov.pagopa.spontaneouspayment.config.TestUtil.readModelFromFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Testcontainers
class PaymentsServiceTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Autowired
	private OrganizationRepository ciRepository;

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
    private ModelMapper modelMapper;

	@Mock
	private GpdClient gpdClient;

	@Mock
	private IuvGeneratorClient iuvGeneratorClient;
	
	@Mock
	private ExternalServiceClient extServiceClient;

	@Container
	private static final CosmosDBEmulatorContainer emulator = new CosmosDBEmulatorContainer(
			DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));

	private static PaymentsService paymentsService;

	@BeforeAll
	public void setUp() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		paymentsService = 
				spy(new PaymentsService(ciRepository, serviceRepository, gpdClient, iuvGeneratorClient, extServiceClient, modelMapper, 3L, "IUVTEST_"));

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
		containerResponse = client.getDatabase("db").createContainerIfNotExists("services", "/transferCategory")
				.block();
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

		ServiceProperty sp2 = new ServiceProperty("propName2", PropertyType.NUMBER, true);
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
		ref1.setSegregationCode("47");
		ref1.setRemittanceInformation("causale-1");
		ServiceRef ref2 = new ServiceRef();
		ref2.setServiceId("id-servizio-2");
		ref2.setIban("iban-2");
		ref2.setSegregationCode("5");
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
	void createSpontaneousPayment() throws IOException {
		assertTrue(emulator.isRunning());

		// precondition
		PaymentPositionModel paymentModel = readModelFromFile("gpd/getPaymentPosition.json",
				PaymentPositionModel.class);
		
		
		when(iuvGeneratorClient.generateIUV(anyString(), any(IuvGenerationModel.class)))
				.thenReturn(IuvGenerationModelResponse.builder().iuv("12345678901234567").build());
		
		when(gpdClient.createDebtPosition(anyString(), any(PaymentPositionModel.class))).thenReturn(paymentModel);

		ArrayList<PaymentOptionModel> paymentOption = new ArrayList<>();
		paymentOption.add(getMockPaymentOptionModel());
		when(extServiceClient.getPaymentOption(any(URI.class), anyString())).thenReturn(PaymentOptionsModel.builder()
						.paymentOption(paymentOption)
				.build());

		PaymentPositionModel ppm = paymentsService.createSpontaneousPayment("organizationTest",
				TestUtil.getSpontaneousPaymentModel());
		assertEquals("JHNDOE00A01F205N", ppm.getFiscalCode());
		assertEquals("John Doe", ppm.getFullName());
	}

	@Test
	void createSpontaneousPayment_404() {
		assertTrue(emulator.isRunning());
		SpontaneousPaymentModel spontaneousPaymentModel = TestUtil.getSpontaneousPaymentModel();
		try {
			// non-existent organization -> must raise a 404 exception
			paymentsService.createSpontaneousPayment("organizationFake", spontaneousPaymentModel);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}

		SpontaneousPaymentModel spontaneousPaymentModel_fakeServiceId = TestUtil
				.getSpontaneousPaymentModel_FakeServiceId();
		try {
			// non-existent service -> must raise a 404 exception
			paymentsService.createSpontaneousPayment("organizationTest", spontaneousPaymentModel_fakeServiceId);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}

		SpontaneousPaymentModel spontaneousPaymentModel_notEnrolledService = TestUtil
				.getSpontaneousPaymentModel_NotEnrolledService();
		try {
			// non-existent enrollment between organization ad service -> must raise a 404
			// exception
			paymentsService.createSpontaneousPayment("organizationTest", spontaneousPaymentModel_notEnrolledService);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	void createSpontaneousPayment_400() {
		assertTrue(emulator.isRunning());
		SpontaneousPaymentModel spontaneousPaymentModel_badProperty = TestUtil.getSpontaneousPaymentModel_BadProperty();
		try {
			// non-existent in request a configured property in service -> must raise a 400
			// exception
			paymentsService.createSpontaneousPayment("organizationTest", spontaneousPaymentModel_badProperty);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
		
		spontaneousPaymentModel_badProperty = TestUtil.getSpontaneousPaymentModel_BadPropertyType();
		try {
			// the request contains a string value but a number was expected -> must raise a 400
			// exception
			paymentsService.createSpontaneousPayment("organizationTest", spontaneousPaymentModel_badProperty);
			fail();
		} catch (AppException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
		} catch (Exception e) {
			fail();
		}
	}

}
