package com.boss_battle.service;


import java.math.BigDecimal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LootboxService {

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    private final Random random = new Random();

    // Preços das lootboxes
    private final long precoBasica = 1_000L;    // exemplo: 50 moedas
    private final long precoAvancada = 2_000L; // exemplo: 150 moedas
    private final long precoEspecial = 3_000L; // exemplo: 300 moedas
    private final long precoLendaria = 5_000L; // exemplo: 1000 moedas

    public String abrirLootboxPorNivel(UsuarioBossBattle usuario, String tipoLootbox) {
        final int maxBasica = 5;
        final int maxAvancada = 10;
        final int maxEspecial = 15;
        final int maxLendaria = 15;

        int quantidade;
        TipoFlecha tipoFlecha;
        long preco;

        switch(tipoLootbox.toLowerCase()) {
            case "basica" -> {
                quantidade = 1 + random.nextInt(maxBasica);
                tipoFlecha = TipoFlecha.FERRO;
                preco = precoBasica;
            }
            case "avancada" -> {
                quantidade = 1 + random.nextInt(maxAvancada);
                tipoFlecha = TipoFlecha.FOGO;
                preco = precoAvancada;
            }
            case "especial" -> {
                quantidade = 1 + random.nextInt(maxEspecial);
                tipoFlecha = TipoFlecha.VENENO;
                preco = precoEspecial;
            }
            case "lendaria" -> {
                quantidade = 1 + random.nextInt(maxLendaria);
                tipoFlecha = TipoFlecha.DIAMANTE;
                preco = precoLendaria;
            }
            default -> throw new RuntimeException("Tipo de lootbox inválido");
        }

        // ✅ Verifica se o usuário tem saldo suficiente
        if (usuario.getBossCoins().compareTo(BigDecimal.valueOf(preco)) < 0) {
            throw new RuntimeException("Saldo insuficiente para abrir a lootbox");
        }

        // 💰 Debita o valor do usuário
        usuario.setBossCoins(usuario.getBossCoins().subtract(BigDecimal.valueOf(preco)));

        // Adiciona a quantidade sorteada diretamente no inventário
        switch (tipoFlecha) {
            case FERRO -> usuario.setFlechaFerro(usuario.getFlechaFerro() + quantidade);
            case FOGO -> usuario.setFlechaFogo(usuario.getFlechaFogo() + quantidade);
            case VENENO -> usuario.setFlechaVeneno(usuario.getFlechaVeneno() + quantidade);
            case DIAMANTE -> usuario.setFlechaDiamante(usuario.getFlechaDiamante() + quantidade);
        }

        // Salva alterações
        usuarioRepository.save(usuario);
        
        //String flecha = (quantidade == 1) ? "Flecha" : "Flechas";
        String flecha;
        if (quantidade == 1) {
            flecha = "Flecha";
        } else {
            flecha = "Flechas";
        }

        return "Você recebeu " + quantidade + " " + flecha + " de " +
               tipoFlecha.name().toLowerCase();

        
/*
        return "Lootbox " + tipoLootbox + " aberta! Você recebeu " + quantidade +
               " flecha(s) de " + tipoFlecha.name().toLowerCase() +
               ". Preço: " + preco + " Boss Coins.";
               */
    }

    
 // Getters
    public long getPrecoBasica() {
        return precoBasica;
    }

    public long getPrecoAvancada() {
        return precoAvancada;
    }

    public long getPrecoEspecial() {
        return precoEspecial;
    }

    public long getPrecoLendaria() {
        return precoLendaria;
    }


  
}
