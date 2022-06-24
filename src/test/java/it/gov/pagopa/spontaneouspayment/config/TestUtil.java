package it.gov.pagopa.spontaneouspayment.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.model.CreateEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.DebtorModel;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.ServiceModel;
import it.gov.pagopa.spontaneouspayment.model.ServicePropertyModel;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Type;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.model.response.TransferModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {

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

		enrollments.add(ServiceRef.builder().serviceId("mockServiceId1").iban("mockIban1").officeName("mockOfficeName1")
				.build());
		enrollments.add(ServiceRef.builder().serviceId("mockServiceId2").iban("mockIban2").officeName("mockOfficeName2")
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
		props.add(ServicePropertyModel.builder().name("propName1").value("1000").build());

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
	
}
