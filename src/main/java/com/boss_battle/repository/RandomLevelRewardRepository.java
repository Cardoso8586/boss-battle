package com.boss_battle.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boss_battle.model.RandomLevelReward;

@Repository
public interface RandomLevelRewardRepository
        extends JpaRepository<RandomLevelReward, Long> {

    // 🔮 prêmio PREVISTO do usuário
    Optional<RandomLevelReward> findByUserId(Long userId);

    // (opcional) limpar tudo do usuário — útil pra reset/testes
    void deleteByUserId(Long userId);
}
