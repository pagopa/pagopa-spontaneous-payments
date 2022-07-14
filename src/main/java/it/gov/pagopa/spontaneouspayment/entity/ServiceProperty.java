package it.gov.pagopa.spontaneouspayment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import it.gov.pagopa.spontaneouspayment.model.enumeration.PropertyType;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProperty {

    @NotBlank
    private String name;

    private PropertyType type;

    private boolean isRequired;
}
