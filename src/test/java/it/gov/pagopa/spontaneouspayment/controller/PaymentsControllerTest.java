package it.gov.pagopa.spontaneouspayment.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.gov.pagopa.spontaneouspayment.config.TestUtil;
import it.gov.pagopa.spontaneouspayment.model.SpontaneousPaymentModel;
import it.gov.pagopa.spontaneouspayment.service.PaymentsService;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentsControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private PaymentsService paymentsService;

	@BeforeEach
    void setUp() {
		when(paymentsService.createSpontaneousPayment(anyString(), any(SpontaneousPaymentModel.class))).thenReturn(TestUtil.getPaymentPositionModel());
	}
	
	@Test
	void createSpontaneousPayment() throws JsonProcessingException, Exception {
		String url = "/organizations/mockOrganization1/spontaneouspayments";
        MvcResult result = mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getSpontaneousPaymentModel())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockIupd"));
	}
}
