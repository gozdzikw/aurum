package com.mybank.aurum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Account {
    private Long id;
    private String firstName;
    private String lastName;
    private Double balance;
}
