package it.gov.pagopa.spontaneouspayment.model;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailUpdModel implements Serializable {
   
	/**
     * generated serialVersionUID
     */
	private static final long serialVersionUID = 7304374105767702339L;

    @NotBlank(message = "name is required")
    private String name;
    
    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "transfer category is required")
    private String transferCategory; // tassonomia

    @NotBlank(message = "endpoint is required")
    private String endpoint;
    
    @NotBlank@NotBlank(message = "base path is required")
    private String basePath;

    @NotNull(message = "status is required")
    private Status status;

    @Valid
    private List<ServiceConfigPropertyModel> properties;

}
