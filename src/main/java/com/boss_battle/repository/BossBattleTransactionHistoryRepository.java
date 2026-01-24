package com.boss_battle.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.BossBattleTransactionHistory;




public interface BossBattleTransactionHistoryRepository extends JpaRepository<BossBattleTransactionHistory, Long> {
	boolean existsByEmailAndAmountAndCurrencyAndCreatedAtAfter(
		    String email, String amount, String currency, LocalDateTime createdAt
		);

	List<BossBattleTransactionHistory> findByUserId(Long userId);

    List<BossBattleTransactionHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<BossBattleTransactionHistory> findAllByOrderByCreatedAtDesc();
}
