package com.boss_battle.service.ativar_equipar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EscudoPrimordialService {

    private static final int QTD_MINIMA_ATIVAR = 1;
    private static final long DESGASTE_MAXIMO_ESCUDO = 200;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    public void ativarEscudoPrimordial(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = usuarioRepository
                .findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade != QTD_MINIMA_ATIVAR) {
            throw new IllegalArgumentException("Só é possível ativar 1 escudo por vez");
        }

        long escudoAtivo = usuario.getEscudoPrimordialAtivo();

        // NORMALIZA ESTADO INVÁLIDO
        if (escudoAtivo > 1) {
            usuario.setEscudoPrimordialAtivo(1);

            if (usuario.getEscudoPrimordialDesgaste() <= 0) {
                usuario.setEscudoPrimordialDesgaste(DESGASTE_MAXIMO_ESCUDO);
            }

            escudoAtivo = 1;
        }

        if (escudoAtivo > 0) {
            throw new IllegalStateException("Já existe um escudo ativo");
        }

        verificarArmasAtivas(usuario);
        verificarEstoqueEscudo(usuario, quantidade);

        usuario.setEscudoPrimordial(usuario.getEscudoPrimordial() - 1);
        usuario.setEscudoPrimordialAtivo(1);
        usuario.setEscudoPrimordialDesgaste(DESGASTE_MAXIMO_ESCUDO);

        usuarioRepository.save(usuario);
    }
    /*
    public void ativarEscudoPrimordial(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = usuarioRepository
                .findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (quantidade < QTD_MINIMA_ATIVAR) {
            throw new RuntimeException("Quantidade mínima para ativar é " + QTD_MINIMA_ATIVAR);
        }
        long escudoAtivo = usuario.getEscudoPrimordialAtivo();

        if (escudoAtivo > 0) {
            throw new IllegalStateException("Não pode ativar mais de um escudo por vez");
        }
        verificarArmasAtivas(usuario);
        verificarEstoqueEscudo(usuario, quantidade);

        // desconta do estoque
        usuario.setEscudoPrimordial(
                usuario.getEscudoPrimordial() - quantidade
        );

        // adiciona aos ativos
        usuario.setEscudoPrimordialAtivo(
                usuario.getEscudoPrimordialAtivo() + quantidade
        );

        // se não havia escudo em uso antes, inicia o desgaste do primeiro
        if (usuario.getEscudoPrimordialDesgaste() <= 0) {
            usuario.setEscudoPrimordialDesgaste(DESGASTE_MAXIMO_ESCUDO);
        }

        usuarioRepository.save(usuario);
    }
*/
    private void verificarEstoqueEscudo(UsuarioBossBattle usuario, int quantidade) {
        if (usuario.getEscudoPrimordial() < quantidade) {
            throw new RuntimeException("Escudo Primordial insuficiente no estoque");
        }
    }

    private void verificarArmasAtivas(UsuarioBossBattle usuario) {

      
        if (usuario.getArcoAtivo() > 0) {
            throw new RuntimeException(
                "Não é possível ativar o Escudo Primordial enquanto um Arco estiver equipado"
            );
        }

       
    }//--->verificarArmasAtivas

    public boolean usarEscudoPrimordial(UsuarioBossBattle usuario) {

        if (usuario.getEscudoPrimordialAtivo() <= 0) {
            return false;
        }

        long desgasteAtual = usuario.getEscudoPrimordialDesgaste();

        if (desgasteAtual <= 0) {
            usuario.setEscudoPrimordialAtivo(0);
            usuario.setEscudoPrimordialDesgaste(0);
            return false;
        }

        desgasteAtual--;

        if (desgasteAtual <= 0) {
            usuario.setEscudoPrimordialAtivo(0);
            usuario.setEscudoPrimordialDesgaste(0);
            return false;
        }

        usuario.setEscudoPrimordialDesgaste(desgasteAtual);
        return true;
    }

}