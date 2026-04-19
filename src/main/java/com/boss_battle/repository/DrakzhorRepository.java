package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossDrakzhor;

public interface DrakzhorRepository extends JpaRepository<GlobalBossDrakzhor, Long> {

    List<GlobalBossDrakzhor> findByAliveTrue();

    Optional<GlobalBossDrakzhor> findFirstByAliveTrue();

    Optional<GlobalBossDrakzhor> findByName(String name);
}
