package com.abc.onboarding.customer;

import com.abc.onboarding.payload.request.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.abc.onboarding.helper.TestHelper.getRegistrationRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateCustomer_Success() throws Exception {
        RegistrationRequest request = getRegistrationRequest();
        mapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                                              .contentType("application/json")
                                              .content(mapper.writeValueAsBytes(request)))
               .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testLogon_Success() throws Exception {
        RegistrationRequest request = getRegistrationRequest();
        request.setUsername("username1");
        mapper.registerModule(new JavaTimeModule());

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                                                           .contentType("application/json")
                                                           .content(mapper.writeValueAsBytes(request)))
                            .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/logon")
                                              .contentType("application/json")
                                              .content(result.getResponse().getContentAsString()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetUserOverview_Success() throws Exception {
        RegistrationRequest request = getRegistrationRequest();
        request.setUsername("username2");
        mapper.registerModule(new JavaTimeModule());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                                                                 .contentType("application/json")
                                                                 .content(mapper.writeValueAsBytes(request)))
                                  .andReturn();
        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/logon")
                                                                    .contentType("application/json")
                                                                    .content(result.getResponse().getContentAsString()))
                                     .andReturn()
                                     .getRequest()
                                     .getSession();

        mockMvc.perform(MockMvcRequestBuilders.get("/overview").session((MockHttpSession) session))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
