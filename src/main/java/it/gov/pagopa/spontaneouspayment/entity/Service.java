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

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Container(containerName = "${azure.cosmos.service-container-name}", autoCreateContainer = false)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotBlank(message = "transfer category is required")
    @PartitionKey
    private String transferCategory; // tassonomia
    
    @NotBlank
    private String endpoint;
    
    @NotBlank
    private String basePath;
    
    @Builder.Default
    private Status status = Status.ENABLED;

    @CreatedDate
    private LocalDateTime insertedDate;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    private List<ServiceProperty> properties;


}
