package com.mybank.aurum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccount() throws Exception {
        //given
        AccountController.AccountRequest request = new AccountController.AccountRequest();
        //TODO powyciagac
        var name = "Name";
        request.setFirstName(name);
        String lastName = "LastName";
        request.setLastName(lastName);
        request.setInitialBalance(1000.0);

        //when
        ResultActions response = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then:
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(name))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void shouldReturnAccountById() throws Exception {
        //given
        AccountController.AccountRequest request = new AccountController.AccountRequest();
        request.setFirstName("FirstName");
        request.setLastName("LastName");
        request.setInitialBalance(500.0);

        //when
        ResultActions response = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        response
                .andExpect(status().isCreated());

        //and
        AccountController.AccountResponse accountResponse =
                objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), AccountController.AccountResponse.class);

        //when
        ResultActions response3 = mockMvc.perform(get("/api/accounts/{id}", accountResponse.getId()));

        //then
        response3
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountResponse.getId()))
                .andExpect(jsonPath("$.firstName").value("FirstName"))
                .andExpect(jsonPath("$.lastName").value("LastName"))
                .andExpect(jsonPath("$.balance").value(500.0));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {
        //given
        ResultActions response = mockMvc.perform(get("/api/accounts/{id}", 9999L));

        //expect
        response.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestInvalid() throws Exception {
        //given
        AccountController.AccountRequest request = new AccountController.AccountRequest();

        //when
        ResultActions response = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        response.andExpect(status().isBadRequest());
    }
}
