package com.boss_battle.service.cacador_reconpensas.ruinas_perdidas;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.cacador_reconpensasDTO.RuinasPerdidasResponse;
import com.boss_battle.enums.enumscacador_reconpensas.PremioRuinasPerdidas;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ReferidosRecompensaService;
import com.boss_battle.service.UltimoValorRecebidoService;

@Service
public class RuinasPerdidasService {

    private static final int LIMITE_TENTATIVAS_DIARIAS = 10;
    private static final int COOLDOWN_MINUTOS = 5;

    private final ReferidosRecompensaService referidosService;
    private final UltimoValorRecebidoService ultimoValorRecebidoService;
    private final UsuarioBossBattleRepository usuarioRepository;
    private final Random random = new Random();

    public RuinasPerdidasService(UsuarioBossBattleRepository usuarioRepository,
    		UltimoValorRecebidoService ultimoValorRecebidoService,
    		ReferidosRecompensaService referidosService) {
        this.usuarioRepository = usuarioRepository;
        this.ultimoValorRecebidoService = ultimoValorRecebidoService;
        this.referidosService = referidosService;
    }

    @Transactional
    public RuinasPerdidasResponse explorarRuinas(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposRuinas(usuario);

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        resetarTentativasSeNovoDia(usuario, hoje);

        if (usuario.getTentativasRuinasHoje() >= LIMITE_TENTATIVAS_DIARIAS) {

            long segundosAteAmanha = calcularSegundosAteAmanha(agora);

            return new RuinasPerdidasResponse(
                    false,
                    "Você já explorou todas as ruínas disponíveis hoje. Volte amanhã.",
                    null,
                    "LIMITE_DIARIO",
                    0L,
                    usuario.getBossCoins(),
                    usuario.getAtaqueBase() ,
                    0,
                    segundosAteAmanha,
                    true
            );
        }

        int tentativasRestantes = Math.max(
                0,
                LIMITE_TENTATIVAS_DIARIAS - usuario.getTentativasRuinasHoje()
        );

        if (estaEmCooldown(usuario, agora)) {

            long segundosRestantes = calcularSegundosRestantes(usuario, agora);

            return new RuinasPerdidasResponse(
                    false,
                    "Aguarde " + formatarTempo(segundosRestantes) + " para explorar novamente.",
                    null,
                    "COOLDOWN",
                    0L,
                    usuario.getBossCoins(),
                    usuario.getAtaqueBase() ,
                    tentativasRestantes,
                    segundosRestantes,
                    true
            );
        }

        PremioRuinasPerdidas premio = sortearPremio(usuario);
        Long valorGanho = aplicarPremio(usuario, premio);

        usuario.setTentativasRuinasHoje(usuario.getTentativasRuinasHoje() + 1);
        usuario.setTotalExploracoesRuinas(usuario.getTotalExploracoesRuinas() + 1);
        usuario.setDataUltimaExploracaoRuinas(hoje);
        usuario.setUltimaExploracaoRuinasEm(agora);

        if ("BOSS_COINS".equals(premio.getTipo())) {

            ultimoValorRecebidoService.setUltimoValorRecebido(
                    usuario,
                    BigDecimal.valueOf(valorGanho)
            );
            
            referidosService.adicionarGanho(usuario, BigDecimal.valueOf(valorGanho));
        }
        
        usuarioRepository.save(usuario);

        int restantesDepois = Math.max(
                0,
                LIMITE_TENTATIVAS_DIARIAS - usuario.getTentativasRuinasHoje()
        );

        Long cooldownRetorno = restantesDepois <= 0
                ? calcularSegundosAteAmanha(agora)
                : COOLDOWN_MINUTOS * 60L;

        boolean emCooldownRetorno = restantesDepois <= 0;

        String mensagem = restantesDepois <= 0
                ? premio.getDescricao() + " Você concluiu todas as explorações de hoje. Volte amanhã."
                : premio.getDescricao();

        return new RuinasPerdidasResponse(
                true,
                mensagem,
                premio.getNome(),
                premio.getTipo(),
                valorGanho,
                usuario.getBossCoins(),
                usuario.getAtaqueBase() ,
                restantesDepois,
                cooldownRetorno,
                emCooldownRetorno
        );
    }

    private void resetarTentativasSeNovoDia(UsuarioBossBattle usuario, LocalDate hoje) {

        if (usuario.getDataUltimaExploracaoRuinas() == null ||
                !usuario.getDataUltimaExploracaoRuinas().isEqual(hoje)) {

            usuario.setTentativasRuinasHoje(0);
            usuario.setDataUltimaExploracaoRuinas(hoje);
            usuario.setUltimaExploracaoRuinasEm(null);
        }
    }

    private boolean estaEmCooldown(UsuarioBossBattle usuario, LocalDateTime agora) {

        if (usuario.getUltimaExploracaoRuinasEm() == null) {
            return false;
        }

        LocalDateTime liberadoEm =
                usuario.getUltimaExploracaoRuinasEm().plusMinutes(COOLDOWN_MINUTOS);

        return agora.isBefore(liberadoEm);
    }

    private long calcularSegundosRestantes(UsuarioBossBattle usuario, LocalDateTime agora) {

        LocalDateTime liberadoEm =
                usuario.getUltimaExploracaoRuinasEm().plusMinutes(COOLDOWN_MINUTOS);

        return Duration.between(agora, liberadoEm).getSeconds();
    }

    private long calcularSegundosAteAmanha(LocalDateTime agora) {

        LocalDateTime proximaMeiaNoite = agora.toLocalDate()
                .plusDays(1)
                .atStartOfDay();

        return Duration.between(agora, proximaMeiaNoite).getSeconds();
    }

