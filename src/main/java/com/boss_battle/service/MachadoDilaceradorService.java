package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MachadoDilaceradorService {
	
	 private static final int QTD_MINIMA_ATIVAR = 1;	
	

	    @Autowired
	    private UsuarioBossBattleRepository usuarioRepository;


	    /**
	     * Ativa machdo dilacerador
	     * ❗ irreversível
	     */
	
	    public void ativarMachadoDilacerador(Long usuarioId, int quantidade) {
	    UsuarioBossBattle usuario = usuarioRepository
	                  .findByIdForUpdate(usuarioId)
	                  .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	    	

        // ❌ quantidade inválida
        if (quantidade < QTD_MINIMA_ATIVAR) {
            throw new RuntimeException(
                "Quantidade mínima para ativar é " + QTD_MINIMA_ATIVAR
            );
        }
        
        // ❌ estoque insuficiente
        if (usuario.getMachadoDilacerador() < quantidade) {
            throw new RuntimeException("Machado dilacerador insuficientes no estoque");
        }
        
     // 🚫 BLOQUEIA SE ESPADA ATIVA
	        if (usuario.getEspadaFlanejanteAtiva() > 0) {
	            throw new RuntimeException(
	                "Não é possível equipar O Machado enquanto uma Espadada Flanejante estiver equipada"
	            );
	        }
        
        // 🔥 desconta do estoque
        usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() - quantidade);
 
        // 🔥 adiciona às equipadas
        usuario.setMachadoDilaceradorAtivo(usuario.getMachadoDilaceradorAtivo() + quantidade);
        
        // reseta desgaste
        usuario.setMachadoDilaceradorDesgaste(200);
        validarArmasAtivas(usuario);
        //usuarioRepository.save(usuario);
        usuarioRepository.saveAndFlush(usuario);
        
 }//--->ativarMachdoDilacerador
	  
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
	    
	//===============================================================================
	                 // USAR MACHADO DILACERADOR
	//===============================================================================
	    public boolean usarMachadoDilacerador(UsuarioBossBattle usuario) {

	        // ❌ nenhuma espada equipada
	        if (usuario.getMachadoDilaceradorAtivo() <= 0) {
	            return false;
	        }
	        
	     // 🚫 BLOQUEIA SE ESPADA ATIVA
	        if (usuario.getEspadaFlanejanteAtiva() > 0) {
	            throw new RuntimeException(
	                "Não é possível equipar o machado enquanto uma espada estiver equipada"
	            );
	        }


	        long desgasteAtual = usuario.getMachadoDilaceradorDesgaste();

	        // 🔥 consome 1% por ataque
	        desgasteAtual--;

	        // 💥 espada quebrou
	        if (desgasteAtual <= 0) {

	            // remove 1 espada ativa
	            usuario.setMachadoDilaceradorAtivo(
	                usuario.getMachadoDilaceradorAtivo() - 1
	            );

	            // reseta desgaste
	            usuario.setMachadoDilaceradorDesgaste(0);

	        } else {
	            usuario.setMachadoDilaceradorDesgaste(desgasteAtual);
	        }
	       
	        return true;
	    }
	
	    
}//--->MachadoDilaceradorService
