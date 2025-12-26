package com.boss_battle.service;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class EquiparGuerreiroService {

    @Autowired
    private UsuarioBossBattleRepository repo;

    
    @Transactional
    public Map<String, Number> equiparGuerreiro(Long usuarioId) {
        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreiros(usuario.getGuerreiros() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);
        repo.save(usuario);

        return Map.of(
            "sucesso", 1,
            "estoqueGuerreiro", usuario.getGuerreirosInventario(),
            "ativaGuerreiro", usuario.getGuerreiros()
        );
    }
///
    
    
    //============================================== ADICIONAR GERREIRO NA RETAGUSRADA =======================================
    
    /**
     * Adiciona guerreiros à retaguarda do jogador
     */
    @Transactional
    public Map<String, Number> adicionarGuerreirosRetaguarda(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreirosRetaguarda(usuario.getGuerreirosRetaguarda() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);

        repo.save(usuario);

        return Map.of(
            "sucesso", 1,
            "estoqueGuerreiro", usuario.getGuerreirosInventario(),
            "ativoGuerreiro", usuario.getGuerreiros()
        );
    }
//=================================================================
    
 // ==============================================
 // RETIRAR GUERREIRO DO ATAQUE
 // ==============================================
 @Transactional
 public Map<String, Number> retirarGuerreiroAtaque(Long usuarioId) {

     UsuarioBossBattle usuario = repo.findById(usuarioId)
             .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

     if (usuario.getGuerreiros() <= 0) {
         return Map.of("sucesso", 0);
     }

     usuario.setGuerreiros(usuario.getGuerreiros() - 1);
     usuario.setGuerreirosInventario(
             usuario.getGuerreirosInventario() + 1
     );

     repo.save(usuario);

     return Map.of(
         "sucesso", 1,
         "estoqueGuerreiro", usuario.getGuerreirosInventario(),
         "guerreirosRetaguarda", usuario.getGuerreirosRetaguarda()
     );
 }
//==============================================
//RETIRAR GUERREIRO DO ATAQUE E DA RETAGUARDA
//==============================================
 @Transactional
 public Map<String, Number> retirarGuerreiroRetaguarda(Long usuarioId) {

     UsuarioBossBattle usuario = repo.findById(usuarioId)
             .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

     if (usuario.getGuerreirosRetaguarda() <= 0) {
         return Map.of("sucesso", 0);
     }

     usuario.setGuerreirosRetaguarda(
             usuario.getGuerreirosRetaguarda() - 1
     );

     usuario.setGuerreirosInventario(
             usuario.getGuerreirosInventario() + 1
     );

     repo.save(usuario);

     return Map.of(
         "sucesso", 1,
         "estoqueGuerreiro", usuario.getGuerreirosInventario(),
         "retaguardaGuerreiro", usuario.getGuerreirosRetaguarda()
     );
 }

}
