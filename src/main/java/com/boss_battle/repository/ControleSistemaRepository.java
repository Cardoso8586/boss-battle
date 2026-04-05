package com.boss_battle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.ControleSistema;

public interface ControleSistemaRepository extends JpaRepository<ControleSistema, Long> {
}