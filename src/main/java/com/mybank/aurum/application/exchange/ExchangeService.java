package com.mybank.aurum.application.exchange;

import com.mybank.aurum.domain.account.Account;
import com.mybank.aurum.domain.account.AccountTransfer;
import com.mybank.aurum.infrastructure.repository.AccountRepository;
import com.mybank.aurum.infrastructure.client.NBPClient;
import com.mybank.aurum.domain.account.Currency;
import com.mybank.aurum.application.exchange.dto.ExchangeResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeService {

    private final AccountRepository accountRepository;
    private final NBPClient nbpClient;
    private final AccountTransfer accountTransfer;

    public ExchangeService(AccountRepository accountRepository,
                           NBPClient nbpClient,
                           AccountTransfer accountTransfer) {
        this.accountRepository = accountRepository;
        this.nbpClient = nbpClient;
        this.accountTransfer = accountTransfer;
    }

    @Transactional
    public ExchangeResponse exchange(Long accountId, Currency from, Currency to, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal rate = nbpClient.getUSDPLNRate();
        BigDecimal convertedAmount;

        if (from == to) {
            convertedAmount = amount;
        } else if (from == Currency.PLN && to == Currency.USD) {
            convertedAmount = amount.divide(rate, 2, BigDecimal.ROUND_HALF_UP);
            accountTransfer.transfer(account, from, to, amount, convertedAmount);
        } else if (from == Currency.USD && to == Currency.PLN) {
            convertedAmount = amount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
            accountTransfer.transfer(account, from, to, amount, convertedAmount);
        } else {
            throw new IllegalArgumentException("Unsupported currency pair: '" + from + "' → '" + to + '.');
        }

        accountRepository.save(account);
        return new ExchangeResponse(
                account.getId(),
                account.getBalancePLN(),
                account.getBalanceUSD(),
                rate
        );
    }
}
