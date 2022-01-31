package com.clane.walletservice.repositories;

import com.clane.walletservice.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
}
