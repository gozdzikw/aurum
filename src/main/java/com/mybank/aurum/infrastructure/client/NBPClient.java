package com.mybank.aurum.infrastructure.client;

import com.mybank.aurum.infrastructure.client.dto.NBPResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class NBPClient {

    private static final String NBP_API_URL = "http://api.nbp.pl/api/exchangerates/rates/A/{currency}/?format=json";
    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getUSDPLNRate() {
        NBPResponse response = restTemplate.getForObject(NBP_API_URL, NBPResponse.class, "USD");
        return response.getRates().get(0).getMid();
    }
}
