package it.gov.pagopa.spontaneouspayment.model;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationModel implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -1181181033127626459L;

    private String companyName;
    private Status status;
}
