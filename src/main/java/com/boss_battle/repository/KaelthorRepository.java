package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossKaelthor;


public interface KaelthorRepository extends JpaRepository<GlobalBossKaelthor, Long> {

    List<GlobalBossKaelthor> findByAliveTrue();

    Optional<GlobalBossKaelthor> findFirstByAliveTrue();

    Optional<GlobalBossKaelthor> findByName(String name);
}
