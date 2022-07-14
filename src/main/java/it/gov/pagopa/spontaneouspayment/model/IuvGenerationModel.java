package it.gov.pagopa.spontaneouspayment.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IuvGenerationModel {
	@NotNull
	private Long segregationCode;
	@NotNull
	private Long auxDigit;
}
