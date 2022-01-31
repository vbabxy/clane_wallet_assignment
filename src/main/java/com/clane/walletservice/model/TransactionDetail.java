package com.clane.walletservice.model;

import com.clane.walletservice.domain.enums.TransactionClass;
import com.clane.walletservice.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Table(name = "transaction_details")
@Entity
public class TransactionDetail extends AuditEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Wallet wallet;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionClass transactionClass;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Transaction transaction;

    @Column(name = "starting_balance")
    private BigDecimal startingBalance;

    @Column(name = "final_balance")
    private BigDecimal finalBalance;
}