    private String formatarTempo(long segundos) {

        long minutos = segundos / 60;
        long restoSegundos = segundos % 60;

        if (minutos > 0) {
            return minutos + "m " + restoSegundos + "s";
        }

        return restoSegundos + "s";
    }
    private PremioRuinasPerdidas sortearPremio(UsuarioBossBattle usuario) {

        int proximaTentativa = usuario.getTentativasRuinasHoje() + 1;

        if (proximaTentativa == 10) {

            if (usuario.getAtaqueBase() >= 1000L) {
                return PremioRuinasPerdidas.BOSS_COINS_GRANDE;
            }

            int especial = random.nextInt(100) + 1;

            if (especial <= 5) {
                return PremioRuinasPerdidas.ATAQUE_ESPECIAL_PLUS_1;
            }

            if (especial <= 55) {
                return PremioRuinasPerdidas.XP_GRANDE;
            }

            if (especial <= 80) {
                return PremioRuinasPerdidas.BOSS_COINS_GRANDE;
            }

            return PremioRuinasPerdidas.NADA_ENCONTRADO;
        }

        int chance = random.nextInt(1000) + 1;

        if (chance <= 500) {
            return PremioRuinasPerdidas.BOSS_COINS_PEQUENO;
        }

        if (chance <= 750) {
            return PremioRuinasPerdidas.XP_PEQUENO;
        }

        if (chance <= 950) {
            return PremioRuinasPerdidas.BOSS_COINS_MEDIO;
        }

        return PremioRuinasPerdidas.NADA_ENCONTRADO;
    }
    private Long aplicarPremio(UsuarioBossBattle usuario, PremioRuinasPerdidas premio) {

        switch (premio) {

            case BOSS_COINS_PEQUENO:
                long bossCoinsPequeno = random.nextInt(6) + 5;
                adicionarBossCoins(usuario, bossCoinsPequeno);
                return bossCoinsPequeno;

            case BOSS_COINS_MEDIO:
                long bossCoinsMedio = random.nextInt(11) + 15;
                adicionarBossCoins(usuario, bossCoinsMedio);
                return bossCoinsMedio;

            case BOSS_COINS_GRANDE:
                long bossGrande = random.nextInt(26) + 25;
                adicionarBossCoins(usuario, bossGrande);
                return bossGrande;

            case XP_PEQUENO:
                long xpGanho = random.nextInt(6) + 5; // 5 a 10
                usuario.setExp(usuario.getExp() + xpGanho);
                return xpGanho;

            case XP_GRANDE:
                long xpGrande = random.nextInt(11) + 10; // 10 a 20
                usuario.setExp(usuario.getExp() + xpGrande);
                return xpGrande;

            case ATAQUE_ESPECIAL_PLUS_1:
                usuario.setAtaqueBase(usuario.getAtaqueBase() + 1L);
                return 1L;

            case NADA_ENCONTRADO:
            default:
                return 0L;
        }
    }

    private void adicionarBossCoins(UsuarioBossBattle usuario, Long valor) {
        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(valor)));
    }

    public Map<String, Object> verificarStatusRuinas(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposRuinas(usuario);

        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();

        if (usuario.getDataUltimaExploracaoRuinas() == null ||
                !usuario.getDataUltimaExploracaoRuinas().isEqual(hoje)) {

            usuario.setTentativasRuinasHoje(0);
            usuario.setDataUltimaExploracaoRuinas(hoje);
            usuario.setUltimaExploracaoRuinasEm(null);
            usuarioRepository.save(usuario);
        }

        int tentativasHoje = usuario.getTentativasRuinasHoje();

        int tentativasRestantes = Math.max(
                0,
                LIMITE_TENTATIVAS_DIARIAS - tentativasHoje
        );

        boolean limiteAtingido = tentativasHoje >= LIMITE_TENTATIVAS_DIARIAS;

        boolean emCooldown = false;
        long segundosRestantes = 0L;
        long segundosAteReset = calcularSegundosAteAmanha(agora);

        if (limiteAtingido) {
            segundosRestantes = segundosAteReset;
        } else if (usuario.getUltimaExploracaoRuinasEm() != null) {

            LocalDateTime liberadoEm =
                    usuario.getUltimaExploracaoRuinasEm().plusMinutes(COOLDOWN_MINUTOS);

            if (agora.isBefore(liberadoEm)) {
                emCooldown = true;
                segundosRestantes = Duration.between(agora, liberadoEm).getSeconds();
            }
        }

        return Map.of(
                "success", true,
                "tentativasHoje", tentativasHoje,
                "tentativasRestantes", tentativasRestantes,
                "limite", LIMITE_TENTATIVAS_DIARIAS,
                "limiteAtingido", limiteAtingido,
                "emCooldown", emCooldown || limiteAtingido,
                "segundosRestantes", segundosRestantes,
                "segundosAteReset", segundosAteReset,
                "podeEntrar", !limiteAtingido && !emCooldown
        );
    }

    private void protegerCamposRuinas(UsuarioBossBattle usuario) {

        if (usuario.getTentativasRuinasHoje() == null) {
            usuario.setTentativasRuinasHoje(0);
        }

        if (usuario.getTotalExploracoesRuinas() == null) {
            usuario.setTotalExploracoesRuinas(0L);
        }

        if (usuario.getAtaqueEspecialBonus() == null) {
            usuario.setAtaqueEspecialBonus(0L);
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }
        
       
    }
}