package com.clane.walletservice.model;

import com.clane.walletservice.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Table(name = "transactions")
@Entity
@Data
public class Transaction extends AuditEntity implements Serializable {

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @NotBlank
    @Column(name = "transaction_reference")
    private String transactionReference;
}