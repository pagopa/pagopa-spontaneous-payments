package it.gov.pagopa.spontaneouspayment.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import it.gov.pagopa.spontaneouspayment.model.enumeration.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfigPropertyModel implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = 7156317962327532355L;

    @NotBlank(message = "name is required")
    private String name;

    private PropertyType type;

    private boolean isRequired = true;
}
