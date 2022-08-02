package it.gov.pagopa.spontaneouspayment.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.model.*;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Type;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.model.response.TransferModel;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class TestUtil {

	public static <T> T readModelFromFile(String relativePath, Class<T> clazz) throws IOException {
		ClassLoader classLoader = TestUtil.class.getClassLoader();
		File file = new File(Objects.requireNonNull(classLoader.getResource(relativePath)).getPath());
		var content = Files.readString(file.toPath());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper.readValue(content, clazz);
	}

	/**
	 * @param object to map into the Json string
	 * @return object as Json string
	 * @throws JsonProcessingException if there is an error during the parsing of
	 *                                 the object
	 */
	public String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	public static Organization getMockOrganization() {

		List<ServiceRef> enrollments = new ArrayList<>();

		enrollments.add(ServiceRef.builder().serviceId("mockServiceId1").iban("mockIban1").officeName("mockOfficeName1").segregationCode("47").remittanceInformation("mockRemittanceInformation1")
				.build());
		enrollments.add(ServiceRef.builder().serviceId("mockServiceId2").iban("mockIban2").officeName("mockOfficeName2").segregationCode("47").remittanceInformation("mockRemittanceInformation2")
				.build());

		return Organization.builder().companyName("mockCompanyName").fiscalCode("mockFiscalCode").status(Status.ENABLED)
				.enrollments(enrollments).build();
	}

	public static OrganizationEnrollmentModel getMockOrganizationEnrollmentModel() {

		List<CreateEnrollmentModel> enrollments = new ArrayList<>();

		enrollments.add(CreateEnrollmentModel.builder().serviceId("mockServiceId1").iban("mockIban1")
				.officeName("mockOfficeName1").build());
		enrollments.add(CreateEnrollmentModel.builder().serviceId("mockServiceId2").iban("mockIban2")
				.officeName("mockOfficeName2").build());

		return OrganizationEnrollmentModel.builder().companyName("mockCompanyName").enrollments(enrollments).build();
	}

	public static OrganizationEnrollmentModel getMockOrganizationEnrollmentModel_NoRequiredField() {

		List<CreateEnrollmentModel> enrollments = new ArrayList<>();

		enrollments.add(CreateEnrollmentModel.builder().serviceId("mockServiceId1").iban("mockIban1")
				.officeName("mockOfficeName1").build());
		enrollments.add(CreateEnrollmentModel.builder().serviceId("mockServiceId2").iban("mockIban2")
				.officeName("mockOfficeName2").build());

		// No required Field company name
		return OrganizationEnrollmentModel.builder()
				.enrollments(enrollments).build();
	}

	public static EnrollmentModel getMockEnrollmentModel() {
		return EnrollmentModel.builder()
				.iban("mockIban")
				.officeName("mockOfficeName")
				.segregationCode("47")
				.remittanceInformation("mockRemittanceInformation")
				.build();
	}

	public static ServiceRef getMockServiceRef() {
		return ServiceRef.builder().serviceId("mockServiceId1").iban("mockIban1").officeName("mockOfficeName1").build();
	}

	public static OrganizationModel getMockOrganizationModel() {
		return OrganizationModel.builder().companyName("mockCompanyName").status(Status.ENABLED).build();
	}

	public static PaymentPositionModel getPaymentPositionModel() {
		TransferModel t = new TransferModel();
		t.setIdTransfer("1");
		t.setAmount(100);
		t.setRemittanceInformation("mockRemittanceInformation");
		t.setCategory("mockCategory");
		t.setIban("mockIban");


		PaymentOptionModel pom = new PaymentOptionModel();
		pom.setIuv("mockIuv");
		pom.setAmount(100);
		pom.setIsPartialPayment(false);
		pom.setDueDate(LocalDateTime.now());
		pom.addTransfers(t);

		List<PaymentOptionModel> poList = new ArrayList<>();
		poList.add(pom);

		return PaymentPositionModel.builder()
				.iupd("mockIupd")
				.type(Type.F)
				.fiscalCode("mockFiscalCode")
				.fullName("mockFullName")
				.email("mockEmail@mock.it")
				.companyName("mockCompanyName")
				.paymentOption(poList)
				.build();
	}

	public static SpontaneousPaymentModel getSpontaneousPaymentModel() {

		List<ServicePropertyModel> props = new ArrayList<>();
		props.add(ServicePropertyModel.builder().name("propName1").value("string value").build());

		return SpontaneousPaymentModel.builder()
				.debtor(DebtorModel.builder()
						.type(Type.F)
						.fiscalCode("mockFiscalCode")
						.fullName("mockFullName")
						.email("mockEmail@mock.it").build())
				.service(ServiceModel.builder()
						.id("id-servizio-1")
						.properties(props)
						.build())
				.build();
	}
	
	public static SpontaneousPaymentModel getSpontaneousPaymentModel_FakeServiceId() {

		List<ServicePropertyModel> props = new ArrayList<>();
		props.add(ServicePropertyModel.builder().name("mockName").value("mockValue").build());

		return SpontaneousPaymentModel.builder()
				.debtor(DebtorModel.builder()
						.type(Type.F)
						.fiscalCode("mockFiscalCode")
						.fullName("mockFullName")
						.email("mockEmail@mock.it").build())
				.service(ServiceModel.builder()
						.id("id-servizio-fake")
						.properties(props)
						.build())
				.build();
	}
	
	public static SpontaneousPaymentModel getSpontaneousPaymentModel_NotEnrolledService() {

		List<ServicePropertyModel> props = new ArrayList<>();
		props.add(ServicePropertyModel.builder().name("mockName").value("mockValue").build());

		return SpontaneousPaymentModel.builder()
				.debtor(DebtorModel.builder()
						.type(Type.F)
						.fiscalCode("mockFiscalCode")
						.fullName("mockFullName")
						.email("mockEmail@mock.it").build())
				.service(ServiceModel.builder()
						.id("id-servizio-4")
						.properties(props)
						.build())
				.build();
	}
	
	public static SpontaneousPaymentModel getSpontaneousPaymentModel_BadProperty() {

		List<ServicePropertyModel> props = new ArrayList<>();
		props.add(ServicePropertyModel.builder().name("mockName").value("mockValue").build());

		return SpontaneousPaymentModel.builder()
				.debtor(DebtorModel.builder()
						.type(Type.F)
						.fiscalCode("mockFiscalCode")
						.fullName("mockFullName")
						.email("mockEmail@mock.it").build())
				.service(ServiceModel.builder()
						.id("id-servizio-1")
						.properties(props)
						.build())
				.build();
	}
	
	public static SpontaneousPaymentModel getSpontaneousPaymentModel_BadPropertyType() {

		List<ServicePropertyModel> props = new ArrayList<>();
		// the type of the property propName2 must be a number but it is a string
		props.add(ServicePropertyModel.builder().name("propName2").value("string value").build());

		return SpontaneousPaymentModel.builder()
				.debtor(DebtorModel.builder()
						.type(Type.F)
						.fiscalCode("mockFiscalCode")
						.fullName("mockFullName")
						.email("mockEmail@mock.it").build())
				.service(ServiceModel.builder()
						.id("id-servizio-1")
						.properties(props)
						.build())
				.build();
	}
	
	public static List<Service> getMockServices() {
		List<Service> services = new ArrayList<>();
		services.add(Service.builder().id("mockId1").name("mockName1").transferCategory("mockTransferCategory1").build());
		services.add(Service.builder().id("mockId2").name("mockName2").transferCategory("mockTransferCategory2").build());

		return  services;
	}
	
	public static Service getMockService() {
		return Service.builder().id("mockId").name("mockName").transferCategory("mockTransferCategory").build();
	}

	public static PaymentOptionModel getMockPaymentOptionModel(){
		return PaymentOptionModel.builder()
				.amount(100)
				.description("string")
				.dueDate(LocalDateTime.of(2022, 8, 10, 16 , 47))
				.retentionDate(LocalDateTime.of(2022, 8, 10, 16 , 47))
				.isPartialPayment(false)
				.transfer(List.of(TransferModel.builder()
						.amount(100)
						.build()))
				.build();
	}
	
}
