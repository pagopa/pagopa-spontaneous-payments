package it.gov.pagopa.spontaneouspayment.model.response;

import java.io.Serializable;
import java.util.List;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailModelResponse implements Serializable {
    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -3792257418379600493L;

    private String id;
    private String name;
    private String description;

    private String transferCategory; // tassonomia

    private String endpoint;
    
    private String basePath;

    private Status status;

    private List<ServicePropertyModelResponse> properties;

}
