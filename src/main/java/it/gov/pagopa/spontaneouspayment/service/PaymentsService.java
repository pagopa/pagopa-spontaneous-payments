package it.gov.pagopa.spontaneouspayment.service;

import java.net.URI;
import java.time.Year;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.entity.ServiceProperty;
import it.gov.pagopa.spontaneouspayment.entity.ServiceRef;
import it.gov.pagopa.spontaneouspayment.exception.AppError;
import it.gov.pagopa.spontaneouspayment.exception.AppException;
import it.gov.pagopa.spontaneouspayment.mapper.ConvertJsonPOToPaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.IuvGenerationModel;
import it.gov.pagopa.spontaneouspayment.model.ServiceModel;
import it.gov.pagopa.spontaneouspayment.model.ServicePropertyModel;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.model.enumeration.PropertyType;
import it.gov.pagopa.spontaneouspayment.model.response.IuvGenerationModelResponse;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.response.PaymentPositionModel;
import it.gov.pagopa.spontaneouspayment.model.response.TransferModel;
import it.gov.pagopa.spontaneouspayment.repository.OrganizationRepository;
import it.gov.pagopa.spontaneouspayment.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PaymentsService {

    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private GpdClient gpdClient;
    
    @Autowired
    private IuvGeneratorClient iuvGeneratorClient;
    
    @Autowired
    private ExternalServiceClient extServiceClient;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${service.aux.digit:3}")
    private Long auxDigit; 
    
    private String iupdPrefix = "GPS" + Year.now().getValue() + "_"; 


    public PaymentPositionModel createSpontaneousPayment(@NotBlank String organizationFiscalCode, @Valid SpontaneousPaymentModel spontaneousPayment) {
        // check if credit institution exists
        var orgConfiguration = getCreditorInstitution(organizationFiscalCode);

        // check if service exists
        var serviceConfiguration = getServiceDetails(spontaneousPayment.getService());

        // check if the relationship between organization and enrollment to service exists
        checkServiceOrganization(organizationFiscalCode, serviceConfiguration);

        // checks if the request contains the properties required by the configured service
        checkServiceProperties(spontaneousPayment, serviceConfiguration);

        return createDebtPosition(organizationFiscalCode, orgConfiguration, serviceConfiguration, spontaneousPayment);
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
			if (confProp.isRequired()) {
				Predicate<ServicePropertyModel> checkName = o -> o.getName().equals(confProp.getName());
				Predicate<ServicePropertyModel> checkType = this.getCheckPropertyType(confProp);
				var isPresentAndValid = spontaneousPayment.getService().getProperties().parallelStream()
						.anyMatch(checkName.and(checkType));
				if (!isPresentAndValid) {
					throw new AppException(AppError.PROPERTY_MISSING_OR_WRONG, confProp.getName(), confProp.getType());
				}
			}
		}
	}

	private Predicate<ServicePropertyModel> getCheckPropertyType(ServiceProperty confProp) {
		if (confProp.getType().equals(PropertyType.NUMBER)) {
			return o -> NumberUtils.isCreatable (o.getValue());
		}
		// Default is always true condition (no check type)
		return o -> true;
	}

    private PaymentPositionModel createDebtPosition(String organizationFiscalCode, 
    		Organization orgConfiguration, it.gov.pagopa.spontaneouspayment.entity.Service serviceConfiguration, SpontaneousPaymentModel spontaneousPayment) {
    	
    	// get the enrollment for the service
    	ServiceRef enrollment = Optional.ofNullable(orgConfiguration.getEnrollments()).orElseGet(Collections::emptyList)
    	        .parallelStream()
    	        .filter(e -> e.getServiceId().equals(serviceConfiguration.getId()))
    	        .findAny()
    	        .orElseThrow(() -> new AppException(AppError.ENROLLMENT_TO_SERVICE_NOT_FOUND, serviceConfiguration.getId(), organizationFiscalCode));
    	
    	// call the external service to get the PO
    	PaymentOptionModel po = this.callExternalService(spontaneousPayment, serviceConfiguration);
        
    	// generate IUV
        String iuv = this.callIuvGeneratorService(organizationFiscalCode, enrollment);
        
        // integration the informations for the PO
        po.setIuv(iuv);
        TransferModel transfer = po.getTransfer().get(0);
        transfer.setIdTransfer("1");
        transfer.setRemittanceInformation(enrollment.getRemittanceInformation());
        transfer.setCategory(serviceConfiguration.getTransferCategory());
        transfer.setIban(enrollment.getIban());
        transfer.setPostalIban(enrollment.getPostalIban());
        
        // Payment Position to create
        PaymentPositionModel pp = modelMapper.map(spontaneousPayment.getDebtor(), PaymentPositionModel.class);
        pp.setIupd(iupdPrefix+iuv);
        pp.setCompanyName(orgConfiguration.getCompanyName());
        pp.addPaymentOptions(po);
        

        return gpdClient.createDebtPosition(organizationFiscalCode, pp);
    }
    

	private PaymentOptionModel callExternalService(SpontaneousPaymentModel spontaneousPayment,
			it.gov.pagopa.spontaneouspayment.entity.Service serviceConfiguration) {

		PaymentOptionModel po = null;

		try {

			JSONObject properties = new JSONObject();

			for (ServicePropertyModel sp : spontaneousPayment.getService().getProperties()) {
				properties.put(sp.getName(), sp.getValue());
			}

			URI runtimeBasePathUri = URI
					.create(serviceConfiguration.getBasePath() + serviceConfiguration.getEndpoint());
			String result = extServiceClient.getPaymentOption(runtimeBasePathUri,
					new JSONObject().put("properties", properties).toString());

			// The payment option has only one element
			JsonNode orderNode = new ObjectMapper().readTree(result).get("paymentOption").get(0);

			po = new PaymentOptionModel();
			
			modelMapper
			.getTypeMap(JsonNode.class,PaymentOptionModel.class)
			.setConverter(new ConvertJsonPOToPaymentOptionModel())
			.map(orderNode, po);
			
		} catch (JsonProcessingException e) {
			log.error("Error during spontaneous payment process", e);
			throw new AppException(AppError.INTERNAL_ERROR, serviceConfiguration.getId(),
					spontaneousPayment.getDebtor().getFiscalCode());
		}
		
		return po;
	}

    private String callIuvGeneratorService(String organizationFiscalCode, ServiceRef enrollment) {
    	
    	Long segregationCode = Long.parseLong(enrollment.getSegregationCode());
    	IuvGenerationModelResponse iuvObj = iuvGeneratorClient.generateIUV(organizationFiscalCode, 
    			IuvGenerationModel.builder().auxDigit(auxDigit).segregationCode(segregationCode).build());
    	
    	return Optional.ofNullable(iuvObj).orElseThrow(() -> new AppException(AppError.IUV_ACQUISITION_ERROR, organizationFiscalCode, auxDigit, segregationCode)).getIuv();
    }
    
    private void checkServiceOrganization(@NotBlank String organizationFiscalCode,
                                          @NotNull it.gov.pagopa.spontaneouspayment.entity.Service service) {
        var org = orgRepository.getCreditInstitutionByOrgFiscCodeAndServiceId(organizationFiscalCode, service.getId());
        if (org.isEmpty()) {
            throw new AppException(AppError.ORGANIZATION_SERVICE_NOT_FOUND, organizationFiscalCode, service.getId());
        }
    }

    private it.gov.pagopa.spontaneouspayment.entity.Service getServiceDetails(@NotNull ServiceModel service) {
        return serviceRepository.findById(service.getId())
                .orElseThrow(() -> new AppException(AppError.SERVICE_NOT_FOUND, service.getId()));
    }

    private Organization getCreditorInstitution(String organizationFiscalCode) {
        return orgRepository.findByFiscalCode(organizationFiscalCode)
                .orElseThrow(() -> new AppException(AppError.ORGANIZATION_NOT_FOUND, organizationFiscalCode));
    }
}
