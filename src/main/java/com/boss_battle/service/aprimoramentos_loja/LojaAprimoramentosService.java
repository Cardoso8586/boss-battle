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
    
 // ---> atualizar preço do guerreiro
    public void atualizarPrecoGuerreiro(UsuarioBossBattle usuario, int quantidade) {

        long aumentoGuerreiro;

        // 🔒 Usa sempre o preço atual como base
        long precoGuerreirosAtual = usuario.getPrecoGuerreiros();
        long quantidadeTotalGuerreiro = quantidadeTotalGuerreiro(usuario);

   

        if (quantidadeTotalGuerreiro <= 25) {
            aumentoGuerreiro = 500;
        } else if (quantidadeTotalGuerreiro <= 50) {
            aumentoGuerreiro = 600;
        } else if (quantidadeTotalGuerreiro <= 75) {
            aumentoGuerreiro = 700;
        } else if (quantidadeTotalGuerreiro <= 100) {
            aumentoGuerreiro = 800;
        } else if (quantidadeTotalGuerreiro <= 125) {
            aumentoGuerreiro = 900;
        } else if (quantidadeTotalGuerreiro <= 150) {
            aumentoGuerreiro = 1000;
        } else if (quantidadeTotalGuerreiro <= 175) {
            aumentoGuerreiro = 1100;
        } else if (quantidadeTotalGuerreiro <= 200) {
            aumentoGuerreiro = 1200;
        } else if (quantidadeTotalGuerreiro <= 225) {
            aumentoGuerreiro = 1300;
        } else if (quantidadeTotalGuerreiro <= 250) {
            aumentoGuerreiro = 1000;
        } else if (quantidadeTotalGuerreiro <= 275) {
            aumentoGuerreiro = 1400;
        } else if (quantidadeTotalGuerreiro <= 300) {
            aumentoGuerreiro = 1500;
        } else if (quantidadeTotalGuerreiro <= 325) {
            aumentoGuerreiro = 1600;
        } else if (quantidadeTotalGuerreiro <= 350) {
            aumentoGuerreiro = 1700;
        } else if (quantidadeTotalGuerreiro <= 375) {
            aumentoGuerreiro = 1500;
        } else if (quantidadeTotalGuerreiro <= 400) {
            aumentoGuerreiro = 1800;
        } else if (quantidadeTotalGuerreiro <= 425) {
            aumentoGuerreiro = 1900;
        } else if (quantidadeTotalGuerreiro <= 450) {
            aumentoGuerreiro = 2000;
        } else if (quantidadeTotalGuerreiro <= 475) {
            aumentoGuerreiro = 2100;
        } else if (quantidadeTotalGuerreiro <= 500) {
            aumentoGuerreiro = 2200;
        } else if (quantidadeTotalGuerreiro <= 600) {
            aumentoGuerreiro = 2300;
        } else if (quantidadeTotalGuerreiro <= 700) {
            aumentoGuerreiro = 2800;
        } else if (quantidadeTotalGuerreiro <= 800) {
            aumentoGuerreiro = 3200;
        } else if (quantidadeTotalGuerreiro <= 900) {
            aumentoGuerreiro = 3600;
        } else if (quantidadeTotalGuerreiro <= 1000) {
            aumentoGuerreiro = 4000;
        } else if (quantidadeTotalGuerreiro <= 1200) {
            aumentoGuerreiro = 4800;
        } else if (quantidadeTotalGuerreiro <= 1400) {
            aumentoGuerreiro = 5600;
        } else if (quantidadeTotalGuerreiro <= 1600) {
            aumentoGuerreiro = 6400;
        } else if (quantidadeTotalGuerreiro <= 1800) {
            aumentoGuerreiro = 7200;
        } else if (quantidadeTotalGuerreiro <= 2000) {
            aumentoGuerreiro = 8000;
        } else {
            aumentoGuerreiro = 15000;
        }
        /*
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
*/
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
        
        
        if (atqueEspecial <= 1000) {
            aumentoPrecoAtaqueEspecial = 100;
        } else if (atqueEspecial <= 2000) {
            aumentoPrecoAtaqueEspecial = 200;
        } else if (atqueEspecial <= 3000) {
            aumentoPrecoAtaqueEspecial = 300;
        } else if (atqueEspecial <= 4000) {
            aumentoPrecoAtaqueEspecial = 400;
        } else if (atqueEspecial <= 5000) {
            aumentoPrecoAtaqueEspecial = 500;
        } else if (atqueEspecial <= 7000) {
            aumentoPrecoAtaqueEspecial = 700;
        } else if (atqueEspecial <= 10000) {
            aumentoPrecoAtaqueEspecial = 1000;
        } else if (atqueEspecial <= 12000) {
            aumentoPrecoAtaqueEspecial = 1300;
        } else if (atqueEspecial <= 14000) {
            aumentoPrecoAtaqueEspecial = 1600;
        } else if (atqueEspecial <= 16000) {
            aumentoPrecoAtaqueEspecial = 2000;
        } else if (atqueEspecial <= 18000) {
            aumentoPrecoAtaqueEspecial = 2500;
        } else if (atqueEspecial <= 20000) {
            aumentoPrecoAtaqueEspecial = 3000;
        } else if (atqueEspecial <= 25000) {
            aumentoPrecoAtaqueEspecial = 4000;
        } else if (atqueEspecial <= 30000) {
            aumentoPrecoAtaqueEspecial = 5000;
        } else if (atqueEspecial <= 40000) {
            aumentoPrecoAtaqueEspecial = 7000;
        } else if (atqueEspecial <= 50000) {
            aumentoPrecoAtaqueEspecial = 9000;
        } else {
            aumentoPrecoAtaqueEspecial = 15000;
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
    
    
   
    
}
