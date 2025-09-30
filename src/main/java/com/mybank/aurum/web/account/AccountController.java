package com.mybank.aurum.web.account;

import com.mybank.aurum.domain.account.InsufficientFundsException;
import com.mybank.aurum.domain.account.dto.AccountCreateRequest;
import com.mybank.aurum.domain.account.dto.AccountResponse;
import com.mybank.aurum.application.account.AccountService;
import com.mybank.aurum.application.account.UserAlreadyHasAccountException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        AccountResponse account = accountService.getAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @ExceptionHandler(UserAlreadyHasAccountException.class)
    public ResponseEntity<String> handleUserAlreadyHasAccount(UserAlreadyHasAccountException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
