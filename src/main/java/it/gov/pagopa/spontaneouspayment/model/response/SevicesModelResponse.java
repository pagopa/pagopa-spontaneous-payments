package it.gov.pagopa.spontaneouspayment.model.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SevicesModelResponse implements Serializable{
	
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -3078022971085103082L;
	private List <ServiceModelResponse> services;
}
