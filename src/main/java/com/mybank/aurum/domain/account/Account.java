package com.mybank.aurum.domain.account;

import com.mybank.aurum.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@Table(name = "accounts")
@NoArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balancePLN;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balanceUSD;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Account(
            BigDecimal balancePLN,
            BigDecimal balanceUSD,
            User user
    ) {
        if (user == null) {
            throw new IllegalArgumentException("Account should have user.");
        }
        this.balancePLN = balancePLN.setScale(2, RoundingMode.HALF_UP);
        this.balanceUSD = balanceUSD.setScale(2, RoundingMode.HALF_UP);
        this.user = user;
    }
}
