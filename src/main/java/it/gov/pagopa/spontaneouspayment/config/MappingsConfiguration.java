package it.gov.pagopa.spontaneouspayment.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;

import it.gov.pagopa.spontaneouspayment.model.response.PaymentOptionModel;

@Configuration
public class MappingsConfiguration {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.createTypeMap(JsonNode.class, PaymentOptionModel.class);
        return mapper;
    }

}
