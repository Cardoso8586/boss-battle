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
        
        // 🔥 desconta do estoque
        usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() - quantidade);
 
        // 🔥 adiciona às equipadas
        usuario.setMachadoDilaceradorAtivo(usuario.getMachadoDilaceradorAtivo() + quantidade);
        
        // reseta desgaste
        usuario.setMachadoDilaceradorDesgaste(200);
        
        usuarioRepository.save(usuario);
        
        
 }//--->ativarMachdoDilacerador
	    
	//===============================================================================
	                 // USAR MACHADO DILACERADOR
	//===============================================================================
	    public boolean usarMachadoDilacerador(UsuarioBossBattle usuario) {

	        // ❌ nenhuma espada equipada
	        if (usuario.getMachadoDilaceradorAtivo() <= 0) {
	            return false;
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
