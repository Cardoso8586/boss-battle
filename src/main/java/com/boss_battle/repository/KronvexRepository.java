package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossKronvex;

public interface KronvexRepository extends JpaRepository<GlobalBossKronvex, Long> {

    List<GlobalBossKronvex> findByAliveTrue();

    Optional<GlobalBossKronvex> findFirstByAliveTrue();

    Optional<GlobalBossKronvex> findByName(String name);
}
