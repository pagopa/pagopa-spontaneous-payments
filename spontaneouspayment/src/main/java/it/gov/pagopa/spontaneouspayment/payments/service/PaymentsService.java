package it.gov.pagopa.spontaneouspayment.payments.service;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import it.gov.pagopa.spontaneouspayment.entity.CreditInstitution;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.payments.model.ServiceModel;
import it.gov.pagopa.spontaneouspayment.repository.SpontaneousPaymentCIRepository;
import it.gov.pagopa.spontaneouspayment.repository.SpontaneousPaymentServiceRepository;

@Service
public class PaymentsService {

	@Autowired
	private SpontaneousPaymentCIRepository ciRepository;
	
	@Autowired
	private SpontaneousPaymentServiceRepository serviceRepository;

	public CreditInstitution getCreditInstitutionDetails(@NotBlank String organizationFiscalCode, @NotNull ServiceModel service) {

		List<CreditInstitution> ec = ciRepository.getCreditInstitutionByOrgFiscCodeAndServiceId(organizationFiscalCode, service.getId());
		
		if (null == ec || ec.isEmpty()) {
			throw new AppException(AppError.ORGANIZATION_SERVICE_NOT_FOUND, organizationFiscalCode, service.getId());
		}
		
		// the list consists of only one element
		return ec.get(0);

	}
	
	public it.gov.pagopa.spontaneouspayment.entity.Service getServiceDetails(@NotNull ServiceModel service) {

		Optional<it.gov.pagopa.spontaneouspayment.entity.Service> serviceConfiguration = serviceRepository.findById(service.getId());
		
		if (serviceConfiguration.isEmpty()) {
			throw new AppException(AppError.SERVICE_NOT_FOUND, service.getId());
		}
		
		return serviceConfiguration.get();

	}

}
