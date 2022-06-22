package it.gov.pagopa.spontaneouspayment.config;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.model.CreateEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
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

}
