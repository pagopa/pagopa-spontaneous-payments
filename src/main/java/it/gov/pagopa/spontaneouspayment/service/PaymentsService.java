package it.gov.pagopa.spontaneouspayment.service;

import it.gov.pagopa.spontaneouspayment.entity.CreditInstitution;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.model.ServiceModel;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.repository.CIRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Service
public class PaymentsService {

    @Autowired
    private CIRepository ciRepository;

    @Autowired
    private ServiceRepository serviceRepository;


    public PaymentPositionModel createSpontaneousPayment(@NotBlank String organizationFiscalCode, @Valid SpontaneousPaymentModel spontaneousPayment) {
        // check if credit institution exists
        getCreditorInstitution(organizationFiscalCode);

        // check if service exists
        var serviceConfiguration = getServiceDetails(spontaneousPayment.getService());

        // check if organizationFiscalCode exists
        checkServiceOrganization(organizationFiscalCode, serviceConfiguration);

        // check if the services exist in the DB
        checkServiceProperties(spontaneousPayment, serviceConfiguration);

        return createDebtPosition(serviceConfiguration);
    }


    /**
     * check if the service configuration is valid
     *
     * @param spontaneousPayment   the spontaneousPayment to check
     * @param serviceConfiguration the serviceConfiguration in the DB
     * @throws AppException if a service configuration is not present in the DB
     */
    private void checkServiceProperties(SpontaneousPaymentModel spontaneousPayment, it.gov.pagopa.spontaneouspayment.entity.Service serviceConfiguration) {
        for (ServiceProperty confProp : serviceConfiguration.getProperties()) {
            var isPresent = spontaneousPayment.getService().getProperties()
                    .parallelStream()
                    .anyMatch(o -> o.getName().equals(confProp.getName()));
            if (!isPresent) {
                throw new AppException(AppError.PROPERTY_NOT_FOUND, confProp.getName());
            }
        }
    }

    private PaymentPositionModel createDebtPosition(it.gov.pagopa.spontaneouspayment.entity.Service serviceConfiguration) {

        this.callExternalService(serviceConfiguration);

        // TODO: finire di implementare la logica
        return new PaymentPositionModel();

    }

    private void callExternalService(it.gov.pagopa.spontaneouspayment.entity.Service serviceConfiguration) {
        // TODO
    }

    private void checkServiceOrganization(@NotBlank String organizationFiscalCode,
                                          @NotNull it.gov.pagopa.spontaneouspayment.entity.Service service) {
        var ci = ciRepository.getCreditInstitutionByOrgFiscCodeAndServiceId(organizationFiscalCode, service.getId());
        if (ci.isEmpty()) {
            throw new AppException(AppError.ORGANIZATION_SERVICE_NOT_FOUND, organizationFiscalCode, service.getId());
        }
    }

    private it.gov.pagopa.spontaneouspayment.entity.Service getServiceDetails(@NotNull ServiceModel service) {
        return serviceRepository.findById(service.getId())
                .orElseThrow(() -> new AppException(AppError.SERVICE_NOT_FOUND, service.getId()));
    }

    private CreditInstitution getCreditorInstitution(String organizationFiscalCode) {
        return ciRepository.findByOrganizationFiscalCode(organizationFiscalCode)
                .orElseThrow(() -> new AppException(AppError.ORGANIZATION_SERVICE_NOT_FOUND, organizationFiscalCode));
    }


}
