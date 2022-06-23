package it.gov.pagopa.spontaneouspayment;

import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.junit.jupiter.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;

@Testcontainers
class CosmosDBContainerTest {

	@Rule
	public TemporaryFolder tempFolder= new TemporaryFolder();


	@Container
	private static final CosmosDBEmulatorContainer emulator = new CosmosDBEmulatorContainer(
			DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest"));



	@BeforeEach
	public void setUp() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		tempFolder.create();
		Path keyStoreFile = tempFolder.newFile("azure-cosmos-emulator.keystore").toPath();
		KeyStore keyStore = emulator.buildNewKeyStore();
		keyStore.store(new FileOutputStream(keyStoreFile.toFile()), emulator.getEmulatorKey().toCharArray());

		System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
		System.setProperty("javax.net.ssl.trustStorePassword", emulator.getEmulatorKey());
		System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
	}

	@Test
	void cosmosDBEmulatorCheck()  {

		assertTrue(emulator.isRunning());



		CosmosAsyncClient client = new CosmosClientBuilder()
				.gatewayMode()
				.endpointDiscoveryEnabled(false)
				.endpoint(emulator.getEmulatorEndpoint())
				.key(emulator.getEmulatorKey())
				.buildAsyncClient();

		CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists("Azure").block();

		Assertions.assertEquals(201, databaseResponse.getStatusCode());
		CosmosContainerResponse containerResponse = client
				.getDatabase("Azure")
				.createContainerIfNotExists("ServiceContainer", "/name")
				.block();
		Assertions.assertEquals(201, containerResponse.getStatusCode());
	}
}
