package com.mybank.aurum.web.exchange;

import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import com.mybank.aurum.domain.account.dto.AccountResponse;
import com.mybank.aurum.infrastructure.repository.AccountRepository;
import com.mybank.aurum.infrastructure.client.NBPClient;
import com.mybank.aurum.domain.account.Currency;
import com.mybank.aurum.application.exchange.dto.ExchangeRequest;
import com.mybank.aurum.application.exchange.dto.ExchangeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private NBPClient nbpClient; // zamockowane API

    private Long accountId;

    BigDecimal exchangeRate = BigDecimal.valueOf(3.6245);
    BigDecimal initialBalance = BigDecimal.valueOf(1000);

    @BeforeEach
    void setUp() {
        when(nbpClient.getUSDPLNRate()).thenReturn(exchangeRate);
        AccountCreateRequest createRequest = new AccountCreateRequest("John", "Doe", initialBalance);
        ResponseEntity<AccountResponse> response = restTemplate.postForEntity(
                "/accounts",
                createRequest,
                AccountResponse.class
        );

        accountId = response.getBody().getAccountId();
        assertNotNull(accountId);
    }

    @Test
    void shouldExchangePLNToUSD() {
        //given
        BigDecimal amount = BigDecimal.valueOf(450);
        BigDecimal expectedBalancePLN = initialBalance.subtract(amount).setScale(2);
        BigDecimal expectedBalanceUSD = amount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
        ExchangeRequest exchangeRequest = new ExchangeRequest(accountId, Currency.PLN, Currency.USD, amount);

        //when
        ResponseEntity<ExchangeResponse> response = restTemplate.postForEntity(
                "/api/exchange",
                exchangeRequest,
                ExchangeResponse.class
        );

        //then
        assertEquals(200, response.getStatusCodeValue());
        ExchangeResponse body = response.getBody();
        assertEquals(accountId, body.getAccountId());
        assertEquals(expectedBalancePLN, body.getBalancePLN());
        assertEquals(expectedBalanceUSD, body.getBalanceUSD());
    }
}