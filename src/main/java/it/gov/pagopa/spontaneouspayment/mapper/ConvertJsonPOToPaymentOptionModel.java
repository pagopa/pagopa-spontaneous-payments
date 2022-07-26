package it.gov.pagopa.spontaneouspayment.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.validation.Valid;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;
import it.gov.pagopa.spontaneouspayment.model.response.TransferModel;

public class ConvertJsonPOToPaymentOptionModel implements Converter<JsonNode, PaymentOptionModel> {

    @Override
    public PaymentOptionModel convert(MappingContext<JsonNode, PaymentOptionModel> mappingContext) {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    	
    	ObjectMapper objMapper = new ObjectMapper();
    	
    	@Valid JsonNode poNode = mappingContext.getSource();
    	@Valid PaymentOptionModel po = mappingContext.getDestination();
    	
    	po.setAmount(poNode.get("amount").asLong());
    	po.setDescription(poNode.has("description")?poNode.get("description").asText():null);
    	po.setDueDate(LocalDateTime.parse(poNode.get("dueDate").asText(),formatter));
    	po.setIsPartialPayment(poNode.get("isPartialPayment").asBoolean());
    	po.setRetentionDate(LocalDateTime.parse(poNode.get("retentionDate").asText(),formatter));
    	
    	
    	TransferModel[] transfer = objMapper.convertValue(poNode.get("transfer"), TransferModel[].class);
    	
    	po.setTransfer(Arrays.asList(transfer));
    	
    	return po;
    }
}
