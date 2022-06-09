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

@Container(containerName = "credit_institution", ru = "400")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditInstitution {
	
		//@Id
		//@GeneratedValue
	    //private String id;
	    @Id
	    //@PartitionKey
	    private String organizationFiscalCode;
	    @NotBlank(message = "company name is required")
	    private String companyName;
	    @CreatedDate
	    private LocalDateTime insertedDate;
	    @LastModifiedDate
	    private LocalDateTime lastUpdatedDate;
	    private String status;
	    
	    private List<ServiceRef> services;
  
}
