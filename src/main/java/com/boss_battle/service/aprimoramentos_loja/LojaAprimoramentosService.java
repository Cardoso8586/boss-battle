package com.boss_battle.service.aprimoramentos_loja;

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
	
	private final long PRECO_ARCO_CELESTIAL = 15_000;
	private final long PRECO_MACHADO_DILACERADOR = 10_000;
	private final long POCAO_VIGOR = 10_000;
	private final long PRECO_ESPADA_FLANEJANTE = 10_000;
	private final long PRECO_ESCUDO_PRIMORDIAL = 20_000;
    
 // ---> atualizar preço do guerreiro
    public void atualizarPrecoGuerreiro(UsuarioBossBattle usuario, int quantidade) {

        long aumentoGuerreiro;

        // 🔒 Usa sempre o preço atual como base
        long precoGuerreirosAtual = usuario.getPrecoGuerreiros();
        long quantidadeTotalGuerreiro = quantidadeTotalGuerreiro(usuario);

   

        if (quantidadeTotalGuerreiro <= 25) {
            aumentoGuerreiro = 3000;
        } else if (quantidadeTotalGuerreiro <= 50) {
            aumentoGuerreiro = 4600;
        } else if (quantidadeTotalGuerreiro <= 75) {
            aumentoGuerreiro = 5700;
        } else if (quantidadeTotalGuerreiro <= 100) {
            aumentoGuerreiro = 6800;
        } else if (quantidadeTotalGuerreiro <= 125) {
            aumentoGuerreiro = 7900;
        } else if (quantidadeTotalGuerreiro <= 150) {
            aumentoGuerreiro = 8000;
        } else if (quantidadeTotalGuerreiro <= 175) {
            aumentoGuerreiro = 9100;
        } else if (quantidadeTotalGuerreiro <= 200) {
            aumentoGuerreiro = 10200;
        } else if (quantidadeTotalGuerreiro <= 225) {
            aumentoGuerreiro = 11300;
        } else if (quantidadeTotalGuerreiro <= 250) {
            aumentoGuerreiro = 12000;
        } else if (quantidadeTotalGuerreiro <= 275) {
            aumentoGuerreiro = 12400;
        } else if (quantidadeTotalGuerreiro <= 300) {
            aumentoGuerreiro = 13500;
        } else if (quantidadeTotalGuerreiro <= 325) {
            aumentoGuerreiro = 14600;
        } else if (quantidadeTotalGuerreiro <= 350) {
            aumentoGuerreiro = 15700;
        } else if (quantidadeTotalGuerreiro <= 375) {
            aumentoGuerreiro = 16500;
        } else if (quantidadeTotalGuerreiro <= 400) {
            aumentoGuerreiro = 17800;
        } else if (quantidadeTotalGuerreiro <= 425) {
            aumentoGuerreiro = 18900;
        } else if (quantidadeTotalGuerreiro <= 450) {
            aumentoGuerreiro = 20000;
        } else if (quantidadeTotalGuerreiro <= 475) {
            aumentoGuerreiro = 21100;
        } else if (quantidadeTotalGuerreiro <= 500) {
            aumentoGuerreiro = 22200;
        } else if (quantidadeTotalGuerreiro <= 600) {
            aumentoGuerreiro = 23300;
        } else if (quantidadeTotalGuerreiro <= 700) {
            aumentoGuerreiro = 24400;
        } else if (quantidadeTotalGuerreiro <= 800) {
            aumentoGuerreiro = 25500;
        } else if (quantidadeTotalGuerreiro <= 900) {
            aumentoGuerreiro = 26600;
        } else if (quantidadeTotalGuerreiro <= 1000) {
            aumentoGuerreiro = 27000;
        } else if (quantidadeTotalGuerreiro <= 1200) {
            aumentoGuerreiro = 28800;
        } else if (quantidadeTotalGuerreiro <= 1400) {
            aumentoGuerreiro = 29600;
        } else if (quantidadeTotalGuerreiro <= 1600) {
            aumentoGuerreiro = 30400;
        } else if (quantidadeTotalGuerreiro <= 1800) {
            aumentoGuerreiro = 31200;
        } else if (quantidadeTotalGuerreiro <= 2000) {
            aumentoGuerreiro = 32000;
        } else {
            aumentoGuerreiro = 50000;
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

        if (capacidadeVigor <= 2000) {
            aumentoCapacidadeVigor = 100;
        } else if (capacidadeVigor <= 4000) {
            aumentoCapacidadeVigor = 200;
        } else if (capacidadeVigor <= 6000) {
            aumentoCapacidadeVigor = 300;
        } else if (capacidadeVigor <= 8000) {
            aumentoCapacidadeVigor = 400;
        } else if (capacidadeVigor <= 10000) {
            aumentoCapacidadeVigor = 500;
        } else if (capacidadeVigor <= 12000) {
            aumentoCapacidadeVigor = 700;
        } else if (capacidadeVigor <= 14000) {
            aumentoCapacidadeVigor = 1000;
        } else if (capacidadeVigor <= 16000) {
            aumentoCapacidadeVigor = 1300;
        } else if (capacidadeVigor <= 18000) {
            aumentoCapacidadeVigor = 1600;
        } else if (capacidadeVigor <= 20000) {
            aumentoCapacidadeVigor = 2000;
        } else if (capacidadeVigor <= 22000) {
            aumentoCapacidadeVigor = 2500;
        } else if (capacidadeVigor <= 24000) {
            aumentoCapacidadeVigor = 3000;
        } else if (capacidadeVigor <= 26000) {
            aumentoCapacidadeVigor = 3500;
        } else if (capacidadeVigor <= 28000) {
            aumentoCapacidadeVigor = 4000;
        } else if (capacidadeVigor <= 30000) {
            aumentoCapacidadeVigor = 5000;
        } else {
            aumentoCapacidadeVigor = 15000;
        }
        /*
        if (capacidadeVigor <= 2000) {
            aumentoCapacidadeVigor = 100;
        } 
        else  if (capacidadeVigor <= 4000) {
            aumentoCapacidadeVigor = 200;
        }
        else  if (capacidadeVigor <= 6000) {
            aumentoCapacidadeVigor = 300;
        }
        else  if (capacidadeVigor <= 8000) {
            aumentoCapacidadeVigor = 400;
        }
        else  if (capacidadeVigor <= 10000) {
            aumentoCapacidadeVigor = 500;
        }
        else  if (capacidadeVigor <= 12000) {
            aumentoCapacidadeVigor = 700;
        }
        else  if (capacidadeVigor <= 14000) {
            aumentoCapacidadeVigor = 1000;
        }
        
        else {
            aumentoCapacidadeVigor = 15000; // valor padrão acima de 10_000
        }
*/
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
        
        
        if (atqueEspecial <= 50) {
            aumentoPrecoAtaqueEspecial = 1000;
        } else if (atqueEspecial <= 100) {
            aumentoPrecoAtaqueEspecial = 2000;
        } else if (atqueEspecial <= 150) {
            aumentoPrecoAtaqueEspecial = 3000;
        } else if (atqueEspecial <= 200) {
            aumentoPrecoAtaqueEspecial = 4000;
        } else if (atqueEspecial <= 250) {
            aumentoPrecoAtaqueEspecial = 5000;
        } else if (atqueEspecial <= 300) {
            aumentoPrecoAtaqueEspecial = 7000;
        } else if (atqueEspecial <= 350) {
            aumentoPrecoAtaqueEspecial = 10000;
        } else if (atqueEspecial <= 400) {
            aumentoPrecoAtaqueEspecial = 10300;
        } else if (atqueEspecial <= 450) {
            aumentoPrecoAtaqueEspecial = 10600;
        } else if (atqueEspecial <= 500) {
            aumentoPrecoAtaqueEspecial = 20000;
        } else if (atqueEspecial <= 550) {
            aumentoPrecoAtaqueEspecial = 20500;
        } else if (atqueEspecial <= 600) {
            aumentoPrecoAtaqueEspecial = 30000;
        } else if (atqueEspecial <= 650) {
            aumentoPrecoAtaqueEspecial = 40000;
        } else if (atqueEspecial <= 700) {
            aumentoPrecoAtaqueEspecial = 50000;
        } else if (atqueEspecial <= 750) {
            aumentoPrecoAtaqueEspecial = 70000;
        } else if (atqueEspecial <= 800) {
            aumentoPrecoAtaqueEspecial = 90000;
        } else {
            aumentoPrecoAtaqueEspecial = 150000;
        }
        
        /*
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
        	aumentoPrecoAtaqueEspecial = 15000; // valor padrão acima de 15_000
        }
*/
        
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

        if (nivel <= 10) aumento = 10;
        else if (nivel <= 20) aumento = 20;
        else if (nivel <= 30) aumento = 30;
        else if (nivel <= 40) aumento = 40;
        else if (nivel <= 50) aumento = 50;
        else if (nivel <= 70) aumento = 70;
        else if (nivel <= 100) aumento = 100;
        else if (nivel <= 200) aumento = 200;
        else if (nivel <= 300) aumento = 300;
        else if (nivel <= 400) aumento = 400;
        else if (nivel <= 500) aumento = 500;
        else if (nivel <= 600) aumento = 600;
        else if (nivel <= 700) aumento = 700;
        else if (nivel <= 800) aumento = 800;
        else if (nivel <= 900) aumento = 900;
        else if (nivel <= 1_000) aumento = 1_000;
        else aumento = 1_500;
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

        if (nivel <= 10) aumento = 10;
        else if (nivel <= 20) aumento = 20;
        else if (nivel <= 30) aumento = 30;
        else if (nivel <= 40) aumento = 40;
        else if (nivel <= 50) aumento = 50;
        else if (nivel <= 70) aumento = 70;
        else if (nivel <= 100) aumento = 100;
        else if (nivel <= 200) aumento = 200;
        else if (nivel <= 300) aumento = 300;
        else if (nivel <= 400) aumento = 400;
        else if (nivel <= 500) aumento = 500;
        else if (nivel <= 600) aumento = 600;
        else if (nivel <= 700) aumento = 700;
        else if (nivel <= 800) aumento = 800;
        else if (nivel <= 900) aumento = 900;
        else if (nivel <= 1_000) aumento = 1_000;
        else aumento = 1_500;
        usuario.setPrecoEspadaFlanejante(precoAtual + aumento);
    }

    
    public void atualizarPrecoMachadoDilacerador(UsuarioBossBattle usuario) {

        long precoAtual = usuario.getPrecoMachadoDilacerador();
        if (precoAtual <= 0) {
            precoAtual = 5_000L;
        }

        long nivel = usuario.getNivel();
        long aumento;

        if (nivel <= 10) aumento = 10;
        else if (nivel <= 20) aumento = 20;
        else if (nivel <= 30) aumento = 30;
        else if (nivel <= 40) aumento = 40;
        else if (nivel <= 50) aumento = 50;
        else if (nivel <= 70) aumento = 70;
        else if (nivel <= 100) aumento = 100;
        else if (nivel <= 200) aumento = 200;
        else if (nivel <= 300) aumento = 300;
        else if (nivel <= 400) aumento = 400;
        else if (nivel <= 500) aumento = 500;
        else if (nivel <= 600) aumento = 600;
        else if (nivel <= 700) aumento = 700;
        else if (nivel <= 800) aumento = 800;
        else if (nivel <= 900) aumento = 900;
        else if (nivel <= 1_000) aumento = 1_000;
        else aumento = 1_500;

        usuario.setPrecoMachadoDilacerador(precoAtual + aumento);
    }

    
    //Atualizar o preço do escudo
    public void atualizarPrecoEscudoPrimordial(UsuarioBossBattle usuario, int quantidade) {
    	
  
          // 🔒 Usa sempre o preço atual como base
          long precoEscudoAtual = usuario.getPrecoEscudoPrimordial();
         
     
          if (precoEscudoAtual <= 0) {
        	  precoEscudoAtual = 10_000L;
          }

          long nivel = usuario.getNivel();

          long aumento;

          if (nivel <= 10) aumento = 500;
          else if (nivel <= 20) aumento = 700;
          else if (nivel <= 30) aumento = 1_000;
          else if (nivel <= 40) aumento = 1_200;
          else if (nivel <= 50) aumento = 1_500;
          else if (nivel <= 70) aumento = 1_700;
          else if (nivel <= 100) aumento = 2_000;
          else if (nivel <= 200) aumento = 2_200;
          else if (nivel <= 300) aumento = 2_500;
          else if (nivel <= 400) aumento = 2_700;
          else if (nivel <= 500) aumento = 3_000;
          else if (nivel <= 600) aumento = 3_300;
          else if (nivel <= 700) aumento = 3_700;
          else if (nivel <= 800) aumento = 4_000;
          else if (nivel <= 900) aumento = 4_500;
          else if (nivel <= 1_000) aumento = 5_000;
          else aumento = 10_000;
          // 🔼 aumento fixo e permanente
          usuario.setPrecoPocaoVigor(precoEscudoAtual + aumento);
          
          
          
    	
    }//atualizarPrecoEscudoPrimordial
    
    //======================================
    /*
    public void atualizarPrecoLoja(UsuarioBossBattle usuario) {
    	
    	atualizarPrecoMachadoDilacerador(usuario);
    	atualizarPrecoEspadaFlanejante(usuario);
    	atualizarPrecoPocaoVigor(usuario);
    	
    }//atualizarPrecoLoja
    */
	public long getPRECO_ARCO_CELESTIAL() {
		return PRECO_ARCO_CELESTIAL;
	}
	public long getPRECO_MACHADO_DILACERADOR() {
		return PRECO_MACHADO_DILACERADOR;
	}
	public long getPOCAO_VIGOR() {
		return POCAO_VIGOR;
	}
	public long getPRECO_ESPADA_FLANEJANTE() {
		return PRECO_ESPADA_FLANEJANTE;
	}
	public long getPRECO_ESCUDO_PRIMORDIAL() {
		return PRECO_ESCUDO_PRIMORDIAL;
	}
    
    
   
    
}
