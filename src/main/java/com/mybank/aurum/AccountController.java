package com.mybank.aurum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    private final Map<Long, Account> accounts = new HashMap<>();
    private final AtomicLong accountIdGenerator = new AtomicLong(1);

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("hello");
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        if (request.getFirstName() == null || request.getLastName() == null || request.getInitialBalance() == null) {
            return ResponseEntity.badRequest().build();
        }

        long id = accountIdGenerator.getAndIncrement();
        Account account = new Account(id, request.getFirstName(), request.getLastName(), request.getInitialBalance());
        accounts.put(id, account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccountResponse(id, account.getFirstName(), account.getLastName(), account.getBalance()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = accounts.get(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new AccountResponse(account.getId(), account.getFirstName(), account.getLastName(), account.getBalance()));
    }

    @Data
    @NoArgsConstructor
    public static class AccountRequest {
        private String firstName;
        private String lastName;
        private Double initialBalance;
    }

    @Data
    @AllArgsConstructor
    static class AccountResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private Double balance;
    }
}
