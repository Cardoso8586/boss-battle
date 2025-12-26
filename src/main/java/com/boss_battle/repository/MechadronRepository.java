package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMechadron;

public interface MechadronRepository extends JpaRepository<GlobalBossMechadron, Long> {

    List<GlobalBossMechadron> findByAliveTrue();

    Optional<GlobalBossMechadron> findFirstByAliveTrue();

    Optional<GlobalBossMechadron> findByName(String name);
}
