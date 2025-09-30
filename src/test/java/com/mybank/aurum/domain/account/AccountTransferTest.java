package com.mybank.aurum.domain.account;

import com.mybank.aurum.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTransferTest {

    private AccountTransfer accountTransfer;
    private Account account;

    @BeforeEach
    void setUp() {
        accountTransfer = new AccountTransfer();
        User user = new User("John", "Doe");
        account = new Account(BigDecimal.valueOf(9999), BigDecimal.valueOf(666), user);
        user.setAccount(account);
    }

    @Test
    void shouldTransferFoundsFromPLNtoUSD() {
        // given
        BigDecimal amountPLN = BigDecimal.valueOf(250);
        BigDecimal convertedAmountUSD = BigDecimal.valueOf(100);
        BigDecimal expectedPLNBalance = account.getBalancePLN().subtract(amountPLN) ;
        BigDecimal expectedUSDBalance = account.getBalanceUSD().add(convertedAmountUSD) ;

        // when
        accountTransfer.transfer(account, Currency.PLN, Currency.USD, amountPLN, convertedAmountUSD);

        // then
        assertEquals(expectedPLNBalance, account.getBalancePLN());
        assertEquals(expectedUSDBalance, account.getBalanceUSD());
    }

    @Test
    void shouldThrowExceptionIfInsufficientFounds() {
        //expect
        assertThrows(InsufficientFundsException.class, () ->
                accountTransfer.transfer(account, Currency.PLN, Currency.USD, BigDecimal.valueOf(9999999), BigDecimal.ZERO)
        );
    }
}
