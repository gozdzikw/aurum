package com.mybank.aurum.application.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ExchangeResponse {
    private Long accountId;
    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;
    private BigDecimal rate;
}
