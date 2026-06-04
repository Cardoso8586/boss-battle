package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossTitanex;

public interface TitanexRepository extends JpaRepository<GlobalBossTitanex, Long> {

    List<GlobalBossTitanex> findByAliveTrue();

    Optional<GlobalBossTitanex> findFirstByAliveTrue();

    Optional<GlobalBossTitanex> findByName(String name);
}
