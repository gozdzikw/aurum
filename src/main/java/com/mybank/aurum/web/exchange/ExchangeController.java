package com.mybank.aurum.web.exchange;

import com.mybank.aurum.application.exchange.dto.ExchangeRequest;
import com.mybank.aurum.application.exchange.dto.ExchangeResponse;
import com.mybank.aurum.application.exchange.ExchangeService;
import com.mybank.aurum.domain.account.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping
    public ResponseEntity<ExchangeResponse> exchange(@RequestBody ExchangeRequest request) {
        ExchangeResponse response = exchangeService.exchange(
                request.getAccountId(),
                request.getFrom(),
                request.getTo(),
                request.getAmount()
        );
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
