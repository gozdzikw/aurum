package com.mybank.aurum.web.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class AccountE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountAndReturnResponse() throws Exception {
        // given
        String firstName = "John";
        String lastName = "Doe";
        BigDecimal initialBalance = BigDecimal.valueOf(123, 2);
        AccountCreateRequest request = new AccountCreateRequest(
                firstName,
                lastName,
                initialBalance
        );

        // when
        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.accountId").isNumber())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.balancePLN").value(initialBalance))
                .andExpect(jsonPath("$.balanceUSD").value("0.0"));
    }

    @Test
    void shouldReturnAccount() throws Exception {
        // given
        String firstName = "Johnny";
        String lastName = "Smith";
        BigDecimal initialBalance = BigDecimal.valueOf(1234, 2);
        AccountCreateRequest request = new AccountCreateRequest(
                firstName,
                lastName,
                initialBalance
        );

        // when
        String responseContent = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long accountId = objectMapper.readTree(responseContent).get("accountId").asLong();

        // and
        ResultActions result = mockMvc.perform(get("/accounts/{id}", accountId));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.balancePLN").value(initialBalance));
    }

    @Test
    void shouldReturnErrorWhenUserAlreadyHasAccount() throws Exception {
        // given
        String firstName = "Ann";
        String lastName = "Smith";
        BigDecimal initialBalance = new BigDecimal("1000");
        AccountCreateRequest request = new AccountCreateRequest(
                firstName,
                lastName,
                initialBalance
        );

        // when
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User: 'Ann Smith' already has an account."));

    }
}