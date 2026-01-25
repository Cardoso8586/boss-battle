package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EspadaFlanejanteService {

    private static final int QTD_MINIMA_ATIVAR = 1;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    /**
     * Ativa Espadas Flanejantes
     * ❗ irreversível
     */
    public void ativarEspadaFlanejante(Long usuarioId, int quantidade) {

        UsuarioBossBattle usuario = usuarioRepository
                .findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // ❌ quantidade inválida
        if (quantidade < QTD_MINIMA_ATIVAR) {
            throw new RuntimeException(
                "Quantidade mínima para ativar é " + QTD_MINIMA_ATIVAR
            );
        }
        
  	     // 🚫 BLOQUEIA SE ESPADA ATIVA
  	        if (usuario.getMachadoDilaceradorAtivo() > 0) {
  	            throw new RuntimeException(
  	                "Não é possível equipar Espada Flanejante enquanto um Machado Dilacerador estiver equipado"
  	            );
  	        }

        // ❌ estoque insuficiente
        if (usuario.getEspadaFlanejante() < quantidade) {
            throw new RuntimeException("Espadas Flanejantes insuficientes no estoque");
        }

        // 🔥 desconta do estoque
        usuario.setEspadaFlanejante(
            usuario.getEspadaFlanejante() - quantidade
        );

        // 🔥 adiciona às equipadas
        usuario.setEspadaFlanejanteAtiva(
            usuario.getEspadaFlanejanteAtiva() + quantidade
        );
        // reseta desgaste
        usuario.setEspadaFlanejanteDesgaste(100);
        validarArmasAtivas(usuario);
       // usuarioRepository.save(usuario);
        usuarioRepository.saveAndFlush(usuario);
    }

    
    private void validarArmasAtivas(UsuarioBossBattle usuario) {

        long espadaAtiva = usuario.getEspadaFlanejanteAtiva();
        long machadoAtivo = usuario.getMachadoDilaceradorAtivo();

        // ⚠️ ESTADO ILEGAL → PUNIÇÃO
        if (
            (espadaAtiva > 0 && machadoAtivo > 0) ||
            espadaAtiva > 1 ||
            machadoAtivo > 1
        ) {
            usuario.setEspadaFlanejanteAtiva(0);
            usuario.setEspadaFlanejanteDesgaste(0);

            usuario.setMachadoDilaceradorAtivo(0);
            usuario.setMachadoDilaceradorDesgaste(0);
        }
    }

    public boolean usarEspadaFlanejante(UsuarioBossBattle usuario) {

        // ❌ nenhuma espada equipada
        if (usuario.getEspadaFlanejanteAtiva() <= 0) {
            return false;
        }

        long desgasteAtual = usuario.getEspadaFlanejanteDesgaste();

        // 🔥 consome 1% por ataque
        desgasteAtual--;

        // 💥 espada quebrou
        if (desgasteAtual <= 0) {

            // remove 1 espada ativa
            usuario.setEspadaFlanejanteAtiva(
                usuario.getEspadaFlanejanteAtiva() - 1
            );

            // reseta desgaste
            usuario.setEspadaFlanejanteDesgaste(0);

        } else {
            usuario.setEspadaFlanejanteDesgaste(desgasteAtual);
        }

        return true;
    }


    // ============================================================================
    // 🔧  desgaste da espada
    // ============================================================================

    public boolean usarEspadaSeAtiva(UsuarioBossBattle usuario) {

        // 🔥 verifica se existe espada equipada
        if (usuario.getEspadaFlanejanteAtiva() <= 0) return false;

        // 🔧 aqui você poderá aplicar lógica de desgaste
        // exemplo:
        // usuario.setEspadaFlanejanteDesgaste(
        //     usuario.getEspadaFlanejanteDesgaste() + 1
        // );

        usuarioRepository.save(usuario);
        return true;
    }
}
