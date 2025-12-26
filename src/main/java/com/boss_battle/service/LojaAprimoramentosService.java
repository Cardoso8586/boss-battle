package com.boss_battle.service;

import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;

@Service
public class LojaAprimoramentosService {



    /**
     * Calcula e salva os preços da loja no usuário
     */
    private static final long PRECO_BASE_PORCAOVIGOR = 10_000L;// VALOR FIXO DO AUTOVIGOR
    private static final long PRECO_BASE_GUERREIROS = 1000L;
    private static final long PRECO_BASE_ENERGIA = 1000L;

    private static final long PRECO_BASE_ATAQUE_ESPECIAL = 1000L;

    public void atualizarPrecosLoja(UsuarioBossBattle usuario) {

        long nivel = usuario.getNivel();
        long guerreiros = usuario.getGuerreiros();
        long energiaBase = usuario.getEnergiaGuerreirosPadrao();
      
        long ataqueEspecial = usuario.getAtaqueBase();
      
        long precoGuerreiros =PRECO_BASE_GUERREIROS + (guerreiros * 5L) + (nivel * 20L);

        long precoEnergia = PRECO_BASE_ENERGIA + (energiaBase * 2L) + (nivel * 10L);

       
        long precoAtaqueEspecial = PRECO_BASE_ATAQUE_ESPECIAL + (ataqueEspecial * 10L) + (nivel * 30L);

        usuario.setPrecoGuerreiros(precoGuerreiros);
        usuario.setPrecoEnergia(precoEnergia);

        usuario.setPrecoAtaqueEspecial(precoAtaqueEspecial);
        
    }//---> atualizarPrecosLoja
    
    //=============================================================
                    //PREÇO FIXO DA POÇÃO VIGOR
    //=============================================================

	public long getPrecoBasePorcaovigor() {
		return PRECO_BASE_PORCAOVIGOR;
	}

}
