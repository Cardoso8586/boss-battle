
package com.boss_battle.service.cacador_reconpensas.masmorra_sombria;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.enums.enumscacador_reconpensas.InimigoMasmorra;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.repository.UsuarioGuerreiroRepository;
import com.boss_battle.service.UltimoValorRecebidoService;

@Service
public class MasmorraSombriaService {

	private static final int CUSTO_VIGOR_ENTRADA = 50;
	private static final int LIMITE_ENTRADAS_CICLO = 10;
	private static final long COOLDOWN_MINUTOS = 60L;

    private final UsuarioBossBattleRepository usuarioRepository;
    private final UsuarioGuerreiroRepository usuarioGuerreiroRepository;
    private final UltimoValorRecebidoService ultimoValorRecebidoService;
    private final Random random = new Random();

    public MasmorraSombriaService(
            UsuarioBossBattleRepository usuarioRepository,
            UsuarioGuerreiroRepository usuarioGuerreiroRepository,
            UltimoValorRecebidoService ultimoValorRecebidoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioGuerreiroRepository = usuarioGuerreiroRepository;
        this.ultimoValorRecebidoService = ultimoValorRecebidoService;
    }

    @Transactional
    public Map<String, Object> entrarMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarCicloSeCooldownTerminou(usuario);

        if (cooldownAtivo(usuario)) {
            return respostaCooldown(usuario);
        }

        if (Boolean.TRUE.equals(usuario.getMasmorraEmCombate())) {
            return respostaCombate(usuario, "Você já está em combate na Masmorra Sombria.");
        }

        if (usuario.getTentativasMasmorraHoje() >= LIMITE_ENTRADAS_CICLO) {
            aplicarCooldown(usuario);
            usuarioRepository.save(usuario);
            return respostaCooldown(usuario);
        }

