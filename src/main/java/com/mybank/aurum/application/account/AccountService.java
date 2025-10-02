package com.mybank.aurum.application.account;

import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import com.mybank.aurum.domain.account.dto.AccountResponse;
import com.mybank.aurum.domain.account.Account;
import com.mybank.aurum.infrastructure.repository.AccountRepository;
import com.mybank.aurum.domain.user.User;
import com.mybank.aurum.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                              UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        log.info("Creating new account for {} {}", request.getFirstName(), request.getLastName());

        User user = userRepository
                .findByFirstNameAndLastName(request.getFirstName(), request.getLastName())
                .orElseGet(() -> {
                    User newUser = new User(request.getFirstName(), request.getLastName());
                    return userRepository.save(newUser);
                });

        if (user.getAccount() != null) {
            throw new UserAlreadyHasAccountException(
                    "User: '" + user.getFirstName() + " " + user.getLastName() + "' already has an account."
            );
        }

        Account account = new Account(request.getInitialBalance(), BigDecimal.ZERO, user);
        account = accountRepository.save(account);

        return new AccountResponse(
                account.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                account.getBalancePLN(),
                account.getBalanceUSD()
        );
    }

    public AccountResponse getAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found."));

        User user = account.getUser();

        return new AccountResponse(
                account.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                account.getBalancePLN(),
                account.getBalanceUSD()
        );
    }
}
