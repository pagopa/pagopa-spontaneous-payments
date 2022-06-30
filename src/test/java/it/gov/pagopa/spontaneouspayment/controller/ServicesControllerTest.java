package it.gov.pagopa.spontaneouspayment.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import it.gov.pagopa.spontaneouspayment.config.TestUtil;
import it.gov.pagopa.spontaneouspayment.service.ServicesService;

@SpringBootTest
@AutoConfigureMockMvc
class ServicesControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private ServicesService servicesService;
	
	@BeforeEach
    void setUp() {
        when(servicesService.getServices()).thenReturn(TestUtil.getMockServices());
        when(servicesService.getServiceDetails(anyString())).thenReturn(TestUtil.getMockService());
    }
	
	@Test
    void getServices() throws Exception {
        String url = "/services";
        MvcResult result = mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockId1"));
        assertTrue(result.getResponse().getContentAsString().contains("mockId2"));
    }
	
	@Test
    void getServiceDetails() throws Exception {
        String url = "/services/mockService1";
        MvcResult result = mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockId"));
    }
}
