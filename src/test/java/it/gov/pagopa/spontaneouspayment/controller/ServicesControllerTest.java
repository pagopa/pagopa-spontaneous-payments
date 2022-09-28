package it.gov.pagopa.spontaneouspayment.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import it.gov.pagopa.spontaneouspayment.config.TestUtil;
import it.gov.pagopa.spontaneouspayment.entity.Service;
import it.gov.pagopa.spontaneouspayment.initializer.Initializer;
import it.gov.pagopa.spontaneouspayment.service.ServicesService;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {Initializer.class})
class ServicesControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private ServicesService servicesService;
	
	@BeforeEach
    void setUp() {
        when(servicesService.getServices()).thenReturn(TestUtil.getMockServices());
        when(servicesService.getServiceDetails(anyString())).thenReturn(TestUtil.getMockService());
        when(servicesService.createService(any(Service.class))).thenReturn(TestUtil.getMockService());
        when(servicesService.updateService(any(Service.class))).thenReturn(TestUtil.getMockService());
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
	
	@Test
    void createService() throws Exception {
		String url = "/services";
        MvcResult result = mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockServiceDetailModel())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockId"));
    }
	
	@Test
    void createService_400() throws Exception {
		String url = "/services";
		mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockServiceDetailModel_NoRequiredField())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
	
	@Test
    void updateService() throws Exception {
		String url = "/services/mockService1";
        MvcResult result = mvc.perform(put(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockServiceDetailUpdModel())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockId"));
    }
	
	@Test
    void updateService_400() throws Exception {
		String url = "/services/mockService1";
		mvc.perform(put(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockServiceDetailUpdModel_NoRequiredField())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
	
	@Test
    void deleteService() throws Exception {
        String url = "/services/mockService1";
        MvcResult result = mvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("was successfully removed"));
    }
}
