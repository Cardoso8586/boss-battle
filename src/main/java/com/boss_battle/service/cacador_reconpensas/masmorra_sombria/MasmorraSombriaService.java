
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

    private static final int CUSTO_VIGOR_ENTRADA = 10;
    private static final int LIMITE_ENTRADAS_DIARIAS = 30;
    private static final long COOLDOWN_MINUTOS = 3L;
    

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
        resetarMasmorraSeNovoDia(usuario);

        if (cooldownAtivo(usuario)) {
            return respostaCooldown(usuario);
        }

        if (Boolean.TRUE.equals(usuario.getMasmorraEmCombate())) {
            return respostaCombate(usuario, "Você já está em combate na Masmorra Sombria.");
        }

        if (usuario.getTentativasMasmorraHoje() >= LIMITE_ENTRADAS_DIARIAS) {
            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "LIMITE_DIARIO"),
                    Map.entry("mensagem", "Você já entrou na Masmorra Sombria todas as vezes de hoje."),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_DIARIAS),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
            );
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
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
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

        usuarioRepository.save(usuario);

        return respostaCombate(usuario, "Você entrou na Masmorra Sombria e encontrou " + inimigo.getNome() + ".");
    }

    @Transactional
    public Map<String, Object> atacarMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarMasmorraSeNovoDia(usuario);

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
        long custoVigorAtaque = Math.max(1L, danoUsuario/2);

        long vigorDepoisDoAtaque = Math.max(0L, usuario.getEnergiaGuerreiros() - custoVigorAtaque);
        usuario.setEnergiaGuerreiros(vigorDepoisDoAtaque);

        long hpAntes = Math.max(0L, usuario.getMasmorraInimigoHpAtual());
        long hpDepois = Math.max(0L, hpAntes - danoUsuario);
        usuario.setMasmorraInimigoHpAtual(hpDepois);

        if (vigorDepoisDoAtaque <= 0 && hpDepois > 0) {

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            aplicarCooldown(usuario);
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
                    Map.entry("cooldownAtivo", true),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
            );
        }

        if (hpDepois <= 0) {

            Map<String, Object> recompensa = aplicarRecompensa(usuario, inimigo);

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            aplicarCooldown(usuario);
            usuarioRepository.save(usuario);

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("status", "VITORIA"),
                    Map.entry("mensagem", "Você derrotou " + inimigo.getNome() ),
                    Map.entry("danoUsuario", danoUsuario),
                    Map.entry("danoRecebido", 0L),
                    Map.entry("vigorGastoAtaque", custoVigorAtaque),
                    Map.entry("inimigoNome", inimigo.getNome()),
                    Map.entry("inimigoImagem", inimigo.getImagem()),
                    Map.entry("inimigoHpAtual", 0L),
                    Map.entry("inimigoHpMax", hpMaxAntesLimpar),
                    Map.entry("inimigoAtaque", ataqueAntesLimpar),
                    Map.entry("recompensa", recompensa),
                    Map.entry("cooldownAtivo", true),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
            );
        }

        long danoRecebido = calcularDanoNoVigor(usuario);
        long vigorDepoisRevidar = Math.max(0L, usuario.getEnergiaGuerreiros() - danoRecebido);
        usuario.setEnergiaGuerreiros(vigorDepoisRevidar);

        if (vigorDepoisRevidar <= 0) {

            long hpMaxAntesLimpar = usuario.getMasmorraInimigoHpMax();
            long ataqueAntesLimpar = usuario.getMasmorraInimigoAtaque();

            limparCombate(usuario);
            aplicarCooldown(usuario);
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
                    Map.entry("cooldownAtivo", true),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
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
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
        );
    }

    @Transactional
    public Map<String, Object> inimigoAtacarMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarMasmorraSeNovoDia(usuario);

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
            aplicarCooldown(usuario);
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
                    Map.entry("cooldownAtivo", true),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                    Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                    Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                    Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                    Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
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
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
        );
    }

    public Map<String, Object> statusMasmorra(Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        protegerCamposMasmorra(usuario);
        resetarMasmorraSeNovoDia(usuario);

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())
                || usuario.getMasmorraInimigoAtual() == null) {

            return Map.ofEntries(
                    Map.entry("sucesso", true),
                    Map.entry("emCombate", false),
                    Map.entry("cooldownAtivo", cooldownAtivo(usuario)),
                    Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                    Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                    Map.entry("limite", LIMITE_ENTRADAS_DIARIAS),
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

        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())) {
            return Map.ofEntries(
                    Map.entry("sucesso", false),
                    Map.entry("status", "SEM_COMBATE"),
                    Map.entry("mensagem", "Nenhum combate ativo.")
            );
        }

        limparCombate(usuario);
        aplicarCooldown(usuario);
        usuarioRepository.save(usuario);

        return Map.ofEntries(
                Map.entry("sucesso", true),
                Map.entry("status", "FUGIU"),
                Map.entry("mensagem", "Você fugiu da Masmorra Sombria. Aguarde 10 minutos para entrar novamente."),
                Map.entry("cooldownAtivo", true),
                Map.entry("cooldownSegundosRestantes", segundosRestantesCooldown(usuario)),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
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

    private Map<String, Object> respostaCooldown(UsuarioBossBattle usuario) {
        long segundos = segundosRestantesCooldown(usuario);
        long minutos = Math.max(1L, (long) Math.ceil(segundos / 60.0));

        return Map.ofEntries(
                Map.entry("sucesso", false),
                Map.entry("status", "COOLDOWN"),
                Map.entry("mensagem", "A Masmorra está em cooldown. Aguarde aproximadamente " + minutos + " minuto(s)."),
                Map.entry("cooldownAtivo", true),
                Map.entry("cooldownSegundosRestantes", segundos),
                Map.entry("cooldownAte", usuario.getMasmorraCooldownAte()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_DIARIAS),
                Map.entry("pocoesVida", Math.max(0, usuario.getPocaoVigor())),
                Map.entry("vigorAtual", usuario.getEnergiaGuerreiros()),
                Map.entry("vigorMaximo", usuario.getEnergiaGuerreirosPadrao())
        );
    }

    private InimigoMasmorra sortearInimigo() {
        int chance = random.nextInt(1000) + 1;

        // 1 - 250
        // Chance: 25%
        if (chance <= 250) return InimigoMasmorra.GOBLIN;

        // 251 - 450
        // Chance: 20%
        if (chance <= 450) return InimigoMasmorra.ESQUELETO;

        // 451 - 600
        // Chance: 15%
        if (chance <= 600) return InimigoMasmorra.GUARDIAO;

        // 601 - 750
        // Chance: 15%
        if (chance <= 750) return InimigoMasmorra.GNOMO_X;

        // 751 - 850
        // Chance: 10%
        if (chance <= 850) return InimigoMasmorra.GOBLIN_Z;

        // 851 - 910
        // Chance: 6%
        if (chance <= 910) return InimigoMasmorra.CHEFE_OCULTO;

        // 911 - 950
        // Chance: 4%
        if (chance <= 950) return InimigoMasmorra.GUARDIAO_MONTANHA;

        // 951 - 980
        // Chance: 3%
        if (chance <= 980) return InimigoMasmorra.GARDIAO_CORRENTES;

        // 981 - 1000
        // Chance: 2%
        return InimigoMasmorra.SUSSURRADOR_DAS_SOMBRAS;
    }
    
    private long sortearHpInimigo(UsuarioBossBattle usuario, InimigoMasmorra inimigo) {

        long ataqueUsuario = Math.max(1L, calcularAtaqueBaseTotal(usuario));

        long minimo = ataqueUsuario;
        long maximo = ataqueUsuario * 3;

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
                return darBossCoins(usuario, sortearNumero(5, 20), "Goblin das Sombras derrotado");

            case ESQUELETO:
                // Recompensa fixa: Boss Coins entre 5 e 30
                return darBossCoins(usuario, sortearNumero(5, 30), "Esqueleto Antigo derrotado");

            case GOBLIN_Z:
                // Recompensa fixa: Boss Coins entre 5 e 25
                return darBossCoins(usuario, sortearNumero(5, 25), "Goblin-Z derrotado");

            case GUARDIAO:
                // Recompensa fixa: XP entre 5 e 15
                long xp = sortearNumero(5, 15);
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
                    long bossCoins = sortearNumero(20, 50);

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
                    long xpMedio = sortearNumero(20, 30);
                    usuario.setExp(usuario.getExp() + xpMedio);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento Sombrio"),
                            Map.entry("valor", xpMedio)
                    );
                }

                // 76 - 95 = 20%
                if (chanceChefe <= 95) {
                    usuario.setAtaqueBase(usuario.getAtaqueBase() + 1L);

                    return Map.ofEntries(
                            Map.entry("tipo", "ATAQUE_BASE"),
                            Map.entry("nome", "Poder Sombrio Absorvido"),
                            Map.entry("valor", 1L)
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
                    long bossCoins = sortearNumero(10, 40);

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
                    long xpX = sortearNumero(15, 30);

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
                    long bossCoins = sortearNumero(30, 70);

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
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 1L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência da Montanha"),
                            Map.entry("valor", 1L)
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
                    usuario.setAtaqueBase(usuario.getAtaqueBase() + 1L);

                    return Map.ofEntries(
                            Map.entry("tipo", "ATAQUE_BASE"),
                            Map.entry("nome", "Força das Correntes Sombrias"),
                            Map.entry("valor", 1L)
                    );
                }

                // 41 - 65 = 25%
                if (chanceGuardiaoCorrentes <= 65) {
                    long xpGuardiaoCorrentes = sortearNumero(15, 25);

                    usuario.setExp(usuario.getExp() + xpGuardiaoCorrentes);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Sabedoria das Correntes"),
                            Map.entry("valor", xpGuardiaoCorrentes)
                    );
                }

                // 66 - 95 = 30%
                if (chanceGuardiaoCorrentes <= 95) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 2L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência das Correntes"),
                            Map.entry("valor", 2L)
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
                    usuario.setAtaqueBase(usuario.getAtaqueBase() + 2L);

                    return Map.ofEntries(
                            Map.entry("tipo", "ATAQUE_BASE"),
                            Map.entry("nome", "Segredos das Sombras"),
                            Map.entry("valor", 2L)
                    );
                }

                // 41 - 65 = 25%
                if (chanceSussurrador <= 65) {
                    long xpSussurrador = sortearNumero(20, 50);

                    usuario.setExp(usuario.getExp() + xpSussurrador);

                    return Map.ofEntries(
                            Map.entry("tipo", "XP"),
                            Map.entry("nome", "Conhecimento Proibido"),
                            Map.entry("valor", xpSussurrador)
                    );
                }

                // 66 - 95 = 30%
                if (chanceSussurrador <= 95) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + 3L);

                    return Map.ofEntries(
                            Map.entry("tipo", "POCAO_VIGOR"),
                            Map.entry("nome", "Essência das Sombras"),
                            Map.entry("valor", 3L)
                    );
                }

                // 96 - 100 = 5%
                return Map.ofEntries(
                        Map.entry("tipo", "NADA"),
                        Map.entry("nome", "As sombras desapareceram"),
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
                Map.entry("cooldownAtivo", false),
                Map.entry("cooldownSegundosRestantes", 0L),
                Map.entry("inimigoNome", inimigo.getNome()),
                Map.entry("inimigoImagem", inimigo.getImagem()),
                Map.entry("inimigoHpAtual", usuario.getMasmorraInimigoHpAtual()),
                Map.entry("inimigoHpMax", usuario.getMasmorraInimigoHpMax()),
                Map.entry("inimigoAtaque", usuario.getMasmorraInimigoAtaque()),
                Map.entry("bossCoinsAtual", usuario.getBossCoins()),
                Map.entry("ataqueBase", calcularAtaqueBaseTotal(usuario)),
                Map.entry("tentativasHoje", usuario.getTentativasMasmorraHoje()),
                Map.entry("limite", LIMITE_ENTRADAS_DIARIAS),
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

    private void resetarMasmorraSeNovoDia(UsuarioBossBattle usuario) {
        LocalDate hoje = LocalDate.now();

        if (usuario.getDataUltimaExploracaoMasmorra() == null ||
                !usuario.getDataUltimaExploracaoMasmorra().isEqual(hoje)) {

            usuario.setTentativasMasmorraHoje(0);
            usuario.setDataUltimaExploracaoMasmorra(hoje);
        }
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
