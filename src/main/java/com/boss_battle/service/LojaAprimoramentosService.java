package com.boss_battle.service;

import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class LojaAprimoramentosService {

    // =====================================
    // PREÇO FIXO DA POÇÃO
    // =====================================
    private static final long PRECO_BASE_PORCAOVIGOR = 5_000L;
    private static final long PRECO_BASE_ESPADA_FLANEJANTE = 5_000L;
    
    /**
     * ⚠️ ESTE MÉTODO DEVE SER CHAMADO
     * SOMENTE APÓS UMA COMPRA
     */
    
 // ---> atualizar preço do guerreiro
    public void atualizarPrecoGuerreiro(UsuarioBossBattle usuario, int quantidade) {

        long aumentoGuerreiro;

        // 🔒 Usa sempre o preço atual como base
        long precoGuerreirosAtual = usuario.getPrecoGuerreiros();
        long quantidadeTotalGuerreiro = quantidadeTotalGuerreiro(usuario);

        if (quantidadeTotalGuerreiro <= 100) {
            aumentoGuerreiro = 100;
        } 
        else if (quantidadeTotalGuerreiro <= 200) {
            aumentoGuerreiro = 300;
        } 
        else if (quantidadeTotalGuerreiro <= 300) {
            aumentoGuerreiro = 500;
        } 
        else if (quantidadeTotalGuerreiro <= 500) {
            aumentoGuerreiro = 800;
        } 
        else if (quantidadeTotalGuerreiro <= 1000) {
            aumentoGuerreiro = 1000;
        } 
        else if (quantidadeTotalGuerreiro <= 5000) {
            aumentoGuerreiro = 10000;
        } 
        else {
            aumentoGuerreiro = 15000; // valor para acima de 5000 (opcional)
        }

        // 🔼 aumento fixo e permanente
        usuario.setPrecoGuerreiros(
            precoGuerreirosAtual + (quantidade * aumentoGuerreiro)
        );
    }
    public long quantidadeTotalGuerreiro(UsuarioBossBattle usuario) {
    	
   	 //cria qiantidade total de guerreiro por usuario
       long quantGuerreiros = usuario.getGuerreiros();
       long estoqueGuerreiro = usuario.getGuerreirosInventario();
       long  guerreirosRetaguarda = usuario.getGuerreirosRetaguarda();
       long totalGuerreiro = quantGuerreiros + estoqueGuerreiro + guerreirosRetaguarda;
   	
       return totalGuerreiro;
   }
   //========================================================================================

    
  
 // ---> atualizar preço do vigor
    public void atualizarPrecoVigor(UsuarioBossBattle usuario, int quantidade) {

        long aumentoCapacidadeVigor;

        // 🔒 Usa sempre o preço atual como base
        long precoEnergiaAtual = usuario.getPrecoEnergia();
        long capacidadeVigor = usuario.getEnergiaGuerreirosPadrao();

        if (capacidadeVigor <= 1000) {
            aumentoCapacidadeVigor = 100;
        } 
        else  if (capacidadeVigor <= 2000) {
            aumentoCapacidadeVigor = 200;
        }
        else  if (capacidadeVigor <= 3000) {
            aumentoCapacidadeVigor = 300;
        }
        else  if (capacidadeVigor <= 4000) {
            aumentoCapacidadeVigor = 400;
        }
        else  if (capacidadeVigor <= 5000) {
            aumentoCapacidadeVigor = 500;
        }
        else  if (capacidadeVigor <= 7000) {
            aumentoCapacidadeVigor = 700;
        }
        else  if (capacidadeVigor <= 10000) {
            aumentoCapacidadeVigor = 1000;
        }
        
        else {
            aumentoCapacidadeVigor = 15000; // valor padrão acima de 10_000
        }

        usuario.setPrecoEnergia(
            precoEnergiaAtual + (quantidade * aumentoCapacidadeVigor)
        );
    }

   
    
    //---> atualizar preço ataque especial
    public void atualizarPrecoAtaqueEspecial(UsuarioBossBattle usuario, int quantidade) {
        long aumentoPrecoAtaqueEspecial;
        // 🔒 Usa sempre o preço atual como base
        long precoAtaqueAtual = usuario.getPrecoAtaqueEspecial();
        long atqueEspecial = usuario.getAtaqueBase();
        
        if (atqueEspecial <= 1000) {
        	aumentoPrecoAtaqueEspecial = 100;
        } 
        else  if (atqueEspecial <= 2000) {
        	aumentoPrecoAtaqueEspecial = 200;
        }
        else  if (atqueEspecial <= 3000) {
        	aumentoPrecoAtaqueEspecial = 300;
        }
        else  if (atqueEspecial <= 4000) {
        	aumentoPrecoAtaqueEspecial = 400;
        }
        else  if (atqueEspecial <= 5000) {
        	aumentoPrecoAtaqueEspecial = 500;
        }
        else  if (atqueEspecial <= 7000) {
        	aumentoPrecoAtaqueEspecial = 700;
        }
        else  if (atqueEspecial <= 10000) {
        	aumentoPrecoAtaqueEspecial = 1000;
        }
        
        else {
        	aumentoPrecoAtaqueEspecial = 15000; // valor padrão acima de 10_000
        }

        
        // 🔼 aumento fixo e permanente
        usuario.setPrecoAtaqueEspecial(precoAtaqueAtual + ( quantidade* aumentoPrecoAtaqueEspecial) );
    }
     //============================================================================================
                                   //
    //=============================================================================================
    
    
   
    
    //======================================
    
    public long getPrecoBasePorcaovigor() {
        return PRECO_BASE_PORCAOVIGOR;
    }
    
    //======================================
    
    public long getPrecoBaseEspadaFlanejante() {
        return PRECO_BASE_ESPADA_FLANEJANTE;
    }
    
    
    
}
