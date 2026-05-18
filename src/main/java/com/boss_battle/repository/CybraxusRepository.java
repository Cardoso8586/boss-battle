package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossCybraxus;

public interface CybraxusRepository extends JpaRepository<GlobalBossCybraxus, Long> {

    List<GlobalBossCybraxus> findByAliveTrue();

    Optional<GlobalBossCybraxus> findFirstByAliveTrue();

    Optional<GlobalBossCybraxus> findByName(String name);
}
