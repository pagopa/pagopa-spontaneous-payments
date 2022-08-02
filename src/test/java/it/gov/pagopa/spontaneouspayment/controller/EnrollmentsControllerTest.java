package it.gov.pagopa.spontaneouspayment.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import it.gov.pagopa.spontaneouspayment.config.TestUtil;
import it.gov.pagopa.spontaneouspayment.entity.Organization;
import it.gov.pagopa.spontaneouspayment.model.EnrollmentModel;
import it.gov.pagopa.spontaneouspayment.model.OrganizationModel;
import it.gov.pagopa.spontaneouspayment.service.EnrollmentsService;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentsControllerTest {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private EnrollmentsService enrollmentsService;
	
	@BeforeEach
    void setUp() {
        when(enrollmentsService.getECEnrollments(anyString())).thenReturn(TestUtil.getMockOrganization());
        when(enrollmentsService.getSingleEnrollment(anyString(), anyString())).thenReturn(TestUtil.getMockServiceRef());
        when(enrollmentsService.createEC(any(Organization.class))).thenReturn(TestUtil.getMockOrganization());
        when(enrollmentsService.createECEnrollment(anyString(), anyString(), any(EnrollmentModel.class))).thenReturn(TestUtil.getMockOrganization());
        when(enrollmentsService.updateECEnrollment(anyString(), anyString(), any(EnrollmentModel.class))).thenReturn(TestUtil.getMockOrganization());
        when(enrollmentsService.updateEC(anyString(), any(OrganizationModel.class))).thenReturn(TestUtil.getMockOrganization());
    }
	
	@Test
    void getECEnrollments() throws Exception {
        String url = "/organizations/mockOrganization1";
        MvcResult result = mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockFiscalCode"));
    }
	
	@Test
    void getSingleEnrollment() throws Exception {
        String url = "/organizations/mockOrganization1/services/mockService1";
        MvcResult result = mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockServiceId1"));
        assertFalse(result.getResponse().getContentAsString().contains("mockFiscalCode"));
    }
	
	@Test
    void createEC() throws Exception {
        String url = "/organizations/mockOrganization1";
        MvcResult result = mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockOrganizationEnrollmentModel())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockFiscalCode"));
    }
	
	@Test
    void createEC_400() throws Exception {
        String url = "/organizations/mockOrganization1";
        mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockOrganizationEnrollmentModel_NoRequiredField())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
    }
	
	@Test
	void createECEnrollment() throws Exception {
        String url = "/organizations/mockOrganization1/services/mockService1";
        MvcResult result = mvc.perform(post(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockEnrollmentModel())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockFiscalCode"));
        assertTrue(result.getResponse().getContentAsString().contains("47"));
    }
	
	@Test
	void updateECEnrollment() throws Exception {
        String url = "/organizations/mockOrganization1/services/mockService1";
        MvcResult result = mvc.perform(put(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockEnrollmentModel())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockFiscalCode"));
    }
	
	@Test
    void updateEC() throws Exception {
        String url = "/organizations/mockOrganization1";
        MvcResult result = mvc.perform(put(url)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(TestUtil.toJson(TestUtil.getMockOrganizationModel())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("mockFiscalCode"));
    }
	
	@Test
    void deleteECEnrollment() throws Exception {
        String url = "/organizations/mockOrganization1/services/mockService1";
        MvcResult result = mvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("was successfully removed"));
    }
}
