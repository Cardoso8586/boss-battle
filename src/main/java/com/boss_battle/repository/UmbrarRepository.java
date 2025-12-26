package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossUmbrar;

public interface UmbrarRepository extends JpaRepository<GlobalBossUmbrar, Long> {

    List<GlobalBossUmbrar> findByAliveTrue();

    Optional<GlobalBossUmbrar> findFirstByAliveTrue();

    Optional<GlobalBossUmbrar> findByName(String name);
}
