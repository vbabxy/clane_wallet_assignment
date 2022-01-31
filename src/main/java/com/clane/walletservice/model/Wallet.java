package com.clane.walletservice.model;

import com.clane.walletservice.domain.enums.WalletStatus;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "wallets")
@Entity
@Data
public class Wallet extends AuditEntity implements Serializable {

    @NotNull
    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    private BigDecimal balance;

    @NotNull
    @NotBlank
    @Column(name = "wallet_account_number")
    private String walletAccountNumber;
}

