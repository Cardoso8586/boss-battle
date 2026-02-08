package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossTrigonBaphydrax;

public interface TrigonBaphydraxRepository extends JpaRepository<GlobalBossTrigonBaphydrax, Long> {

    List<GlobalBossTrigonBaphydrax> findByAliveTrue();

    Optional<GlobalBossTrigonBaphydrax> findFirstByAliveTrue();

    Optional<GlobalBossTrigonBaphydrax> findByName(String name);
}
