package com.boss_battle.service.recompensa_anuncio;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.dto.RecompensaAnuncioDTO;
import com.boss_battle.enums.RewardItem;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.UltimoValorRecebidoService;

@Service
public class AnuncioRecompensaService {

    @Autowired
    private UsuarioBossBattleRepository usuarioBossBattleRepository;
    @Autowired
    private UltimoValorRecebidoService ultimoValorRecebidoService;

    private final Random random = new Random();

    private static final int LIMITE_STREAK_ITEM = 15;
    private static final int COOLDOWN_MINUTOS = 5;

    private static final ZoneId ZONA_BRASIL = ZoneId.of("America/Sao_Paulo");

    //----------------------------------------------------------
    public RecompensaAnuncioDTO receberRecompensa(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioBossBattleRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        resetarStreakSeVirouODia(usuario);

        validarLimiteDiario(usuario);
        validarCooldown(usuario);

        int streakAtual = usuario.getStreakAnuncios();
        streakAtual++;

        boolean ganhouItem = false;
        String itemGanho = null;
        String imagemItem = null;
        int quantidadeItem = 0;

        long bossCoinsGanhas = sortearBossCoins(streakAtual);
      
     // 🔥 bônus especial no último (15)
     if (streakAtual == LIMITE_STREAK_ITEM) {
         bossCoinsGanhas += 20;
     }

        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoinsGanhas)));
        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, BigDecimal.valueOf(bossCoinsGanhas));

        String descricao = "Você ganhou " + bossCoinsGanhas + " BOSS Coins.";

        if (streakAtual >= LIMITE_STREAK_ITEM) {

            ItemAnuncio item = sortearItemEspecialAnuncio();

            aplicarItem(usuario, item);

            ganhouItem = true;
            itemGanho = item.getNome();
            imagemItem = item.getImagemUrl();
            quantidadeItem = item.getQuantidade();

            descricao += "Bônus: " + item.getQuantidade() + "x " + item.getNome();

            streakAtual = LIMITE_STREAK_ITEM;
        }

        usuario.setStreakAnuncios(streakAtual);
        usuario.setUltimoAnuncioAssistido(LocalDateTime.now(ZONA_BRASIL));

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
    }//--->receberRecompensa
    
    //--------------------------------------------------------------
    private void resetarStreakSeVirouODia(UsuarioBossBattle usuario) {

        LocalDateTime ultimo = usuario.getUltimoAnuncioAssistido();

        // nunca assistiu anúncio
        if (ultimo == null) return;

        // pega data atual (Brasil)
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        // se for dia diferente, reseta
        if (!ultimo.toLocalDate().isEqual(hoje)) {
            usuario.setStreakAnuncios(0);
        }
    }//--->resetarStreakSeVirouODia
    //---------------------------------------------------------
    private void validarLimiteDiario(UsuarioBossBattle usuario) {

        if (usuario.getStreakAnuncios() >= LIMITE_STREAK_ITEM) {

            LocalDateTime ultimo = usuario.getUltimoAnuncioAssistido();

            if (ultimo != null && ultimo.toLocalDate().isEqual(LocalDate.now())) {
                throw new RuntimeException("Limite diário de anúncios atingido.");
            }
        }
    }//--->validarLimiteDiario
    
    //-----------------------------------------------------
    private void validarCooldown(UsuarioBossBattle usuario) {

        LocalDateTime ultimoAnuncio = usuario.getUltimoAnuncioAssistido();

        if (ultimoAnuncio != null &&
                ultimoAnuncio.isAfter(LocalDateTime.now().minusMinutes(COOLDOWN_MINUTOS))) {

            throw new RuntimeException("Aguarde " + COOLDOWN_MINUTOS + " minutos para assistir outro anúncio.");
        }
    }//--->validarCooldown
    
//---------------------------------
    private long sortearBossCoins(int streakAtual) {

        int min = 50;
        int max = 200;

        double fator = (double) streakAtual / LIMITE_STREAK_ITEM;

        int baseProgressivo = (int) (min + (max - min) * fator);

        int variacao = random.nextInt(21) - 10; // -10 até +10

        int valor = baseProgressivo + variacao;

        if (valor < min) valor = min;
        if (valor > max) valor = max;

        return valor;
    }//--->sortearBossCoins

    private ItemAnuncio sortearItemEspecialAnuncio() {

        int roll = random.nextInt(100);

        if (roll < 30) {
            return new ItemAnuncio(
                    RewardItem.ESPADA_FLANEJANTE,
                    "Espada Flanejante",
                    1,
                    "icones/espada_flanejante.webp"
            );

        } else if (roll < 50) {
            return new ItemAnuncio(
                    RewardItem.MACHADO_DILACERADOR,
                    "Machado Dilacerador",
                    1,
                    "icones/machado_dilacerador.webp"
            );

        } else if (roll < 70) {
            return new ItemAnuncio(
                    RewardItem.ESCUDO_PRIMORDIAL,
                    "Escudo Primordial",
                    1,
                    "icones/escudo_primordial.webp"
            );

        } else if (roll < 80) {
            return new ItemAnuncio(
                    RewardItem.POCAO_VIGOR,
                    "poção de vigor",
                    1,
                    "icones/pocao_vigor.webp"
            );

        } 
        
        else {
            return new ItemAnuncio(
                    RewardItem.ARCO_CELESTIAL,
                    "Arco Celestial",
                    1,
                    "icones/arco.webp"
            );
        }
    }//--->sortearItemEspecialAnuncio
    
    //--------------------------------------------------------------------------------------
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
        
        if (item.getRewardItem() == RewardItem.POCAO_VIGOR) {
            usuario.setPocaoVigor(usuario.getPocaoVigor() + item.getQuantidade());
        }
        
     

    }//--->aplicarItem
    
    //--------------------------------------------------------------
    public Map<String, Object> verificarStatusAnuncio(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioBossBattleRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        LocalDateTime ultimo = usuario.getUltimoAnuncioAssistido();

        // 🔥 reset diário seguro
        if (ultimo == null || 
            !ultimo.toLocalDate().isEqual(LocalDate.now(ZONA_BRASIL))) {

            usuario.setStreakAnuncios(0);
            usuarioBossBattleRepository.save(usuario);
        }

        int streakAtual = usuario.getStreakAnuncios();
        boolean limiteAtingido = streakAtual >= LIMITE_STREAK_ITEM;

        ZonedDateTime agoraBrasil = ZonedDateTime.now(ZONA_BRASIL);
        ZonedDateTime proximaMeiaNoite = agoraBrasil.toLocalDate()
                .plusDays(1)
                .atStartOfDay(ZONA_BRASIL);

        long segundosAteReset = Duration.between(agoraBrasil, proximaMeiaNoite).getSeconds();

        return Map.of(
                "success", true,
                "streakAtual", streakAtual,
                "limite", LIMITE_STREAK_ITEM,
                "limiteAtingido", limiteAtingido,
                "podeAssistir", !limiteAtingido,
                "segundosAteReset", segundosAteReset
        );
    }//--->verificarStatusAnuncio
    
    
}