package com.mybank.aurum.application.account;

import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import com.mybank.aurum.domain.account.dto.AccountResponse;
import com.mybank.aurum.infrastructure.repository.AccountRepository;
import com.mybank.aurum.infrastructure.repository.UserRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@ActiveProfiles("integration")
@SpringBootTest
@Transactional
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldCreateNewUserAndAccount() {
        // given
        String firstName = "John";
        String lastName = "Doe";
        BigDecimal initialBalance = BigDecimal.valueOf(1000, 2);
        AccountCreateRequest request = new AccountCreateRequest(
                firstName,
                lastName,
                initialBalance
        );

        // when
        AccountResponse response = accountService.createAccount(request);

        // then
        assertNotNull(response.getUserId());
        assertNotNull(response.getAccountId());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(initialBalance, response.getBalancePLN());
        assertEquals(BigDecimal.ZERO.setScale(2), response.getBalanceUSD());

        assertEquals(1, userRepository.findAll().size());
        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyHasAccount() {
        String firstName = "John";
        String lastName = "Smith";
        BigDecimal initialBalance = BigDecimal.valueOf(111, 2);
        AccountCreateRequest request = new AccountCreateRequest(
                firstName,
                lastName,
                initialBalance
        );

        accountService.createAccount(request);

        // then
        assertThrows(RuntimeException.class, () -> accountService.createAccount(request));
    }
}