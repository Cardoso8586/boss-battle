package com.boss_battle.service.missoes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.MissaoDiariaDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ReferidosRecompensaService;
import com.boss_battle.service.UltimoValorRecebidoService;

@Service
public class MissaoDiariaService {


	
	
    //====================== RECOMPENSAS ======================

    //========================================================

    private final UsuarioBossBattleRepository usuarioRepository;
    private final ReferidosRecompensaService referidosService;
    private final UltimoValorRecebidoService ultimoValorRecebidoService;
    
    
    public MissaoDiariaService(
    		UsuarioBossBattleRepository usuarioRepository, 
    		ReferidosRecompensaService referidosService,
    		UltimoValorRecebidoService ultimoValorRecebidoService
    		
    		)
    
    
    {
            this.usuarioRepository = usuarioRepository;
            this.referidosService = referidosService;
            this.ultimoValorRecebidoService = ultimoValorRecebidoService;
    }

   
  //====================== RESET AUTOMÁTICO ======================
 // debug
// @Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void resetarMissoesDiariasAutomaticamente() {
        ZoneId zone = ZoneId.of("America/Sao_Paulo");

        try {
            System.out.println("🔥 INICIOU: " + LocalDateTime.now(zone));

            List<UsuarioBossBattle> usuarios = usuarioRepository.findAll();
            LocalDate hoje = LocalDate.now(zone);

            for (UsuarioBossBattle usuario : usuarios) {
                resetarMissaoDiaria(usuario, hoje);
            }

            usuarioRepository.saveAll(usuarios);
            usuarioRepository.flush();

            System.out.println("✅ FINALIZOU: " + LocalDateTime.now(zone));
            System.out.println("👥 RESETADOS: " + usuarios.size());

        } catch (Exception e) {
            System.out.println("❌ ERRO NO SCHEDULER");
            e.printStackTrace();
        }
    }

 public void resetarMissaoDiaria(UsuarioBossBattle usuario, LocalDate hoje) {
     usuario.setMissaoDiariaNivelDano(1);
     usuario.setMissaoDiariaNivelAtaquesEspeciais(1);

     usuario.setMissaoDiariaDanoAtual(0L);
     usuario.setMissaoDiariaAtaquesEspeciaisAtual(0);

     usuario.setMissaoDiariaDanoConcluida(false);
     usuario.setMissaoDiariaAtaquesConcluida(false);

     usuario.setMissaoDiariaDataReset(hoje);
 }
