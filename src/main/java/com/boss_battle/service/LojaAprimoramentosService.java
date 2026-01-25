package com.boss_battle.service;

import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class LojaAprimoramentosService {


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
    
 // ---> atualizar preço poção de vigor
    public void atualizarPrecoPocaoVigor(UsuarioBossBattle usuario) {

        long precoAtual = usuario.getPrecoPocaoVigor();
     

        if (precoAtual <= 0) {
            precoAtual = 5_000L;
        }

        long nivel = usuario.getNivel();

        long aumento;

        if (nivel <= 100) {
            aumento = 100;
        } else if (nivel <= 200) {
            aumento = 200;
        } else if (nivel <= 300) {
            aumento = 300;
        } else if (nivel <= 400) {
            aumento = 400;
        } else if (nivel <= 500) {
            aumento = 500;
        } else if (nivel <= 700) {
            aumento = 700;
        } else if (nivel <= 1_000) {
            aumento = 1_000;
        } else {
            aumento = 10_000;
        }

        // 🔼 aumento fixo e permanente
        usuario.setPrecoPocaoVigor(precoAtual + aumento);
    }

    
 // ---> atualizar preço espada flanejante
    public void atualizarPrecoEspadaFlanejante(UsuarioBossBattle usuario) {

      
        
      
        long precoAtual = usuario.getPrecoEspadaFlanejante();

        if (precoAtual <= 0) {
            precoAtual = 5_000L;
        }

        long nivel = usuario.getNivel();

        long aumento;

        if (nivel <= 100) {
            aumento = 100;
        } else if (nivel <= 200) {
            aumento = 200;
        } else if (nivel <= 300) {
            aumento = 300;
        } else if (nivel <= 400) {
            aumento = 400;
        } else if (nivel <= 500) {
            aumento = 500;
        } else if (nivel <= 700) {
            aumento = 700;
        } else if (nivel <= 1_000) {
            aumento = 1_000;
        } else {
            aumento = 10_000;
        }

        usuario.setPrecoEspadaFlanejante(precoAtual + aumento);
    }

    
 // ---> atualizar preço espada flanejante
    public void atualizarPrecoMachadoDilacerador(UsuarioBossBattle usuario) {

        long precoAtual = usuario.getPrecoMachadoDilacerador();
        if (precoAtual <= 0) {
            precoAtual = 5_000L;
        }
        long nivel = usuario.getNivel();

        long aumento;

        if (nivel <= 100) {
            aumento = 100;
        } else if (nivel <= 200) {
            aumento = 200;
        } else if (nivel <= 300) {
            aumento = 300;
        } else if (nivel <= 400) {
            aumento = 400;
        } else if (nivel <= 500) {
            aumento = 500;
        } else if (nivel <= 700) {
            aumento = 700;
        } else if (nivel <= 1_000) {
            aumento = 1_000;
        } else {
            aumento = 10_000;
        }

        usuario.setPrecoMachadoDilacerador(precoAtual + aumento);
    }

    
    //======================================
    
    public void atualizarPrecoLoja(UsuarioBossBattle usuario) {
    	
    	atualizarPrecoMachadoDilacerador(usuario);
    	atualizarPrecoEspadaFlanejante(usuario);
    	atualizarPrecoPocaoVigor(usuario);
    	
    }//atualizarPrecoLoja
    
    
   
    
}
