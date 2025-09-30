package com.mybank.aurum.domain.account;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountTransfer {

    public void transfer(Account account, Currency from, Currency to, BigDecimal amount, BigDecimal convertedAmount) {
        withdraw(account, from, amount);
        deposit(account, to, convertedAmount);
    }

    private void withdraw(Account account, Currency currency, BigDecimal amount) {
        BigDecimal current = getBalance(account, currency);
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds.");
        }
        setBalance(account, currency, current.subtract(amount));
    }

    private void deposit(Account account, Currency currency, BigDecimal amount) {
        BigDecimal current = getBalance(account, currency);
        setBalance(account, currency, current.add(amount));
    }

    private BigDecimal getBalance(Account account, Currency currency) {
        return switch (currency) {
            case PLN -> account.getBalancePLN();
            case USD -> account.getBalanceUSD();
        };
    }

    private void setBalance(Account account, Currency currency, BigDecimal newBalance) {
        switch (currency) {
            case PLN -> account.setBalancePLN(newBalance);
            case USD -> account.setBalanceUSD(newBalance);
        }
    }
}
