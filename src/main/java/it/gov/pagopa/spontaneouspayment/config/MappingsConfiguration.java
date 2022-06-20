package it.gov.pagopa.spontaneouspayment.config;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.mapper.ConvertOrganizationModelToOrganizationEntity;
import it.gov.pagopa.spontaneouspayment.model.OrganizationEnrollmentModel;

@Configuration
public class MappingsConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        Converter<OrganizationEnrollmentModel, Organization> convertOrganizationModelToOrganizationEntity = new ConvertOrganizationModelToOrganizationEntity();
        mapper.createTypeMap(OrganizationEnrollmentModel.class, Organization.class).setConverter(convertOrganizationModelToOrganizationEntity);
           
        return mapper;
    }

}
