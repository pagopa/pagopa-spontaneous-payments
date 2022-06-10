package it.gov.pagopa.spontaneouspayment.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import com.azure.spring.data.cosmos.core.mapping.Container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Container(containerName = "services", ru = "400")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

	@Id
	private String id;
	private String name;
	private String description;
	@NotBlank(message = "transfer category is required")
	private String transferCategory; // tassonomia
	@NotBlank(message = "remittance information is required")
	private String remittanceInformation; // causale
	
	@CreatedDate
    private LocalDateTime insertedDate;
	@LastModifiedDate
    private LocalDateTime lastUpdatedDate;
	private String status;
	
	private String endpoint;
	private String basePath;
	
	private List<ServiceProperty> properties;

	
}
