package com.mybank.aurum.application.account;

import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import com.mybank.aurum.domain.account.Account;
import com.mybank.aurum.infrastructure.repository.AccountRepository;
import com.mybank.aurum.domain.user.User;
import com.mybank.aurum.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, userRepository);
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyHasAccount() {
        // given
        String firstName = "John";
        String lastName = "Doe";
        AccountCreateRequest request = new AccountCreateRequest(firstName, lastName, new BigDecimal("999"));

        User existingUser = new User(1L, firstName, lastName, null);
        Account existingAccount = new Account(BigDecimal.ONE, BigDecimal.ZERO, existingUser);
        existingUser.setAccount(existingAccount);

        // when
        when(userRepository.findByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(existingUser));

        // then
        assertThrows(UserAlreadyHasAccountException.class, () -> accountService.createAccount(request));
        verify(accountRepository, never()).save(any(Account.class));
    }
}