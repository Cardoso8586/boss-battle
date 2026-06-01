package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.Guerreiro;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.model.UsuarioGuerreiro;

public interface UsuarioGuerreiroRepository extends JpaRepository<UsuarioGuerreiro, Long> {

    List<UsuarioGuerreiro> findByUsuario(UsuarioBossBattle usuario);

    Optional<UsuarioGuerreiro> findByUsuarioAndGuerreiro(
        UsuarioBossBattle usuario,
        Guerreiro guerreiro
    );
    
    
}