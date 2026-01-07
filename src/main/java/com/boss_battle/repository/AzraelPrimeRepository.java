package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossAzraelPrime;

public interface AzraelPrimeRepository extends JpaRepository<GlobalBossAzraelPrime, Long> {

    List<GlobalBossAzraelPrime> findByAliveTrue();

    Optional<GlobalBossAzraelPrime> findFirstByAliveTrue();

    Optional<GlobalBossAzraelPrime> findByName(String name);
}
