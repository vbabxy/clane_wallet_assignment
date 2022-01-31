package com.clane.walletservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


@Table(name = "kycs")
@Entity
@Data
public class Kyc extends AuditEntity implements Serializable {

    private String levelName;

    private BigDecimal transactionLimit;
}
