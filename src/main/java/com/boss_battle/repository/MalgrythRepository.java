package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossMalgryth;

public interface MalgrythRepository extends JpaRepository<GlobalBossMalgryth, Long> {

    List<GlobalBossMalgryth> findByAliveTrue();

    Optional<GlobalBossMalgryth> findFirstByAliveTrue();

    Optional<GlobalBossMalgryth> findByName(String name);
}
