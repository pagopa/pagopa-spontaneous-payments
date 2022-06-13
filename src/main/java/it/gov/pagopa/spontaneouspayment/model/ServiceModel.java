package it.gov.pagopa.spontaneouspayment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ServiceModel implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = 3545685764645620201L;

    @NotBlank(message = "service id is required")
    private String id;

    @Valid
    @NotNull
    private List<ServicePropertyModel> properties;

}
