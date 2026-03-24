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

@Service
public class MissaoDiariaService {


	
	
    //====================== RECOMPENSAS ======================

    //========================================================

    private final UsuarioBossBattleRepository usuarioRepository;

    public MissaoDiariaService(UsuarioBossBattleRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
    
    /*
 @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
 @Transactional
 public void resetarMissoesDiariasAutomaticamente() {
     try {
         System.out.println("🔥 INICIOU: " + LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

         List<UsuarioBossBattle> usuarios = usuarioRepository.findAll();
         LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

         for (UsuarioBossBattle usuario : usuarios) {
             resetarMissaoDiaria(usuario, hoje);
         }

         usuarioRepository.saveAll(usuarios);

         System.out.println("✅ FINALIZOU: " + LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
     } catch (Exception e) {
         System.out.println("❌ ERRO NO SCHEDULER");
         e.printStackTrace();
     }
 }
 */

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

        
        if (nivelUsuario < 100) baseDano = 100L;
        else if (nivelUsuario < 200) baseDano = 500L;
        else if (nivelUsuario < 300) baseDano = 700L;
        else if (nivelUsuario < 400) baseDano = 1_500L;
        else if (nivelUsuario < 500) baseDano = 2_200L;
        else if (nivelUsuario < 600) baseDano = 3_000L;
        else if (nivelUsuario < 700) baseDano = 4_500L;
        else if (nivelUsuario < 800) baseDano = 5_500L;
        else if (nivelUsuario < 900) baseDano = 6_000L;
        else if (nivelUsuario < 1_000) baseDano = 7_000L;
        else if (nivelUsuario < 1_500) baseDano = 10_000L;
        else baseDano = 15_000L;

        switch (nivelMissao) {
            case 1:
                return baseDano;
            case 2:
                return baseDano * 3;
            case 3:
                return baseDano * 4;
            case 4:
                return baseDano * 5;
            case 5:
                return baseDano * 6;
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

        if (nivelUsuario < 100) base = 400;
        else if (nivelUsuario < 200) base = 500;
        else if (nivelUsuario < 300) base = 600;
        else if (nivelUsuario < 400) base = 700;
        else if (nivelUsuario < 500) base = 800;
        else if (nivelUsuario < 600) base = 900;
        else if (nivelUsuario < 700) base = 1_000;
        else if (nivelUsuario < 800) base = 1_200;
        else if (nivelUsuario < 900) base = 1_500;
        else base = 2500;

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