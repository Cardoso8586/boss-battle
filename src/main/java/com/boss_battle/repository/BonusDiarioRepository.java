package com.boss_battle.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boss_battle.model.BossBonusDiario;
import com.boss_battle.model.UsuarioBossBattle;


@Repository
public interface BonusDiarioRepository extends JpaRepository<BossBonusDiario, Long> {
    Optional<BossBonusDiario> findByUsuarioAndDataColeta(UsuarioBossBattle usuario, LocalDate dataColeta);
    Optional<BossBonusDiario> findTopByUsuarioOrderByDataColetaDesc(UsuarioBossBattle usuario);
    Optional<BossBonusDiario> findTopByUsuarioAndDataColetaBeforeOrderByDataColetaDesc(UsuarioBossBattle usuario, LocalDate data);
	

}//BonusDiarioRepository
