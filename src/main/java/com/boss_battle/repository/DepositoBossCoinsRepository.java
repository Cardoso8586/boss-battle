package com.boss_battle.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.DepositoBossCoins;

public interface DepositoBossCoinsRepository
extends JpaRepository<DepositoBossCoins, Long> {

Optional<DepositoBossCoins> findByPaymentId(String paymentId);

List<DepositoBossCoins> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

void deleteByStatusInAndCriadoEmBefore(List<String> status, LocalDateTime data);


Optional<DepositoBossCoins> findByOrderId(String orderId);

}