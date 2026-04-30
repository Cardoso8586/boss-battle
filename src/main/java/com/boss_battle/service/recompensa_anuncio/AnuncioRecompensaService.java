package com.boss_battle.service.recompensa_anuncio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.dto.RecompensaAnuncioDTO;
import com.boss_battle.enums.RewardItem;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
public class AnuncioRecompensaService {

    @Autowired
    private UsuarioBossBattleRepository usuarioBossBattleRepository;

    private final Random random = new Random();

    private static final int LIMITE_STREAK_ITEM = 10;
    private static final int COOLDOWN_MINUTOS = 5;

    public RecompensaAnuncioDTO receberRecompensa(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioBossBattleRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        validarCooldown(usuario);

        int streakAtual = usuario.getStreakAnuncios();
        streakAtual++;

        boolean ganhouItem = false;
        String itemGanho = null;
        String imagemItem = null;
        int quantidadeItem = 0;

        long bossCoinsGanhas = sortearBossCoins();

        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoinsGanhas)));

        String descricao = "💰 Você ganhou " + bossCoinsGanhas + " BOSS Coins.";

        if (streakAtual >= LIMITE_STREAK_ITEM) {

            ItemAnuncio item = sortearItemEspecialAnuncio();

            aplicarItem(usuario, item);

            ganhouItem = true;
            itemGanho = item.getNome();
            imagemItem = item.getImagemUrl();
            quantidadeItem = item.getQuantidade();

            descricao += " 🎁 Bônus do 10º anúncio: " + item.getQuantidade() + "x " + item.getNome();

            streakAtual = 0;
        }

        usuario.setStreakAnuncios(streakAtual);
        usuario.setUltimoAnuncioAssistido(LocalDateTime.now());

        usuarioBossBattleRepository.save(usuario);

        return new RecompensaAnuncioDTO(
                true,
                "ANUNCIO",
                bossCoinsGanhas,
                descricao,
                streakAtual,
                ganhouItem,
                itemGanho,
                imagemItem,
                quantidadeItem
        );
    }

    private void validarCooldown(UsuarioBossBattle usuario) {

        LocalDateTime ultimoAnuncio = usuario.getUltimoAnuncioAssistido();

        if (ultimoAnuncio != null &&
                ultimoAnuncio.isAfter(LocalDateTime.now().minusMinutes(COOLDOWN_MINUTOS))) {

            throw new RuntimeException("Aguarde " + COOLDOWN_MINUTOS + " minutos para assistir outro anúncio.");
        }
    }

    private long sortearBossCoins() {
        return 10 + random.nextInt(41); 
    }

    private ItemAnuncio sortearItemEspecialAnuncio() {

        int roll = random.nextInt(100);

        if (roll < 30) {
            return new ItemAnuncio(
                    RewardItem.ESPADA_FLANEJANTE,
                    "Espada Flanejante",
                    1,
                    "icones/espada_flanejante.webp"
            );

        } else if (roll < 60) {
            return new ItemAnuncio(
                    RewardItem.MACHADO_DILACERADOR,
                    "Machado Dilacerador",
                    1,
                    "icones/machado_dilacerador.webp"
            );

        } else if (roll < 80) {
            return new ItemAnuncio(
                    RewardItem.ESCUDO_PRIMORDIAL,
                    "Escudo Primordial",
                    1,
                    "icones/escudo_primordial.webp"
            );

        } else {
            return new ItemAnuncio(
                    RewardItem.ARCO_CELESTIAL,
                    "Arco Celestial",
                    1,
                    "icones/arco.webp"
            );
        }
    }

    private void aplicarItem(UsuarioBossBattle usuario, ItemAnuncio item) {

        if (item.getRewardItem() == RewardItem.ESPADA_FLANEJANTE) {
            usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + item.getQuantidade());
        }

        if (item.getRewardItem() == RewardItem.MACHADO_DILACERADOR) {
            usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + item.getQuantidade());
        }

        if (item.getRewardItem() == RewardItem.ESCUDO_PRIMORDIAL) {
            usuario.setEscudoPrimordial(usuario.getEscudoPrimordial() + item.getQuantidade());
        }

        if (item.getRewardItem() == RewardItem.ARCO_CELESTIAL) {
            usuario.setInventarioArco(usuario.getInventarioArco() + item.getQuantidade());
        }
    }
}