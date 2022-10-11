package it.gov.pagopa.spontaneouspayment.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Container(containerName = "${azure.cosmos.ec-container-name}", autoCreateContainer = false, ru="800")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {


    @Id
    @PartitionKey
    private String fiscalCode;

    @NotBlank(message = "company name is required")
    private String companyName;

    @CreatedDate
    private LocalDateTime insertedDate;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    private Status status;

    @Valid
    private List<ServiceRef> enrollments;

}
