package it.gov.pagopa.spontaneouspayment.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.model.CreateEnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;

public class ConvertOrganizationModelToOrganizationEntity
		implements Converter<OrganizationEnrollmentModel, Organization> {

	@Override
	public Organization convert(MappingContext<OrganizationEnrollmentModel, Organization> context) {
		OrganizationEnrollmentModel source = context.getSource();
		List<ServiceRef> enrollments = new ArrayList<>();
		for (CreateEnrollmentModel cem: source.getEnrollments()) {
			ServiceRef sr = new ServiceRef();
			sr.setServiceId(cem.getServiceId());
			sr.setIban(cem.getIban());
			sr.setOfficeName(cem.getOfficeName());
			enrollments.add(sr);
		}
		return Organization.builder().companyName(source.getCompanyName()).enrollments(enrollments).build();
	}

}
