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
    // =====================================
    // AUMENTO FIXO POR COMPRA
    // =====================================
    private static final long AUMENTO_PRECO_GUERREIROS = 100L;
    private static final long AUMENTO_PRECO_ENERGIA = 100L;
    private static final long AUMENTO_PRECO_ATAQUE_ESPECIAL = 150L;

    /**
     * ⚠️ ESTE MÉTODO DEVE SER CHAMADO
     * SOMENTE APÓS UMA COMPRA
     */
    
    //---> atualizar preço do guerreiro
    public void atualizarPrecoGuerreiro(UsuarioBossBattle usuario, int quantidade) {

        // 🔒 Usa sempre o preço atual como base
        long precoGuerreirosAtual = usuario.getPrecoGuerreiros();
        // 🔼 aumento fixo e permanente
        usuario.setPrecoGuerreiros(precoGuerreirosAtual + (quantidade*AUMENTO_PRECO_GUERREIROS));

       
    }

    
    //--->atualizar preço do vigor
    public void atualizarPrecoVigor(UsuarioBossBattle usuario, int quantidade) {

        // 🔒 Usa sempre o preço atual como base
        long precoEnergiaAtual = usuario.getPrecoEnergia();
        usuario.setPrecoEnergia(precoEnergiaAtual + (quantidade*AUMENTO_PRECO_ENERGIA));
    }

    
    //---> atualizar preço ataque especial
    public void atualizarPrecoAtaqueEspecial(UsuarioBossBattle usuario, int quantidade) {

        // 🔒 Usa sempre o preço atual como base
        long precoAtaqueAtual = usuario.getPrecoAtaqueEspecial();

        // 🔼 aumento fixo e permanente
        usuario.setPrecoAtaqueEspecial(precoAtaqueAtual + ( quantidade* AUMENTO_PRECO_ATAQUE_ESPECIAL) );
    }

    
    //======================================
    
    public long getPrecoBasePorcaovigor() {
        return PRECO_BASE_PORCAOVIGOR;
    }
    
    //======================================
    
    public long getPrecoBaseEspadaFlanejante() {
        return PRECO_BASE_ESPADA_FLANEJANTE;
    }
    
    
    
}
