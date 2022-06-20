package it.gov.pagopa.spontaneouspayment.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import it.gov.pagopa.spontaneouspayment.model.enumeration.Status;
import lombok.AllArgsConstructor;
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
public class Service {

	@Id
	private String id;

	@NotBlank(message = "name is required")
	private String name;

	private String description;

	@NotBlank(message = "transfer category is required")
	@PartitionKey
	private String transferCategory; // tassonomia

	@NotBlank(message = "remittance information is required")
	private String remittanceInformation; // causale

	@CreatedDate
	private LocalDateTime insertedDate;

	@LastModifiedDate
	private LocalDateTime lastUpdatedDate;

	private Status status;

	private String endpoint;

	private String basePath;

	private List<ServiceProperty> properties;


}
