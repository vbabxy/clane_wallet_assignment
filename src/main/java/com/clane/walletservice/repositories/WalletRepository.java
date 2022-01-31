package com.clane.walletservice.repositories;

import com.clane.walletservice.model.User;
import com.clane.walletservice.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findWalletByUser(User user);

    Page<Wallet> findAllByWalletAccountNumberContaining(String walletAccountNumber, Pageable pageable);

    Optional<Wallet> findWalletByUser_EmailAddress(String walletIdentifier);

    @Query("select w from Wallet w where w.walletAccountNumber = ?1")
    Optional<Wallet> findWalletByWalletAccountNumber(String walletAccountNumber);
}
