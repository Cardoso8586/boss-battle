package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.Guerreiro;

public interface GuerreiroRepository extends JpaRepository<Guerreiro, Long> {

    Optional<Guerreiro> findByPadraoTrue();

    Optional<Guerreiro> findByNome(String nome);
    boolean existsByNome(String nome);
    
    List<Guerreiro> findByAtivoTrue();
}