package com.clane.walletservice.repositories;

import com.clane.walletservice.model.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, Long> {

    Optional<Kyc> findByLevelName(String name);

}
