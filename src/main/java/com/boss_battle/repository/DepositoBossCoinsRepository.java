package com.boss_battle.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boss_battle.model.DepositoBossCoins;

import jakarta.persistence.LockModeType;

public interface DepositoBossCoinsRepository
extends JpaRepository<DepositoBossCoins, Long> {

Optional<DepositoBossCoins> findByPaymentId(String paymentId);

List<DepositoBossCoins> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

void deleteByStatusInAndCriadoEmBefore(List<String> status, LocalDateTime data);


Optional<DepositoBossCoins> findByOrderId(String orderId);

long countByStatusInAndCriadoEmBefore(
        List<String> status,
        LocalDateTime criadoEm
);

List<DepositoBossCoins> findByCreditadoFalse();

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT d FROM DepositoBossCoins d WHERE d.paymentId = :paymentId")
Optional<DepositoBossCoins> findByPaymentIdForUpdate(@Param("paymentId") String paymentId);


}