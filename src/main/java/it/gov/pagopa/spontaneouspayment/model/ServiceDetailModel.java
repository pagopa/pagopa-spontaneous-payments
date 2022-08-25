package it.gov.pagopa.spontaneouspayment.model;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailModel implements Serializable {
	/**
     * generated serialVersionUID
     */
	private static final long serialVersionUID = 1076139345635868265L;

    @NotBlank(message = "id is required")
    private String id;
    
    @NotBlank(message = "name is required")
    private String name;
    
    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "transfer category is required")
    private String transferCategory; // tassonomia

    @NotBlank(message = "endpoint is required")
    private String endpoint;
    
    @NotBlank(message = "base path is required")
    private String basePath;

    @NotNull(message = "status is required")
    private Status status;

    @Valid
    private List<ServiceConfigPropertyModel> properties;

}