        if (usuario.getEnergiaGuerreiros() < CUSTO_VIGOR_ENTRADA) {
            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_VIGOR"),
                    Map.entry("mensagem", "Você precisa de pelo menos " + CUSTO_VIGOR_ENTRADA + " de vigor para entrar na Masmorra."),
                    Map.entry("vigorNecessario", CUSTO_VIGOR_ENTRADA),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO)
            );
        }

        usuario.setEnergiaGuerreiros(usuario.getEnergiaGuerreiros() - CUSTO_VIGOR_ENTRADA);

        InimigoMasmorra inimigo = sortearInimigo();

        long hpMax = sortearHpInimigo(usuario, inimigo);
        long ataqueInimigo = sortearAtaqueInimigo(usuario, inimigo);

        usuario.setMasmorraEmCombate(true);
        usuario.setMasmorraInimigoAtual(inimigo.name());
        usuario.setMasmorraInimigoHpMax(hpMax);
        usuario.setMasmorraInimigoHpAtual(hpMax);
        usuario.setMasmorraInimigoAtaque(ataqueInimigo);

        usuario.setTentativasMasmorraHoje(usuario.getTentativasMasmorraHoje() + 1);
        usuario.setTotalExploracoesMasmorra(usuario.getTotalExploracoesMasmorra() + 1);
        usuario.setDataUltimaExploracaoMasmorra(LocalDate.now());

        if (usuario.getTentativasMasmorraHoje() >= LIMITE_ENTRADAS_CICLO) {
            aplicarCooldown(usuario);
        }

        usuarioRepository.save(usuario);

        return respostaCombate(usuario, "Você entrou na Masmorra Sombria e encontrou " + inimigo.getNome() + ".");
    }
    
    
    @Transactional
    public Map<String, Object> atacarMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarCicloSeCooldownTerminou(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())
                || usuario.getMasmorraInimigoAtual() == null) {

            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_COMBATE"),
                    Map.entry("mensagem", "Você não está em combate na Masmorra.")
            );
        }

        InimigoMasmorra inimigo = InimigoMasmorra.valueOf(usuario.getMasmorraInimigoAtual());

        long danoUsuario = calcularDanoUsuario(usuario);
        long custoVigorAtaque = Math.max(1L, danoUsuario / 2);

        long vigorDepoisDoAtaque = Math.max(0L, usuario.getEnergiaGuerreiros() - custoVigorAtaque);
        usuario.setEnergiaGuerreiros(vigorDepoisDoAtaque);

        long hpAntes = Math.max(0L, usuario.getMasmorraInimigoHpAtual());
        long hpDepois = Math.max(0L, hpAntes - danoUsuario);
        usuario.setMasmorraInimigoHpAtual(hpDepois);

        if (vigorDepoisDoAtaque <= 0 && hpDepois > 0) {

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            usuarioRepository.save(usuario);

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("status", "DERROTA"),
                    Map.entry("mensagem", "Você atacou, causou " + danoUsuario + " de dano, mas ficou sem vigor e foi derrotado."),
                    Map.entry("danoUsuario", danoUsuario),
                    Map.entry("danoRecebido", 0L),
                    Map.entry("vigorGastoAtaque", custoVigorAtaque),
                    Map.entry("inimigoNome", inimigo.getNome()),
                    Map.entry("inimigoImagem", inimigo.getImagem()),
                    Map.entry("inimigoHpAtual", hpDepois),
                    Map.entry("inimigoHpMax", hpMaxAntesLimpar),
                    Map.entry("inimigoAtaque", ataqueAntesLimpar),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO)
            );
        }

        if (hpDepois <= 0) {

            Map<String, Object> recompensa = aplicarRecompensa(usuario, inimigo);

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            usuarioRepository.save(usuario);

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("status", "VITORIA"),
                    Map.entry("mensagem", "Você derrotou " + inimigo.getNome()),
                    Map.entry("danoUsuario", danoUsuario),
                    Map.entry("danoRecebido", 0L),
                    Map.entry("vigorGastoAtaque", custoVigorAtaque),
                    Map.entry("inimigoNome", inimigo.getNome()),
                    Map.entry("inimigoImagem", inimigo.getImagem()),
                    Map.entry("inimigoHpAtual", 0L),
                    Map.entry("inimigoHpMax", hpMaxAntesLimpar),
                    Map.entry("inimigoAtaque", ataqueAntesLimpar),
                    Map.entry("recompensa", recompensa),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO)
            );
        }

        long danoRecebido = calcularDanoNoVigor(usuario);
        long vigorDepoisRevidar = Math.max(0L, usuario.getEnergiaGuerreiros() - danoRecebido);
        usuario.setEnergiaGuerreiros(vigorDepoisRevidar);

        if (vigorDepoisRevidar <= 0) {

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            usuarioRepository.save(usuario);

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("status", "DERROTA"),
                    Map.entry("mensagem", "Você causou " + danoUsuario + " de dano, mas " + inimigo.getNome() + " zerou seu vigor."),
                    Map.entry("danoUsuario", danoUsuario),
                    Map.entry("danoRecebido", danoRecebido),
                    Map.entry("vigorGastoAtaque", custoVigorAtaque),
                    Map.entry("inimigoNome", inimigo.getNome()),
                    Map.entry("inimigoImagem", inimigo.getImagem()),
                    Map.entry("inimigoHpAtual", hpDepois),
                    Map.entry("inimigoHpMax", hpMaxAntesLimpar),
                    Map.entry("inimigoAtaque", ataqueAntesLimpar),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO)
            );
        }

        usuarioRepository.save(usuario);

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "COMBATE"),
                Map.entry("mensagem", "Você atacou, gastou " + custoVigorAtaque + " de vigor e causou " + danoUsuario + " de dano. " + inimigo.getNome() + " revidou e causou " + danoRecebido + " de dano no seu vigor."),
                Map.entry("danoUsuario", danoUsuario),
                Map.entry("danoRecebido", danoRecebido),
                Map.entry("vigorGastoAtaque", custoVigorAtaque),
                Map.entry("inimigoNome", inimigo.getNome()),
                Map.entry("inimigoImagem", inimigo.getImagem()),
                Map.entry("inimigoHpAtual", hpDepois),
                Map.entry("inimigoHpMax", usuario.getMasmorraInimigoHpMax()),
                Map.entry("inimigoAtaque", usuario.getMasmorraInimigoAtaque()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_CICLO)
        );
    }
    
    
    @Transactional
    public Map<String, Object> inimigoAtacarMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarCicloSeCooldownTerminou(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())
                || usuario.getMasmorraInimigoAtual() == null) {

            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_COMBATE"),
                    Map.entry("mensagem", "Nenhum combate ativo.")
            );
        }

        InimigoMasmorra inimigo = InimigoMasmorra.valueOf(usuario.getMasmorraInimigoAtual());

        long danoRecebido = calcularDanoNoVigor(usuario);
        long vigorDepois = Math.max(0L, usuario.getEnergiaGuerreiros() - danoRecebido);
        usuario.setEnergiaGuerreiros(vigorDepois);

        if (vigorDepois <= 0) {

            long hpAntesLimpar = usuario.getMasmorraInimigoHpAtual();
            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            usuarioRepository.save(usuario);

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("status", "DERROTA"),
                    Map.entry("mensagem", inimigo.getNome() + " zerou seu vigor."),
                    Map.entry("danoUsuario", 0L),
                    Map.entry("danoRecebido", danoRecebido),
                    Map.entry("inimigoNome", inimigo.getNome()),
                    Map.entry("inimigoImagem", inimigo.getImagem()),
                    Map.entry("inimigoHpAtual", hpAntesLimpar),
                    Map.entry("inimigoHpMax", hpMaxAntesLimpar),
                    Map.entry("inimigoAtaque", ataqueAntesLimpar),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO)
            );
        }

        usuarioRepository.save(usuario);

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "COMBATE"),
                Map.entry("mensagem", inimigo.getNome() + " atacou e causou " + danoRecebido + " de dano no seu vigor."),
                Map.entry("danoUsuario", 0L),
                Map.entry("danoRecebido", danoRecebido),
                Map.entry("inimigoNome", inimigo.getNome()),
                Map.entry("inimigoImagem", inimigo.getImagem()),
                Map.entry("inimigoHpAtual", usuario.getMasmorraInimigoHpAtual()),
                Map.entry("inimigoHpMax", usuario.getMasmorraInimigoHpMax()),
                Map.entry("inimigoAtaque", usuario.getMasmorraInimigoAtaque()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_CICLO)
        );
    }

    public Map<String, Object> statusMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarCicloSeCooldownTerminou(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())
                || usuario.getMasmorraInimigoAtual() == null) {

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("emCombate", false),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_CICLO),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
            );
        }

        return respostaCombate(usuario, "Combate em andamento.");
    }

    @Transactional
    public Map<String, Object> fugirMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarCicloSeCooldownTerminou(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())) {
            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_COMBATE"),
                    Map.entry("mensagem", "Nenhum combate ativo.")
            );
        }

        limparCombate(usuario);
        usuarioRepository.save(usuario);

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "FUGIU"),
                Map.entry("mensagem", "Você fugiu da Masmorra Sombria."),
                Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_CICLO)
        );
    }
    
    public long calcularAtaqueBaseTotal(UsuarioBossBattle usuario) {
        long ataqueBaseJogador = Math.max(0L, usuario.getAtaqueBase());
        return ataqueBaseJogador + calcularAtaqueBaseElite(usuario);
    }

    public long calcularAtaqueBaseElite(UsuarioBossBattle usuario) {
        return usuarioGuerreiroRepository
                .findByUsuario(usuario)
                .stream()
                .mapToLong(ug -> {
                    if (ug.getQuantidade() == null) return 0L;
                    if (ug.getGuerreiro() == null) return 0L;
                    if (ug.getGuerreiro().getDanoBase() == null) return 0L;

                    return ug.getQuantidade() * ug.getGuerreiro().getDanoBase();
                })
                .sum();
    }
    private void aplicarCooldown(UsuarioBossBattle usuario) {
        usuario.setMasmorraCooldownAte(LocalDateTime.now().plusMinutes(COOLDOWN_MINUTOS));
    }

    private boolean cooldownAtivo(UsuarioBossBattle usuario) {
        return usuario.getMasmorraCooldownAte() != null
                && usuario.getMasmorraCooldownAte().isAfter(LocalDateTime.now());
    }

    private long segundosRestantesCooldown(UsuarioBossBattle usuario) {
        if (!cooldownAtivo(usuario)) {
            return 0L;
        }

        return Math.max(
                0L,
                Duration.between(LocalDateTime.now(), usuario.getMasmorraCooldownAte()).getSeconds()
        );
    }

    private void resetarCicloSeCooldownTerminou(UsuarioBossBattle usuario) {
        if (usuario.getMasmorraCooldownAte() != null
                && !usuario.getMasmorraCooldownAte().isAfter(LocalDateTime.now())) {

            usuario.setTentativasMasmorraHoje(0);
            usuario.setMasmorraCooldownAte(null);
            usuario.setDataUltimaExploracaoMasmorra(LocalDate.now());
        }
    }

    private Map<String, Object> respostaCooldown(UsuarioBossBattle usuario) {
        long segundos = segundosRestantesCooldown(usuario);
        long minutos = Math.max(1L, (long) Math.ceil(segundos / 60.0));

        return Map.ofEntries(
                Map.entry("sucesso", false),
                Map.entry("status", "COOLDOWN"),
                Map.entry("mensagem", "A Masmorra está em descanso após " + LIMITE_ENTRADAS_CICLO + " entradas. Aguarde aproximadamente " + minutos + " minuto(s)."),
                Map.entry("cooldownAtivo", true),
                Map.entry("cooldownSegundosRestantes", segundos),
                Map.entry("cooldownAte", usuario.getMasmorraCooldownAte()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_CICLO),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
        );
    }

    private InimigoMasmorra sortearInimigo() {
        int chance = random.nextInt(1000) + 1;

        // 1 - 120 = 12%
        if (chance <= 120) return InimigoMasmorra.GOBLIN;

        // 121 - 220 = 10%
        if (chance <= 220) return InimigoMasmorra.ESQUELETO;

        // 221 - 300 = 8%
        if (chance <= 300) return InimigoMasmorra.GUARDIAO;

        // 301 - 370 = 7%
        if (chance <= 370) return InimigoMasmorra.GNOMO_X;

        // 371 - 430 = 6%
        if (chance <= 430) return InimigoMasmorra.GOBLIN_Z;

        // 431 - 480 = 5%
        if (chance <= 480) return InimigoMasmorra.CHEFE_OCULTO;

        // 481 - 525 = 4.5%
        if (chance <= 525) return InimigoMasmorra.GUARDIAO_MONTANHA;

        // 526 - 565 = 4%
        if (chance <= 565) return InimigoMasmorra.GARDIAO_CORRENTES;

        // 566 - 605 = 4%
        if (chance <= 605) return InimigoMasmorra.AMEBA_CAVEIRA;

        // 606 - 645 = 4%
        if (chance <= 645) return InimigoMasmorra.MORCEGO_VAMPIRICO;

        // 646 - 680 = 3.5%
        if (chance <= 680) return InimigoMasmorra.LODO_TOXICO;

        // 681 - 715 = 3.5%
        if (chance <= 715) return InimigoMasmorra.ARANHA_SOMBRIA;

        // 716 - 750 = 3.5%
        if (chance <= 750) return InimigoMasmorra.ESCORPIAO_GIGANTE;

        // 751 - 780 = 3%
        if (chance <= 780) return InimigoMasmorra.BESTA_DAS_CAVERNAS;

        // 781 - 810 = 3%
        if (chance <= 810) return InimigoMasmorra.GOLEM_PEDRA;

        // 811 - 837 = 2.7%
        if (chance <= 837) return InimigoMasmorra.ESQUELETO_GUERREIRO;

        // 838 - 864 = 2.7%
        if (chance <= 864) return InimigoMasmorra.CARNICEIRO_ORC;

        // 865 - 890 = 2.6%
        if (chance <= 890) return InimigoMasmorra.GOLEM_MAGMA;

        // 891 - 915 = 2.5%
        if (chance <= 915) return InimigoMasmorra.BRUTO_MONTANHA;

        // 916 - 937 = 2.2%
        if (chance <= 937) return InimigoMasmorra.CAVALEIRO_AMALDICOADO;

        // 938 - 957 = 2%
        if (chance <= 957) return InimigoMasmorra.OLHO_ABISSAL;

        // 958 - 974 = 1.7%
        if (chance <= 974) return InimigoMasmorra.DEVORADOR_ALMAS;

        // 975 - 987 = 1.3%
        if (chance <= 987) return InimigoMasmorra.NECROMANTE_PROPANO;

        // 988 - 992 = 0.5%
        if (chance <= 992) return InimigoMasmorra.MINOTAURO_FURIOSO;

        // 993 - 995 = 0.3%
        if (chance <= 995) return InimigoMasmorra.ARACNIDEA_RAINHA;

        // 996 - 997 = 0.2%
        if (chance <= 997) return InimigoMasmorra.GUARDIAO_UNICO;

        // 998 = 0.1%
        if (chance <= 998) return InimigoMasmorra.HIDRA_DAS_PROFUNDEZAS;

        // 999 = 0.1%
        if (chance <= 999) return InimigoMasmorra.TITA_OBSIDIANA;

        // 1000 = 0.1%
        return InimigoMasmorra.SUSSURRADOR_DAS_SOMBRAS;
    }
    
    private long sortearHpInimigo(UsuarioBossBattle usuario, InimigoMasmorra inimigo) {

        long ataqueUsuario = Math.max(1L, calcularAtaqueBaseTotal(usuario));

        long minimo = ataqueUsuario;
        long maximo = ataqueUsuario * 4;

        return sortearNumero((int) minimo, (int) maximo);
    }

    private long sortearAtaqueInimigo(UsuarioBossBattle usuario, InimigoMasmorra inimigo) {

        long ataqueUsuario = Math.max(1L, calcularAtaqueBaseTotal(usuario));

        long minimo = Math.max(1L, ataqueUsuario / 20); // 5%
        long maximo = Math.max(2L, ataqueUsuario / 10); // 10%

        return sortearNumero((int) minimo, (int) maximo);
    }

    private long calcularDanoUsuario(UsuarioBossBattle usuario) {
        return Math.max(1L, calcularAtaqueBaseTotal(usuario));
    }

    private long calcularDanoNoVigor(UsuarioBossBattle usuario) {
        return Math.max(1L, usuario.getMasmorraInimigoAtaque());
    }
    
    
    private Map<String, Object> aplicarRecompensa(
            UsuarioBossBattle usuario,
            InimigoMasmorra inimigo
    ) {
        switch (inimigo) {

            case GOBLIN:
                // Recompensa fixa: Boss Coins entre 5 e 20
                return darBossCoins(usuario, sortearNumero(5, 10), "Goblin das Sombras derrotado");

            case ESQUELETO:
                // Recompensa fixa: Boss Coins entre 5 e 30
                return darBossCoins(usuario, sortearNumero(3, 15), "Esqueleto Antigo derrotado");

            case GOBLIN_Z:
                // Recompensa fixa: Boss Coins entre 5 e 25
                return darBossCoins(usuario, sortearNumero(5, 10), "Goblin-Z derrotado");

            case GUARDIAO:
                // Recompensa fixa: XP entre 5 e 15
                long xp = sortearNumero(5, 10);
                usuario.setExp(usuario.getExp() + xp);

                return Map.ofEntries(
                        Map.entry("tipo", "XP"),
                        Map.entry("nome", "Guardião Sombrio derrotado"),
                        Map.entry("valor", xp)
                );
                
                

            case CHEFE_OCULTO:
                int chanceChefe = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceChefe <= 40) {
                    long bossCoins = sortearNumero(10, 20);

                    usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                    ultimoValorRecebidoService.setUltimoValorRecebido(
                            usuario,
                            BigDecimal.valueOf(bossCoins)
                    );

                    return Map.ofEntries(
                            Map.entry("tipo", "BOSS_COINS"),
                            Map.entry("nome", "Tesouro do Chefe Oculto"),
                            Map.entry("valor", bossCoins)
                    );
                }

                // 41 - 75 = 35%
                if (chanceChefe <= 75) {
                    long xpMedio = sortearNumero(5, 10);
                    usuario.setExp(usuario.getExp() + xpMedio);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento Sombrio"),
                            Map.entry("valor", xpMedio)
                    );
                }

                // 76 - 95 = 20%
                if (chanceChefe <= 95) {
                	   // 96 - 100 = 5%
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );

            case GNOMO_X:
                int chanceGnomo = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceGnomo <= 40) {
                    long bossCoins = sortearNumero(5, 20);

                    usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                    ultimoValorRecebidoService.setUltimoValorRecebido(
                            usuario,
                            BigDecimal.valueOf(bossCoins)
                    );

                    return Map.ofEntries(
                            Map.entry("tipo", "BOSS_COINS"),
                            Map.entry("nome", "Tesouro do Gnomo-X"),
                            Map.entry("valor", bossCoins)
                    );
                }

                // 41 - 65 = 25%
                if (chanceGnomo <= 65) {
                    long xpX = sortearNumero(5, 10);

                    usuario.setExp(usuario.getExp() + xpX);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento do Gnomo-X"),
                            Map.entry("valor", xpX)
                    );
                }

                // 66 - 95 = 30%
                if (chanceGnomo <= 95) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 1L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Elixir do Gnomo-X"),
                            Map.entry("valor", 1L)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );

            case GUARDIAO_MONTANHA:
                int chanceGuardiao = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceGuardiao <= 40) {
                    long bossCoins = sortearNumero(5, 15);

                    usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                    ultimoValorRecebidoService.setUltimoValorRecebido(
                            usuario,
                            BigDecimal.valueOf(bossCoins)
                    );

                    return Map.ofEntries(
                            Map.entry("tipo", "BOSS_COINS"),
                            Map.entry("nome", "Tesouro do Guardião da Montanha"),
                            Map.entry("valor", bossCoins)
                    );
                }

                // 41 - 65 = 25%
                if (chanceGuardiao <= 65) {
                    long xpGuardiao = sortearNumero(15, 25);

                    usuario.setExp(usuario.getExp() + xpGuardiao);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Sabedoria da Montanha"),
                            Map.entry("valor", xpGuardiao)
                    );
                }

                // 66 - 95 = 30%
                if (chanceGuardiao <= 95) {
                	
                	long pocaoGuardiao = sortearNumero(1, 2);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoGuardiao);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência da Montanha"),
                            Map.entry("valor", pocaoGuardiao)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );

             // --- CASE GUARDIÃO DAS CORRENTES COM MAIS DE UM PRÊMIO
            case GARDIAO_CORRENTES:
                int chanceGuardiaoCorrentes = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceGuardiaoCorrentes <= 40) {
                	
                	  long xpGuardiaoCorrentes = sortearNumero(15, 25);

                      usuario.setExp(usuario.getExp() + xpGuardiaoCorrentes);

                      return Map.ofEntries(
                              Map.entry("tipo", "XP"),
                              Map.entry("nome", "Sabedoria das Correntes"),
                              Map.entry("valor", xpGuardiaoCorrentes)
                      );
                  
                }

                // 41 - 65 = 25%
                if (chanceGuardiaoCorrentes <= 65) {
                	return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }
                

                // 66 - 95 = 30%
                if (chanceGuardiaoCorrentes <= 95) {
                	  long pocaoGuardiaoCorrentes  = sortearNumero(1, 2);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoGuardiaoCorrentes);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência das Correntes"),
                            Map.entry("valor", pocaoGuardiaoCorrentes)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );
             // --- CASE SUSSURRADOR DAS SOMBRAS COM MAIS DE UM PRÊMIO
            case SUSSURRADOR_DAS_SOMBRAS:
                int chanceSussurrador = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceSussurrador <= 40) {
                	
                	  long pocaoSussurrador  = sortearNumero(1, 2);
                	  
                	  
                	 usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoSussurrador);

                     return Map.ofEntries(
                             Map.entry("tipo", "POCAO_VIGOR"),
                             Map.entry("nome", "Essência das Sombras"),
                             Map.entry("valor", pocaoSussurrador)
                     );
                }

                // 41 - 65 = 25%
                if (chanceSussurrador <= 65) {
                    long xpSussurrador = sortearNumero(2, 15);

                    usuario.setExp(usuario.getExp() + xpSussurrador);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento Proibido"),
                            Map.entry("valor", xpSussurrador)
                    );
                }

                // 66 - 95 = 30%
                if (chanceSussurrador <= 95) {
                	
                	 usuario.setAtaqueBase(usuario.getAtaqueBase() + 1L);

                     return Map.ofEntries(
                             Map.entry("tipo", "ATAQUE_BASE"),
                             Map.entry("nome", "Segredos das Sombras"),
                             Map.entry("valor", 1L)
                     );
                   
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "As sombras desapareceram"),
                        Map.entry("valor", 0L)
                );   
                
                
             // --- CASE GOLEM MAGMA COM MAIS DE UM PRÊMIO
            case GOLEM_MAGMA:
                int chanceGolemMagma = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceGolemMagma <= 40) {
                	
                	  long xpGolemMagma = sortearNumero(5, 11);

                      usuario.setExp(usuario.getExp() + xpGolemMagma);

                      return Map.ofEntries(
                              Map.entry("tipo", "XP"),
                              Map.entry("nome", "Sabedoria das Rochas Ardentes"),
                              Map.entry("valor", xpGolemMagma)
                      );
                  
                }
                
             

                // 41 - 65 = 25%
                if (chanceGolemMagma <= 65) {
                	
                	  long espadaEsqueleto = sortearNumero(1, 2);
                	
                	  usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + espadaEsqueleto);


                      return Map.ofEntries(
                              Map.entry("tipo", "ESPADA_FANEJANTE"),
                              Map.entry("nome", "Força Vulcânica"),
                              Map.entry("valor", espadaEsqueleto)
                      );
                }

                // 66 - 95 = 30%
                if (chanceGolemMagma <= 95) {
                	 long pocaoEsqueleto = sortearNumero(1, 3);
                	
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoEsqueleto);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Núcleo de Magma"),
                            Map.entry("valor", pocaoEsqueleto)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "O magma esfriou"),
                        Map.entry("valor", 0L)
                );
                
                
             // --- CASE ESQUELETO GUERREIRO COM MAIS DE UM PRÊMIO
            case ESQUELETO_GUERREIRO:
                int chanceEsqueletoGuerreiro = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceEsqueletoGuerreiro <= 40) {
                	 long bossCoins = sortearNumero(5, 15);

                     usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                     ultimoValorRecebidoService.setUltimoValorRecebido(
                             usuario,
                             BigDecimal.valueOf(bossCoins)
                     );

                     return Map.ofEntries(
                             Map.entry("tipo", "BOSS_COINS"),
                             Map.entry("nome", "Tesouro do Chefe Oculto"),
                             Map.entry("valor", bossCoins)
                     );
                 

                }

                // 41 - 65 = 25%
                if (chanceEsqueletoGuerreiro <= 65) {
                    long xpEsqueleto = sortearNumero(5, 15);

                    usuario.setExp(usuario.getExp() + xpEsqueleto);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Memórias de Batalha"),
                            Map.entry("valor", xpEsqueleto)
                    );
                }

                // 66 - 95 = 30%
                if (chanceEsqueletoGuerreiro <= 95) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 2L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Pó dos Antigos"),
                            Map.entry("valor", 2L)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Os ossos viraram poeira"),
                        Map.entry("valor", 0L)
                );
                
                
             // --- CASE BRUTO MONTANHA COM MAIS DE UM PRÊMIO
            case BRUTO_MONTANHA:
                int chanceBrutoMontanha = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceBrutoMontanha <= 40) {
                	
                	  long xpBrutoMontanha = sortearNumero(10, 20);

                      usuario.setExp(usuario.getExp() + xpBrutoMontanha);

                      return Map.ofEntries(
                              Map.entry("tipo", "XP"),
                              Map.entry("nome", "Experiência de Sobrevivência"),
                              Map.entry("valor", xpBrutoMontanha)
                      );
                 
                }

               
                // 41 - 65 = 25%
                if (chanceBrutoMontanha <= 65) {
                	 return Map.ofEntries(
                             Map.entry("tipo", "NADA"),
                             Map.entry("nome", "O gigante desapareceu nas montanhas"),
                             Map.entry("valor", 0L)
                     );
                }

                // 66 - 95 = 30%
                if (chanceBrutoMontanha <= 95) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 1L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Tônico do Colosso"),
                            Map.entry("valor", 1L)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "O gigante desapareceu nas montanhas"),
                        Map.entry("valor", 0L)
                );
                
                
             // --- CASE MINOTAURO FURIOSO COM MAIS DE UM PRÊMIO
            case MINOTAURO_FURIOSO:
                int chanceMinotauroFurioso = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceMinotauroFurioso <= 40) {
                	  long xpMinotauro = sortearNumero(10, 20);

                      usuario.setExp(usuario.getExp() + xpMinotauro);

                      return Map.ofEntries(
                              Map.entry("tipo", "XP"),
                              Map.entry("nome", "Instinto de Caçador"),
                              Map.entry("valor", xpMinotauro)
                      );
                }
                
               

                // 41 - 65 = 25%
                if (chanceMinotauroFurioso <= 65) {
                    long pocaoMinotauro = sortearNumero(1, 5);

                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoMinotauro);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Sangue do Minotauro"),
                            Map.entry("valor", pocaoMinotauro)
                    );
                }

                // 66 - 95 = 30%
                if (chanceMinotauroFurioso <= 95) {
                	 long machadoMinotauro = sortearNumero(1, 3);
                	
                  usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + machadoMinotauro);

                    return Map.ofEntries(
                            Map.entry("tipo", "MACHADO_DILACERADOR"),
                            Map.entry("nome", "Sangue do Minotauro"),
                            Map.entry("valor", machadoMinotauro)
                    );
                }
           
                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "A besta retornou ao labirinto"),
                        Map.entry("valor", 0L)
                );
                
             // --- CASE OLHO ABISSAL COM MAIS DE UM PRÊMIO
            case OLHO_ABISSAL:
                int chanceOlhoAbissal = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceOlhoAbissal <= 40) {
                    long xpOlhoAbissal = sortearNumero(5, 20);

                    usuario.setExp(usuario.getExp() + xpOlhoAbissal);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Visões do Abismo"),
                            Map.entry("valor", xpOlhoAbissal)
                    );
                }

                // 41 - 65 = 25%
                if (chanceOlhoAbissal <= 65) {
                    long pocaoOlhoAbissal = sortearNumero(1, 3);

                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoOlhoAbissal);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Lágrimas do Abismo"),
                            Map.entry("valor", pocaoOlhoAbissal)
                    );
                }
                

                // 66 - 95 = 30%
                if (chanceOlhoAbissal <= 95) {
                	 long bossCoins = sortearNumero(10, 20);

                     usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                     ultimoValorRecebidoService.setUltimoValorRecebido(
                             usuario,
                             BigDecimal.valueOf(bossCoins)
                     );

                     return Map.ofEntries(
                             Map.entry("tipo", "BOSS_COINS"),
                             Map.entry("nome", "Tesouro do Chefe Oculto"),
                             Map.entry("valor", bossCoins)
                     );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "O olhar desapareceu na escuridão"),
                        Map.entry("valor", 0L)
                );
                
             // --- CASE GOLEM PEDRA COM MAIS DE UM PRÊMIO
            case GOLEM_PEDRA:
                int chanceGolemPedra = random.nextInt(100) + 1;

                // 1 - 40 = 40%
                if (chanceGolemPedra <= 40) {
                    long xpGolemPedra = sortearNumero(10, 20);

                    usuario.setExp(usuario.getExp() + xpGolemPedra);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Sabedoria da Montanha"),
                            Map.entry("valor", xpGolemPedra)
                    );
                }

                // 41 - 65 = 25%
                if (chanceGolemPedra <= 65) {
                    long pocaoGolemPedra = sortearNumero(1, 4);

                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoGolemPedra);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Fragmentos de Granito"),
                            Map.entry("valor", pocaoGolemPedra)
                    );
                }

                // 66 - 95 = 30%
                if (chanceGolemPedra <= 95) {
                    long recompensaGolemPedra = sortearNumero(1, 3);

              
                    usuario.setEscudoPrimordial(
                            usuario.getEscudoPrimordial() + recompensaGolemPedra
                    );

                    return Map.ofEntries(
                            Map.entry("tipo", "ESCUDO_PRIMORDIAL"),
                            Map.entry("nome", "Coração de Pedra"),
                            Map.entry("valor", recompensaGolemPedra)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "O golem virou escombros"),
                        Map.entry("valor", 0L)
                );
                
             // --- CASE AMEBA CAVEIRA COM MAIS DE UM PRÊMIO
            case AMEBA_CAVEIRA:
                int chanceAmebaCaveira = random.nextInt(100) + 1;

                if (chanceAmebaCaveira <= 40) {
                    long xpAmebaCaveira = sortearNumero(10, 20);
                    usuario.setExp(usuario.getExp() + xpAmebaCaveira);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Memória Gelatinosa"),
                            Map.entry("valor", xpAmebaCaveira)
                    );
                }

                if (chanceAmebaCaveira <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceAmebaCaveira <= 95) {
                    long pocaoAmebaCaveira = sortearNumero(1, 2);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoAmebaCaveira);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Lodo Revigorante"),
                            Map.entry("valor", pocaoAmebaCaveira)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE CARNICEIRO ORC COM MAIS DE UM PRÊMIO
            case CARNICEIRO_ORC:
                int chanceCarniceiroOrc = random.nextInt(100) + 1;

                if (chanceCarniceiroOrc <= 40) {
                    long xpCarniceiroOrc = sortearNumero(2, 15);
                    usuario.setExp(usuario.getExp() + xpCarniceiroOrc);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Brutalidade Orc"),
                            Map.entry("valor", xpCarniceiroOrc)
                    );
                }

                if (chanceCarniceiroOrc <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceCarniceiroOrc <= 95) {
                    long pocaoCarniceiroOrc = sortearNumero(2, 4);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoCarniceiroOrc);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Sangue Orc"),
                            Map.entry("valor", pocaoCarniceiroOrc)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE CAVALEIRO AMALDIÇOADO COM MAIS DE UM PRÊMIO
            case CAVALEIRO_AMALDICOADO:
                int chanceCavaleiroAmaldicoado = random.nextInt(100) + 1;

                if (chanceCavaleiroAmaldicoado <= 40) {
                    long xpCavaleiroAmaldicoado = sortearNumero(30, 55);
                    usuario.setExp(usuario.getExp() + xpCavaleiroAmaldicoado);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Honra Corrompida"),
                            Map.entry("valor", xpCavaleiroAmaldicoado)
                    );
                }

                if (chanceCavaleiroAmaldicoado <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceCavaleiroAmaldicoado <= 95) {
                    long pocaoCavaleiroAmaldicoado = sortearNumero(2, 4);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoCavaleiroAmaldicoado);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência Amaldiçoada"),
                            Map.entry("valor", pocaoCavaleiroAmaldicoado)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE DEVORADOR DE ALMAS COM MAIS DE UM PRÊMIO
            case DEVORADOR_ALMAS:
                int chanceDevoradorAlmas = random.nextInt(100) + 1;

                if (chanceDevoradorAlmas <= 40) {
                    long xpDevoradorAlmas = sortearNumero(4, 20);
                    usuario.setExp(usuario.getExp() + xpDevoradorAlmas);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Fragmentos de Alma"),
                            Map.entry("valor", xpDevoradorAlmas)
                    );
                }

                if (chanceDevoradorAlmas <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceDevoradorAlmas <= 95) {
                	  long espadaDevoradorAlmas = sortearNumero(1, 3);
                  	
                	  usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + espadaDevoradorAlmas);


                      return Map.ofEntries(
                              Map.entry("tipo", "ESPADA_FANEJANTE"),
                              Map.entry("nome", "Força Almática"),
                              Map.entry("valor", espadaDevoradorAlmas)
                      );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE GUARDIÃO ÚNICO COM MAIS DE UM PRÊMIO
            case GUARDIAO_UNICO:
                int chanceGuardiaoUnico = random.nextInt(100) + 1;

                if (chanceGuardiaoUnico <= 40) {
                    long xpGuardiaoUnico = sortearNumero(10, 15);
                    usuario.setExp(usuario.getExp() + xpGuardiaoUnico);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Sabedoria do Guardião"),
                            Map.entry("valor", xpGuardiaoUnico)
                    );
                }

                if (chanceGuardiaoUnico <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceGuardiaoUnico <= 95) {
                	 long machadoGuardiaoUnico = sortearNumero(1, 2);
                 	
                     usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + machadoGuardiaoUnico);

                       return Map.ofEntries(
                               Map.entry("tipo", "MACHADO_DILACERADOR"),
                               Map.entry("nome", "Sangue do Unico "),
                               Map.entry("valor", machadoGuardiaoUnico)
                       );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE HIDRA DAS PROFUNDEZAS COM MAIS DE UM PRÊMIO
            case HIDRA_DAS_PROFUNDEZAS:
                int chanceHidraProfundezas = random.nextInt(100) + 1;

                if (chanceHidraProfundezas <= 40) {
                    long xpHidraProfundezas = sortearNumero(10, 20);
                    usuario.setExp(usuario.getExp() + xpHidraProfundezas);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento Abissal"),
                            Map.entry("valor", xpHidraProfundezas)
                    );
                }

                if (chanceHidraProfundezas <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceHidraProfundezas <= 95) {
                    long pocaoHidraProfundezas = sortearNumero(1, 2);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoHidraProfundezas);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Sangue da Hidra"),
                            Map.entry("valor", pocaoHidraProfundezas)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE NECROMANTE PROFANO COM MAIS DE UM PRÊMIO
            case NECROMANTE_PROPANO:
                int chanceNecromantePropano = random.nextInt(100) + 1;

                if (chanceNecromantePropano <= 40) {
                    long xpNecromantePropano = sortearNumero(5, 20);
                    usuario.setExp(usuario.getExp() + xpNecromantePropano);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Magia Profana"),
                            Map.entry("valor", xpNecromantePropano)
                    );
                }

                if (chanceNecromantePropano <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceNecromantePropano <= 95) {
                	 long machadoNecromantePropano = sortearNumero(1, 2);
                 	
                     usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + machadoNecromantePropano);

                       return Map.ofEntries(
                               Map.entry("tipo", "MACHADO_DILACERADOR"),
                               Map.entry("nome", "Visão Profana"),
                               Map.entry("valor", machadoNecromantePropano)
                       );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE TITÃ OBSIDIANA COM MAIS DE UM PRÊMIO
            case TITA_OBSIDIANA:
                int chanceTitaObsidiana = random.nextInt(100) + 1;

                if (chanceTitaObsidiana <= 40) {
                   
                    long recompensaGolemPedra = sortearNumero(1, 2);

                    
                    usuario.setEscudoPrimordial(
                            usuario.getEscudoPrimordial() + recompensaGolemPedra
                    );

                    return Map.ofEntries(
                            Map.entry("tipo", "ESCUDO_PRIMORDIAL"),
                            Map.entry("nome", "Poder de Obsidiana"),
                            Map.entry("valor", recompensaGolemPedra)
                    );
                }

                if (chanceTitaObsidiana <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceTitaObsidiana <= 95) {
                    long pocaoTitaObsidiana = sortearNumero(1, 3);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoTitaObsidiana);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Núcleo de Obsidiana"),
                            Map.entry("valor", pocaoTitaObsidiana)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE ARACNÍDEA RAINHA COM MAIS DE UM PRÊMIO
            case ARACNIDEA_RAINHA:
                int chanceAracnideaRainha = random.nextInt(100) + 1;

                if (chanceAracnideaRainha <= 40) {
                    long xpAracnideaRainha = sortearNumero(5, 10);
                    usuario.setExp(usuario.getExp() + xpAracnideaRainha);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Instinto da Rainha"),
                            Map.entry("valor", xpAracnideaRainha)
                    );
                }

                if (chanceAracnideaRainha <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceAracnideaRainha <= 95) {
                    long pocaoAracnideaRainha = sortearNumero(1, 3);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoAracnideaRainha);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Veneno Real"),
                            Map.entry("valor", pocaoAracnideaRainha)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE ARANHA SOMBRIA COM MAIS DE UM PRÊMIO
            case ARANHA_SOMBRIA:
                int chanceAranhaSombria = random.nextInt(100) + 1;

                if (chanceAranhaSombria <= 40) {
                    long xpAranhaSombria = sortearNumero(8, 25);
                    usuario.setExp(usuario.getExp() + xpAranhaSombria);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Teia Sombria"),
                            Map.entry("valor", xpAranhaSombria)
                    );
                }

                if (chanceAranhaSombria <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceAranhaSombria <= 95) {
                    long pocaoAranhaSombria = sortearNumero(1, 3);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoAranhaSombria);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Veneno Sombrio"),
                            Map.entry("valor", pocaoAranhaSombria)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE BESTA DAS CAVERNAS COM MAIS DE UM PRÊMIO
            case BESTA_DAS_CAVERNAS:
                int chanceBestaCavernas = random.nextInt(100) + 1;

                if (chanceBestaCavernas <= 40) {
                    long xpBestaCavernas = sortearNumero(12, 20);
                    usuario.setExp(usuario.getExp() + xpBestaCavernas);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Fúria das Cavernas"),
                            Map.entry("valor", xpBestaCavernas)
                    );
                }

                if (chanceBestaCavernas <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceBestaCavernas <= 95) {
                	  long bossCoins = sortearNumero(10, 25);

                      usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                      ultimoValorRecebidoService.setUltimoValorRecebido(
                              usuario,
                              BigDecimal.valueOf(bossCoins)
                      );

                      return Map.ofEntries(
                              Map.entry("tipo", "BOSS_COINS"),
                              Map.entry("nome", "Tesouro do Guardião da Montanha"),
                              Map.entry("valor", bossCoins)
                      );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE ESCORPIÃO GIGANTE COM MAIS DE UM PRÊMIO
            case ESCORPIAO_GIGANTE:
                int chanceEscorpiaoGigante = random.nextInt(100) + 1;

                if (chanceEscorpiaoGigante <= 40) {
                    long xpEscorpiaoGigante = sortearNumero(10, 20);
                    usuario.setExp(usuario.getExp() + xpEscorpiaoGigante);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Ferrão Mortal"),
                            Map.entry("valor", xpEscorpiaoGigante)
                    );
                }

                if (chanceEscorpiaoGigante <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceEscorpiaoGigante <= 95) {
                    long pocaoEscorpiaoGigante = sortearNumero(1, 3);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoEscorpiaoGigante);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Poção Concentrada"),
                            Map.entry("valor", pocaoEscorpiaoGigante)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE LODO TÓXICO COM MAIS DE UM PRÊMIO
            case LODO_TOXICO:
                int chanceLodoToxico = random.nextInt(100) + 1;

                if (chanceLodoToxico <= 40) {
                    long xpLodoToxico = sortearNumero(15, 30);
                    usuario.setExp(usuario.getExp() + xpLodoToxico);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Resíduo Tóxico"),
                            Map.entry("valor", xpLodoToxico)
                    );
                }

                if (chanceLodoToxico <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                if (chanceLodoToxico <= 95) {
                    long pocaoLodoToxico = sortearNumero(1, 2);
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + pocaoLodoToxico);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Lodo Energético"),
                            Map.entry("valor", pocaoLodoToxico)
                    );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );


            // --- CASE MORCEGO VAMPÍRICO COM MAIS DE UM PRÊMIO
            case MORCEGO_VAMPIRICO:
                int chanceMorcegoVampirico = random.nextInt(100) + 1;

                if (chanceMorcegoVampirico <= 40) {
                    long xpMorcegoVampirico = sortearNumero(12, 25);
                    usuario.setExp(usuario.getExp() + xpMorcegoVampirico);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Sangue Vampírico"),
                            Map.entry("valor", xpMorcegoVampirico)
                    );
                }

                if (chanceMorcegoVampirico <= 65) {
                    return Map.ofEntries(
                            Map.entry("tipo", "NADA"),
                            Map.entry("nome", "Nada encontrado"),
                            Map.entry("valor", 0L)
                    );
                }

                
                if (chanceMorcegoVampirico <= 95) {
                	  long bossCoins = sortearNumero(10, 25);

                      usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(bossCoins)));

                      ultimoValorRecebidoService.setUltimoValorRecebido(
                              usuario,
                              BigDecimal.valueOf(bossCoins)
                      );

                      return Map.ofEntries(
                              Map.entry("tipo", "BOSS_COINS"),
                              Map.entry("nome", "Moedas Vampíricas"),
                              Map.entry("valor", bossCoins)
                      );
                }

                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );
            default:
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "Nada encontrado"),
                        Map.entry("valor", 0L)
                );
        }
    }

    private Map<String, Object> darBossCoins(
            UsuarioBossBattle usuario,
            long valor,
            String nome
    ) {
        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(valor)));

        ultimoValorRecebidoService.setUltimoValorRecebido(
                usuario,
                BigDecimal.valueOf(valor)
        );

        return Map.ofEntries(
                Map.entry("tipo", "BOSS_COINS"),
                Map.entry("nome", nome),
                Map.entry("valor", valor)
        );
    }

    private Map<String, Object> respostaCombate(
            UsuarioBossBattle usuario,
            String mensagem
    ) {
        if (usuario.getMasmorraInimigoAtual() == null) {
            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_COMBATE"),
                    Map.entry("mensagem", "Nenhum combate ativo.")
            );
        }

        InimigoMasmorra inimigo = InimigoMasmorra.valueOf(usuario.getMasmorraInimigoAtual());

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "COMBATE"),
                Map.entry("mensagem", mensagem),
                Map.entry("emCombate", true),
                Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                Map.entry("inimigoNome", inimigo.getNome()),
                Map.entry("inimigoImagem", inimigo.getImagem()),
                Map.entry("inimigoHpAtual", usuario.getMasmorraInimigoHpAtual()),
                Map.entry("inimigoHpMax", usuario.getMasmorraInimigoHpMax()),
                Map.entry("inimigoAtaque", usuario.getMasmorraInimigoAtaque()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_CICLO),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
        );
    }
    
    private void limparCombate(UsuarioBossBattle usuario) {
        usuario.setMasmorraEmCombate(false);
        usuario.setMasmorraInimigoAtual(null);
        usuario.setMasmorraInimigoHpAtual(0L);
        usuario.setMasmorraInimigoHpMax(0L);
        usuario.setMasmorraInimigoAtaque(0L);
    }


    private void protegerCamposMasmorra(UsuarioBossBattle usuario) {

        if (usuario.getTentativasMasmorraHoje() == null) {
            usuario.setTentativasMasmorraHoje(0);
        }

        if (usuario.getTotalExploracoesMasmorra() == null) {
            usuario.setTotalExploracoesMasmorra(0L);
        }

        if (usuario.getMasmorraEmCombate() == null) {
            usuario.setMasmorraEmCombate(false);
        }

        if (usuario.getMasmorraInimigoHpAtual() == null) {
            usuario.setMasmorraInimigoHpAtual(0L);
        }

        if (usuario.getMasmorraInimigoHpMax() == null) {
            usuario.setMasmorraInimigoHpMax(0L);
        }

        if (usuario.getMasmorraInimigoAtaque() == null) {
            usuario.setMasmorraInimigoAtaque(0L);
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        if (usuario.getEnergiaGuerreiros() == null) {
            usuario.setEnergiaGuerreiros(0L);
        }
    }

    private long sortearNumero(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    
    //---> usarPocao
 // ---> usarPocao
    @Transactional
    public Map<String, Object> usarPocao(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())) {

            return Map.of(
                    "sucesso", false,
                    "mensagem", "Você não está em combate."
            );
        }

        if (usuario.getPocaoVigor() <= 0) {

            return Map.of(
                    "sucesso", false,
                    "mensagem", "Você não possui poções."
            );
        }

        long vigorMaximo =
                Math.max(1L, usuario.getEnergiaGuerreirosPadrao());

        usuario.setEnergiaGuerreiros(vigorMaximo);

        usuario.setPocaoVigor(
                Math.max(0, usuario.getPocaoVigor() - 1)
        );

        usuarioRepository.save(usuario);

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "POCAO_USADA"),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao()),
                Map.entry("pocoesRestantes", usuario.getPocaoVigor()),
                Map.entry("pocoesVida", usuario.getPocaoVigor()),
                Map.entry(
                        "mensagem",
                        "Você utilizou uma poção e recuperou todo o vigor."
                )
        );
    }
}
