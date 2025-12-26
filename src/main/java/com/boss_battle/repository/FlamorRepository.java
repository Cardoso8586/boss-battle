package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossFlamor;

public interface FlamorRepository extends JpaRepository<GlobalBossFlamor, Long> {

    List<GlobalBossFlamor> findByAliveTrue();

    Optional<GlobalBossFlamor> findFirstByAliveTrue();

    Optional<GlobalBossFlamor> findByName(String name);
}