//==============================================================================================
    public MissaoDiariaDTO buscarMissaoDiaria(Long usuarioId) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);
        validarResetDiario(usuario);
        MissaoDiariaDTO dto = new MissaoDiariaDTO();
        /*DEBUG QUSE FUNCIONOU
        System.out.println("RESGATAR ATAQUES -> usuario=" + usuarioId
                + ", nivel=" + usuario.getMissaoDiariaNivelAtaquesEspeciais()
                + ", concluida=" + usuario.isMissaoDiariaAtaquesConcluida()
                + ", atual=" + usuario.getMissaoDiariaAtaquesEspeciaisAtual());
       */
        int nivelDano = normalizarNivelMissao(usuario.getMissaoDiariaNivelDano());
        int nivelAtaques = normalizarNivelMissao(usuario.getMissaoDiariaNivelAtaquesEspeciais());

        // corrige também no objeto, caso tenha vindo 0 do banco
        usuario.setMissaoDiariaNivelDano(nivelDano);
        usuario.setMissaoDiariaNivelAtaquesEspeciais(nivelAtaques);
        usuarioRepository.save(usuario);

        long objetivoDano = calcularObjetivoDano(usuario, nivelDano);
        int recompensaDano = calcularRecompensaDano(usuario, nivelDano);

        int objetivoAtaques = calcularObjetivoAtaque(usuario, nivelAtaques);
        int recompensaAtaques = calcularRecompensaAtaque(usuario, nivelAtaques);

        dto.setDanoAtual(usuario.getMissaoDiariaDanoAtual());
        dto.setDanoObjetivo(objetivoDano);
        dto.setNivelDano(nivelDano);
        dto.setRecompensaDano(recompensaDano);
       // dto.setPodeResgatarDano(usuario.getMissaoDiariaDanoAtual() >= objetivoDano);

        dto.setAtaquesAtual(usuario.getMissaoDiariaAtaquesEspeciaisAtual());
        dto.setAtaquesObjetivo(objetivoAtaques);
        dto.setNivelAtaques(nivelAtaques);
        dto.setRecompensaAtaques(recompensaAtaques);
       // dto.setPodeResgatarAtaques(usuario.getMissaoDiariaAtaquesEspeciaisAtual() >= objetivoAtaques);
        
        dto.setPodeResgatarDano(
        	    !usuario.isMissaoDiariaDanoConcluida()
        	    && usuario.getMissaoDiariaDanoAtual() >= objetivoDano
        	);

        	dto.setPodeResgatarAtaques(
        	    !usuario.isMissaoDiariaAtaquesConcluida()
        	    && usuario.getMissaoDiariaAtaquesEspeciaisAtual() >= objetivoAtaques
        	);

        return dto;
    }
    
    @Transactional
    public MissaoDiariaDTO resgatarMissaoDano(Long usuarioId) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);
        
        int nivelAtual = usuario.getMissaoDiariaNivelDano();

        if (usuario.isMissaoDiariaDanoConcluida()) {
            return buscarMissaoDiaria(usuarioId);
        }

        long objetivo = calcularObjetivoDano(usuario, nivelAtual);
        int recompensa = calcularRecompensaDano(usuario, nivelAtual);

        if (usuario.getMissaoDiariaDanoAtual() < objetivo) {
            throw new RuntimeException("Missão de dano ainda não concluída.");
        }

        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(recompensa)));
        referidosService.adicionarGanho(usuario, BigDecimal.valueOf(recompensa));
        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, BigDecimal.valueOf(recompensa));
        

        if (nivelAtual < 5) {
            usuario.setMissaoDiariaNivelDano(nivelAtual + 1);
        } else {
            usuario.setMissaoDiariaDanoConcluida(true);
        }

        usuarioRepository.saveAndFlush(usuario);
        return buscarMissaoDiaria(usuarioId);
    }
   
    @Transactional
    public MissaoDiariaDTO resgatarMissaoAtaques(Long usuarioId) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);
        
        int nivelAtual = usuario.getMissaoDiariaNivelAtaquesEspeciais();

        // se já concluiu hoje, não explode erro
        if (usuario.isMissaoDiariaAtaquesConcluida()) {
            return buscarMissaoDiaria(usuarioId);
        }

        int objetivo = calcularObjetivoAtaque(usuario, nivelAtual);
        int recompensa = calcularRecompensaAtaque(usuario, nivelAtual);

        if (usuario.getMissaoDiariaAtaquesEspeciaisAtual() < objetivo) {
            throw new RuntimeException("Missão de ataques especiais ainda não concluída.");
        }

        // paga primeiro
        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(recompensa)));
        referidosService.adicionarGanho(usuario, BigDecimal.valueOf(recompensa));
        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, BigDecimal.valueOf(recompensa));

        if (nivelAtual < 5) {
            usuario.setMissaoDiariaNivelAtaquesEspeciais(nivelAtual + 1);
        } else {
            // nível 5: paga e trava
            usuario.setMissaoDiariaAtaquesConcluida(true);
        }

        usuarioRepository.saveAndFlush(usuario);
        return buscarMissaoDiaria(usuarioId);
    }
    /*
   
    */
    //====================== BUSCA ======================

    private UsuarioBossBattle buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));
    }

    //====================== OBJETIVOS ======================

    private long calcularObjetivoDano(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        long baseDano;

        if (nivelUsuario < 25) baseDano = 100L;
        else if (nivelUsuario < 50) baseDano = 300L;
        else if (nivelUsuario < 75) baseDano = 500L;
        else if (nivelUsuario < 100) baseDano = 700L;
        else if (nivelUsuario < 125) baseDano = 900L;
        else if (nivelUsuario < 150) baseDano = 1_000L;
        else if (nivelUsuario < 175) baseDano = 1_200L;
        else if (nivelUsuario < 200) baseDano = 1_500L;
        else if (nivelUsuario < 225) baseDano = 1_700L;
        else if (nivelUsuario < 250) baseDano = 1_900L;
        else if (nivelUsuario < 275) baseDano = 2_100L;
        else if (nivelUsuario < 300) baseDano = 2_500L;
        else if (nivelUsuario < 325) baseDano = 3_000L;
        else if (nivelUsuario < 350) baseDano = 3_500L;
        else if (nivelUsuario < 375) baseDano = 4_000L;
        else if (nivelUsuario < 400) baseDano = 4_500L;
        else if (nivelUsuario < 425) baseDano = 5_000L;
        else if (nivelUsuario < 450) baseDano = 5_500L;
        else if (nivelUsuario < 475) baseDano = 6_000L;
        else if (nivelUsuario < 500) baseDano = 6_500L;
        else if (nivelUsuario < 525) baseDano = 7_000L;
        else if (nivelUsuario < 550) baseDano = 7_500L;
        else if (nivelUsuario < 575) baseDano = 8_000L;
        else if (nivelUsuario < 600) baseDano = 8_500L;
        else if (nivelUsuario < 625) baseDano = 9_000L;
        else if (nivelUsuario < 650) baseDano = 9_500L;
        else if (nivelUsuario < 675) baseDano = 10_000L;
        else if (nivelUsuario < 700) baseDano = 10_500L;
        else if (nivelUsuario < 725) baseDano = 11_000L;
        else if (nivelUsuario < 750) baseDano = 11_500L;
        else if (nivelUsuario < 775) baseDano = 12_000L;
        else if (nivelUsuario < 800) baseDano = 12_500L;
        else if (nivelUsuario < 825) baseDano = 13_000L;
        else if (nivelUsuario < 850) baseDano = 13_500L;
        else if (nivelUsuario < 875) baseDano = 14_000L;
        else if (nivelUsuario < 900) baseDano = 14_500L;
        else if (nivelUsuario < 925) baseDano = 15_000L;
        else if (nivelUsuario < 950) baseDano = 15_500L;
        else if (nivelUsuario < 975) baseDano = 16_000L;
        else if (nivelUsuario < 1_000) baseDano = 16_500L;

        else baseDano = 17_000L;

        switch (nivelMissao) {
            case 1:
                return baseDano;
            case 2:
                return baseDano * 2;
            case 3:
                return baseDano * 3;
            case 4:
                return baseDano * 4;
            case 5:
                return baseDano * 5;
            default:
                return baseDano;
        }
    }
    private int calcularObjetivoAtaque(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int baseAtaques;

        
     
        if (nivelUsuario < 100) baseAtaques = 5;
        else if (nivelUsuario < 200) baseAtaques = 5;
        else if (nivelUsuario < 300) baseAtaques = 5;
        else if (nivelUsuario < 400) baseAtaques = 5;
        else if (nivelUsuario < 500) baseAtaques = 5;
        else if (nivelUsuario < 600) baseAtaques = 5;
        else if (nivelUsuario < 700) baseAtaques = 5;
        else if (nivelUsuario < 800) baseAtaques = 5;
        else if (nivelUsuario < 900) baseAtaques = 5;
        else baseAtaques = 5;

        switch (nivelMissao) {
            case 1:
                return baseAtaques;
            case 2:
                return baseAtaques * 2;
            case 3:
                return baseAtaques * 3;
            case 4:
                return baseAtaques * 4;
            case 5:
                return baseAtaques * 5;
            default:
                return baseAtaques;
        }
    }
    //====================== RECOMPENSAS ======================

    private int calcularRecompensaDano(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int base;

        if (nivelUsuario < 25) base = 400;
        else if (nivelUsuario < 50) base = 450;
        else if (nivelUsuario < 75) base = 500;
        else if (nivelUsuario < 100) base = 550;
        else if (nivelUsuario < 125) base = 600;
        else if (nivelUsuario < 150) base = 650;
        else if (nivelUsuario < 175) base = 700;
        else if (nivelUsuario < 200) base = 750;
        else if (nivelUsuario < 225) base = 800;
        else if (nivelUsuario < 250) base = 850;
        else if (nivelUsuario < 275) base = 900;
        else if (nivelUsuario < 300) base = 950;
        else if (nivelUsuario < 325) base = 1_000;
        else if (nivelUsuario < 350) base = 1_050;
        else if (nivelUsuario < 375) base = 1_100;
        else if (nivelUsuario < 400) base = 1_150;
        else if (nivelUsuario < 425) base = 1_200;
        else if (nivelUsuario < 450) base = 1_250;
        else if (nivelUsuario < 475) base = 1_300;
        else if (nivelUsuario < 500) base = 1_350;
        else if (nivelUsuario < 525) base = 1_400;
        else if (nivelUsuario < 550) base = 1_450;
        else if (nivelUsuario < 575) base = 1_500;
        else if (nivelUsuario < 600) base = 1_550;
        else if (nivelUsuario < 625) base = 1_600;
        else if (nivelUsuario < 650) base = 1_650;
        else if (nivelUsuario < 675) base = 1_700;
        else if (nivelUsuario < 700) base = 1_750;
        else if (nivelUsuario < 725) base = 1_800;
        else if (nivelUsuario < 750) base = 1_850;
        else if (nivelUsuario < 775) base = 1_900;
        else if (nivelUsuario < 800) base = 1_950;
        else if (nivelUsuario < 825) base = 2_000;
        else if (nivelUsuario < 850) base = 2_050;
        else if (nivelUsuario < 875) base = 2_100;
        else if (nivelUsuario < 900) base = 2_150;
        else if (nivelUsuario < 925) base = 2_200;
        else if (nivelUsuario < 950) base = 2_250;
        else if (nivelUsuario < 975) base = 2_300;
        else if (nivelUsuario < 1_000) base = 2_350;

        else base = 3_000;

        switch (nivelMissao) {
            case 1:
                return base;
            case 2:
                return (int) (base * 1.5);
            case 3:
                return base * 2;
            case 4:
                return (int) (base * 2.5);
            case 5:
                return base * 3;
            default:
                return base;
        }
    }
    private int calcularRecompensaAtaque(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int base;

        if (nivelUsuario < 100) base = 200;
        else if (nivelUsuario < 200) base = 300;
        else if (nivelUsuario < 300) base = 400;
        else if (nivelUsuario < 400) base = 500;
        else if (nivelUsuario < 500) base = 600;
        else if (nivelUsuario < 600) base = 700;
        else if (nivelUsuario < 700) base = 800;
        else if (nivelUsuario < 800) base = 900;
        else if (nivelUsuario < 900) base = 1000;
        else base = 1200;

        switch (nivelMissao) {
            case 1:
                return base;
            case 2:
                return (int) (base * 1.5);
            case 3:
                return base * 2;
            case 4:
                return (int) (base * 2.5);
            case 5:
                return base * 3;
            default:
                return base;
        }
    }

    private int normalizarNivelMissao(int nivelMissao) {
        if (nivelMissao < 1) {
            return 1;
        }
        if (nivelMissao > 5) {
            return 5;
        }
        return nivelMissao;
    }
    
    
    private void validarResetDiario(UsuarioBossBattle usuario) {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        if (usuario.getMissaoDiariaDataReset() == null || !hoje.equals(usuario.getMissaoDiariaDataReset())) {
            resetarMissaoDiaria(usuario, hoje);
            usuarioRepository.saveAndFlush(usuario);
        }
    }
    //====================== ATUALIZAÇÕES ======================

    @Transactional
    public UsuarioBossBattle atualizarProgressoDano(Long usuarioId, long dano) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);
        
        usuario.setMissaoDiariaDanoAtual(usuario.getMissaoDiariaDanoAtual() + dano);

        System.out.println("Usuario-" + usuario.getUsername() +
                " Tem um total dano de " + usuario.getMissaoDiariaDanoAtual());

        return usuarioRepository.save(usuario);
    }
    @Transactional
    public UsuarioBossBattle atualizarProgressoQuantidade(Long usuarioId, int quantidade) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);
        
        usuario.setMissaoDiariaAtaquesEspeciaisAtual(
                usuario.getMissaoDiariaAtaquesEspeciaisAtual() + quantidade
        );

        System.out.println("Usuario-" + usuario.getUsername() +
                " Tem um total ataque de " + usuario.getMissaoDiariaAtaquesEspeciaisAtual());

        return usuarioRepository.save(usuario);
    }
}