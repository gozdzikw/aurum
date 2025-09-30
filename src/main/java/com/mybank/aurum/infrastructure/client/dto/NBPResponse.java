package com.mybank.aurum.infrastructure.client.dto;

import java.math.BigDecimal;
import java.util.List;

public class NBPResponse {

    private List<Rate> rates;

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public static class Rate {
        private BigDecimal mid;

        public BigDecimal getMid() {
            return mid;
        }

        public void setMid(BigDecimal mid) {
            this.mid = mid;
        }
    }
}
