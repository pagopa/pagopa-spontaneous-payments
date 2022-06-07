package it.gov.pagopa.spontaneouspayment.payments.service;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gov.pagopa.spontaneouspayment.entity.CreditInstitution;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.payments.model.ServiceModel;
import it.gov.pagopa.spontaneouspayment.repository.SpontaneousPaymentCIRepository;

@Service
public class PaymentsService {

	@Autowired
	private SpontaneousPaymentCIRepository ciRepository;

	public void getServiceDetails(@NotBlank String organizationFiscalCode, @NotNull ServiceModel service) {

		List<CreditInstitution> ec = ciRepository.getCreditInstitutionByOrgFiscCodeAndServiceId(organizationFiscalCode, service.getId());
		
		if (null == ec || ec.isEmpty()) {
			throw new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode, service.getId());
		}

	}

}
