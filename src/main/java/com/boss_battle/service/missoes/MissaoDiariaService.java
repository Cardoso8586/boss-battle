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

     // missão de dano
     usuario.setMissaoDiariaNivelDano(1);
     usuario.setMissaoDiariaDanoAtual(0L);
     usuario.setMissaoDiariaDanoConcluida(false);

     // missão de ataques especiais
     usuario.setMissaoDiariaNivelAtaquesEspeciais(1);
     usuario.setMissaoDiariaAtaquesEspeciaisAtual(0);
     usuario.setMissaoDiariaAtaquesConcluida(false);

     // missão PTC
     usuario.setMissaoDiariaNivelPtc(1);
     usuario.setMissaoDiariaPtcAtual(0);
     usuario.setMissaoDiariaPtcConcluida(false);

  
     // missão caçador de recompensas
     usuario.setMissaoDiariaNivelCacadorRecompensas(1);
     usuario.setMissaoDiariaCacadorRecompensasAtual(0);
     usuario.setMissaoDiariaCacadorRecompensasConcluida(false);
     
     // data reset
     usuario.setMissaoDiariaDataReset(hoje);
 }
 //==============================================================================================
//==============================================================================================
 public MissaoDiariaDTO buscarMissaoDiaria(Long usuarioId) {
	    UsuarioBossBattle usuario = buscarUsuario(usuarioId);
	    validarResetDiario(usuario);

	    MissaoDiariaDTO dto = new MissaoDiariaDTO();

	    int nivelDano = normalizarNivelMissao(usuario.getMissaoDiariaNivelDano());
	    int nivelAtaques = normalizarNivelMissao(usuario.getMissaoDiariaNivelAtaquesEspeciais());
	    int nivelPtc = normalizarNivelMissao(usuario.getMissaoDiariaNivelPtc());
        int nivelCacador =normalizarNivelMissao(usuario.getMissaoDiariaNivelCacadorRecompensas());
	    // corrige também no objeto, caso tenha vindo 0 do banco
	    usuario.setMissaoDiariaNivelDano(nivelDano);
	    usuario.setMissaoDiariaNivelAtaquesEspeciais(nivelAtaques);
	    usuario.setMissaoDiariaNivelPtc(nivelPtc);

	    usuarioRepository.save(usuario);
        //missão dano
	    long objetivoDano = calcularObjetivoDano(usuario, nivelDano);
	    int recompensaDano = calcularRecompensaDano(usuario, nivelDano);
        //missão ataques
	    int objetivoAtaques = calcularObjetivoAtaque(usuario, nivelAtaques);
	    int recompensaAtaques = calcularRecompensaAtaque(usuario, nivelAtaques);
        //missão ptc
	    int objetivoPtc = calcularObjetivoPtc(nivelPtc);
	    int recompensaPtc = calcularRecompensaPtc(usuario, nivelPtc);
	    //missão caçador de reconpensas
		int objetivoCacador =calcularObjetivoCacadorRecompensas(nivelCacador);
		int recompensaCacador =calcularRecompensaCacadorRecompensas(usuario,nivelCacador);


	    // missão dano
	    dto.setDanoAtual(usuario.getMissaoDiariaDanoAtual());
	    dto.setDanoObjetivo(objetivoDano);
	    dto.setNivelDano(nivelDano);
	    dto.setRecompensaDano(recompensaDano);

	    dto.setPodeResgatarDano(
	            !usuario.isMissaoDiariaDanoConcluida()
	            && usuario.getMissaoDiariaDanoAtual() >= objetivoDano
	    );

	    // missão ataques especiais
	    dto.setAtaquesAtual(usuario.getMissaoDiariaAtaquesEspeciaisAtual());
	    dto.setAtaquesObjetivo(objetivoAtaques);
	    dto.setNivelAtaques(nivelAtaques);
	    dto.setRecompensaAtaques(recompensaAtaques);

	    dto.setPodeResgatarAtaques(
	            !usuario.isMissaoDiariaAtaquesConcluida()
	            && usuario.getMissaoDiariaAtaquesEspeciaisAtual() >= objetivoAtaques
	    );

	    // missão PTC
	    dto.setPtcAtual(usuario.getMissaoDiariaPtcAtual());
	    dto.setPtcObjetivo(objetivoPtc);
	    dto.setNivelPtc(nivelPtc);
	    dto.setRecompensaPtc(recompensaPtc);

	    dto.setPodeResgatarPtc(
	            !usuario.isMissaoDiariaPtcConcluida()
	            && usuario.getMissaoDiariaPtcAtual() >= objetivoPtc
	    );
 
	   // missão caçadpr de recompensas
	   
	   dto.setCacadorAtual(usuario.getMissaoDiariaCacadorRecompensasAtual());
	   dto.setCacadorObjetivo(objetivoCacador);
	   dto.setNivelCacador(nivelCacador);
	   dto.setRecompensaCacador(recompensaCacador);
	   dto.setPodeResgatarCacador(!usuario.isMissaoDiariaCacadorRecompensasConcluida()
	        &&usuario.getMissaoDiariaCacadorRecompensasAtual()>= objetivoCacador);
	   
	    return dto;
	}
    //resgatarMissaoDano
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
   //resgatarMissaoAtaques
    @Transactional
    public MissaoDiariaDTO resgatarMissaoAtaques(Long usuarioId) {
    	  UsuarioBossBattle usuario = usuarioRepository.buscarPorIdComLock(usuarioId)
          	    .orElseThrow();
          
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
    
    //resgatarMissaoPtc
    @Transactional
    public MissaoDiariaDTO resgatarMissaoPtc(Long usuarioId) {
        UsuarioBossBattle usuario = usuarioRepository.buscarPorIdComLock(usuarioId)
        	    .orElseThrow();
        
        validarResetDiario(usuario);

        int nivelAtual = normalizarNivelMissao(usuario.getMissaoDiariaNivelPtc());

        if (usuario.isMissaoDiariaPtcConcluida()) {
            return buscarMissaoDiaria(usuarioId);
        }

        int objetivo = calcularObjetivoPtc(nivelAtual);
        int recompensa = calcularRecompensaPtc(usuario, nivelAtual);

        if (usuario.getMissaoDiariaPtcAtual() < objetivo) {
            throw new RuntimeException("Missão de visualizar PTC ainda não concluída.");
        }

        usuario.setBossCoins(usuario.getBossCoins().add(BigDecimal.valueOf(recompensa)));
        referidosService.adicionarGanho(usuario, BigDecimal.valueOf(recompensa));
        ultimoValorRecebidoService.setUltimoValorRecebido(usuario, BigDecimal.valueOf(recompensa));

        if (nivelAtual < 5) {
            usuario.setMissaoDiariaNivelPtc(nivelAtual + 1);
        } else {
            usuario.setMissaoDiariaPtcConcluida(true);
        }

        usuarioRepository.saveAndFlush(usuario);
        return buscarMissaoDiaria(usuarioId);
    }
    /*
   
    */
    //resgatar resgatarMissaoCacadorRecompensas
    @Transactional
    public MissaoDiariaDTO resgatarMissaoCacadorRecompensas(
            Long usuarioId) {

        UsuarioBossBattle usuario =
                usuarioRepository.buscarPorIdComLock(usuarioId)
                        .orElseThrow();

        validarResetDiario(usuario);

        int nivelAtual =
                normalizarNivelMissao(
                        usuario.getMissaoDiariaNivelCacadorRecompensas());

        if (usuario.isMissaoDiariaCacadorRecompensasConcluida()) {
            return buscarMissaoDiaria(usuarioId);
        }

        int objetivo =
                calcularObjetivoCacadorRecompensas(nivelAtual);

        int recompensa =
                calcularRecompensaCacadorRecompensas(
                        usuario,
                        nivelAtual);

        if (usuario.getMissaoDiariaCacadorRecompensasAtual()
                < objetivo) {

            throw new RuntimeException(
                    "Missão Caçador de Recompensas ainda não concluída.");
        }

        usuario.setBossCoins(
                usuario.getBossCoins()
                        .add(BigDecimal.valueOf(recompensa)));

        referidosService.adicionarGanho(
                usuario,
                BigDecimal.valueOf(recompensa));

        ultimoValorRecebidoService.setUltimoValorRecebido(
                usuario,
                BigDecimal.valueOf(recompensa));

        if (nivelAtual < 5) {

            usuario.setMissaoDiariaNivelCacadorRecompensas(
                    nivelAtual + 1);

        } else {

            usuario.setMissaoDiariaCacadorRecompensasConcluida(true);
        }

        usuarioRepository.saveAndFlush(usuario);

        return buscarMissaoDiaria(usuarioId);
    }
    //====================== BUSCA ======================

    private UsuarioBossBattle buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));
    }
    //====================== OBJETIVOS ======================
   
    //objetivo ptc
    private int calcularObjetivoPtc(int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        switch (nivelMissao) {
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 30;
            case 4:
                return 40;
            case 5:
                return 50;
            default:
                return 50;
        }
    }

    //objetivo dano
    private long calcularObjetivoDano(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        long baseDano;
     

        if (nivelUsuario < 25) baseDano = 100L;
        else if (nivelUsuario < 50) baseDano = 450L;
        else if (nivelUsuario < 75) baseDano = 900L;
        else if (nivelUsuario < 100) baseDano = 1_050L;
        else if (nivelUsuario < 125) baseDano = 1_200L;
        else if (nivelUsuario < 150) baseDano = 1_350L;
        else if (nivelUsuario < 175) baseDano = 1_500L;
        else if (nivelUsuario < 200) baseDano = 1_650L;
        else if (nivelUsuario < 225) baseDano = 1_800L;
        else if (nivelUsuario < 250) baseDano = 1_950L;
        else if (nivelUsuario < 275) baseDano = 2_100L;
        else if (nivelUsuario < 300) baseDano = 2_250L;
        else if (nivelUsuario < 325) baseDano = 2_400L;
        else if (nivelUsuario < 350) baseDano = 2_550L;
        else if (nivelUsuario < 375) baseDano = 2_700L;
        else if (nivelUsuario < 400) baseDano = 2_850L;
        else if (nivelUsuario < 425) baseDano = 3_000L;
        else if (nivelUsuario < 450) baseDano = 3_150L;
        else if (nivelUsuario < 475) baseDano = 3_300L;
        else if (nivelUsuario < 500) baseDano = 3_450L;
        else if (nivelUsuario < 525) baseDano = 3_600L;
        else if (nivelUsuario < 550) baseDano = 3_750L;
        else if (nivelUsuario < 575) baseDano = 3_900L;
        else if (nivelUsuario < 600) baseDano = 4_050L;
        else if (nivelUsuario < 625) baseDano = 4_200L;
        else if (nivelUsuario < 650) baseDano = 4_350L;
        else if (nivelUsuario < 675) baseDano = 4_500L;
        else if (nivelUsuario < 700) baseDano = 4_650L;
        else if (nivelUsuario < 725) baseDano = 4_800L;
        else if (nivelUsuario < 750) baseDano = 4_950L;
        else if (nivelUsuario < 775) baseDano = 5_100L;
        else if (nivelUsuario < 800) baseDano = 5_250L;
        else if (nivelUsuario < 825) baseDano = 5_400L;
        else if (nivelUsuario < 850) baseDano = 5_550L;
        else if (nivelUsuario < 875) baseDano = 5_700L;
        else if (nivelUsuario < 900) baseDano = 5_850L;
        else if (nivelUsuario < 925) baseDano = 6_000L;
        else if (nivelUsuario < 950) baseDano = 6_150L;
        else if (nivelUsuario < 975) baseDano = 6_300L;
        else if (nivelUsuario < 1000) baseDano = 6_450L;
        else baseDano = 6_600L;
        
        
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
    //objetivo ataque
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
    
    
    //objetivo caçador de reconpensas
    private int calcularObjetivoCacadorRecompensas(int nivelMissao) {

        nivelMissao = normalizarNivelMissao(nivelMissao);

        switch (nivelMissao) {
            case 1:
                return 1;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 15;
            case 5:
                return 20;
            default:
                return 20;
        }
    }
    //====================== Calcular RECOMPENSAS ======================
    
    //calcular caçador de recompensas
    private int calcularRecompensaCacadorRecompensas(
            UsuarioBossBattle usuario,
            int nivelMissao) {

        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();

        int base = 30 + (int)(nivelUsuario / 25) * 10;

        if (base > 1000) {
            base = 1000;
        }

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
    private int calcularRecompensaPtc(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int base;

        if (nivelUsuario < 25) base = 50;
        else if (nivelUsuario < 50) base = 60;
        else if (nivelUsuario < 75) base = 70;
        else if (nivelUsuario < 100) base = 80;
        else if (nivelUsuario < 125) base = 90;
        else if (nivelUsuario < 150) base = 100;
        else if (nivelUsuario < 175) base = 110;
        else if (nivelUsuario < 200) base = 120;
        else if (nivelUsuario < 225) base = 130;
        else if (nivelUsuario < 250) base = 140;
        else if (nivelUsuario < 275) base = 150;
        else if (nivelUsuario < 300) base = 160;
        else if (nivelUsuario < 325) base = 170;
        else if (nivelUsuario < 350) base = 180;
        else if (nivelUsuario < 375) base = 190;
        else if (nivelUsuario < 400) base = 200;
        else if (nivelUsuario < 425) base = 210;
        else if (nivelUsuario < 450) base = 220;
        else if (nivelUsuario < 475) base = 230;
        else if (nivelUsuario < 500) base = 240;
        else if (nivelUsuario < 525) base = 250;
        else if (nivelUsuario < 550) base = 260;
        else if (nivelUsuario < 575) base = 270;
        else if (nivelUsuario < 600) base = 280;
        else if (nivelUsuario < 625) base = 290;
        else if (nivelUsuario < 650) base = 300;
        else if (nivelUsuario < 675) base = 310;
        else if (nivelUsuario < 700) base = 320;
        else if (nivelUsuario < 725) base = 330;
        else if (nivelUsuario < 750) base = 340;
        else if (nivelUsuario < 775) base = 350;
        else if (nivelUsuario < 800) base = 360;
        else if (nivelUsuario < 825) base = 370;
        else if (nivelUsuario < 850) base = 380;
        else if (nivelUsuario < 875) base = 390;
        else if (nivelUsuario < 900) base = 400;
        else if (nivelUsuario < 925) base = 410;
        else if (nivelUsuario < 950) base = 420;
        else if (nivelUsuario < 975) base = 430;
        else if (nivelUsuario < 1000) base = 440;

        else base = 500;

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
    
    //calcular Dano
    private int calcularRecompensaDano(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int base;

        if (nivelUsuario < 25) base = 30;
        else if (nivelUsuario < 50) base = 40;
        else if (nivelUsuario < 75) base = 50;
        else if (nivelUsuario < 100) base = 60;
        else if (nivelUsuario < 125) base = 70;
        else if (nivelUsuario < 150) base = 80;
        else if (nivelUsuario < 175) base = 90;
        else if (nivelUsuario < 200) base = 100;
        else if (nivelUsuario < 225) base = 110;
        else if (nivelUsuario < 250) base = 120;
        else if (nivelUsuario < 275) base = 130;
        else if (nivelUsuario < 300) base = 140;
        else if (nivelUsuario < 325) base = 150;
        else if (nivelUsuario < 350) base = 160;
        else if (nivelUsuario < 375) base = 170;
        else if (nivelUsuario < 400) base = 180;
        else if (nivelUsuario < 425) base = 190;
        else if (nivelUsuario < 450) base = 200;
        else if (nivelUsuario < 475) base = 210;
        else if (nivelUsuario < 500) base = 220;
        else if (nivelUsuario < 525) base = 230;
        else if (nivelUsuario < 550) base = 240;
        else if (nivelUsuario < 575) base = 250;
        else if (nivelUsuario < 600) base = 260;
        else if (nivelUsuario < 625) base = 270;
        else if (nivelUsuario < 650) base = 280;
        else if (nivelUsuario < 675) base = 290;
        else if (nivelUsuario < 700) base = 300;
        else if (nivelUsuario < 725) base = 310;
        else if (nivelUsuario < 750) base = 320;
        else if (nivelUsuario < 775) base = 330;
        else if (nivelUsuario < 800) base = 340;
        else if (nivelUsuario < 825) base = 350;
        else if (nivelUsuario < 850) base = 360;
        else if (nivelUsuario < 875) base = 370;
        else if (nivelUsuario < 900) base = 380;
        else if (nivelUsuario < 925) base = 390;
        else if (nivelUsuario < 950) base = 400;
        else if (nivelUsuario < 975) base = 410;
        else if (nivelUsuario < 1000) base = 420;

        else base = 500;
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
    // Calcular Ataque
    private int calcularRecompensaAtaque(UsuarioBossBattle usuario, int nivelMissao) {
        nivelMissao = normalizarNivelMissao(nivelMissao);

        long nivelUsuario = usuario.getNivel();
        int base;

        if (nivelUsuario < 25) base = 20;
        else if (nivelUsuario < 50) base = 30;
        else if (nivelUsuario < 75) base = 40;
        else if (nivelUsuario < 100) base = 50;
        else if (nivelUsuario < 125) base = 60;
        else if (nivelUsuario < 150) base = 70;
        else if (nivelUsuario < 175) base = 80;
        else if (nivelUsuario < 200) base = 90;
        else if (nivelUsuario < 225) base = 100;
        else if (nivelUsuario < 250) base = 110;
        else if (nivelUsuario < 275) base = 120;
        else if (nivelUsuario < 300) base = 130;
        else if (nivelUsuario < 325) base = 140;
        else if (nivelUsuario < 350) base = 150;
        else if (nivelUsuario < 375) base = 160;
        else if (nivelUsuario < 400) base = 170;
        else if (nivelUsuario < 425) base = 180;
        else if (nivelUsuario < 450) base = 190;
        else if (nivelUsuario < 475) base = 200;
        else if (nivelUsuario < 500) base = 210;
        else if (nivelUsuario < 525) base = 220;
        else if (nivelUsuario < 550) base = 230;
        else if (nivelUsuario < 575) base = 240;
        else if (nivelUsuario < 600) base = 250;
        else if (nivelUsuario < 625) base = 260;
        else if (nivelUsuario < 650) base = 270;
        else if (nivelUsuario < 675) base = 280;
        else if (nivelUsuario < 700) base = 290;
        else if (nivelUsuario < 725) base = 300;
        else if (nivelUsuario < 750) base = 310;
        else if (nivelUsuario < 775) base = 320;
        else if (nivelUsuario < 800) base = 330;
        else if (nivelUsuario < 825) base = 340;
        else if (nivelUsuario < 850) base = 350;
        else if (nivelUsuario < 875) base = 360;
        else if (nivelUsuario < 900) base = 370;
        else if (nivelUsuario < 925) base = 380;
        else if (nivelUsuario < 950) base = 390;
        else if (nivelUsuario < 975) base = 400;
        else if (nivelUsuario < 1000) base = 410;

        // reduzido também nos níveis altos

        else if (nivelUsuario < 1500) base = 600;
        else if (nivelUsuario < 2000) base = 800;
        else if (nivelUsuario < 2500) base = 1000;
        else if (nivelUsuario < 3000) base = 1200;

        else base = 1500;

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
    public UsuarioBossBattle atualizarProgressoPtc(Long usuarioId, int quantidade) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);

        int atualAntes = usuario.getMissaoDiariaPtcAtual();

        usuario.setMissaoDiariaPtcAtual(atualAntes + quantidade);

        UsuarioBossBattle salvo = usuarioRepository.saveAndFlush(usuario);

        System.out.println("📺 PTC ATUALIZADO");
        System.out.println("Usuário: " + salvo.getUsername()
        +  " Tem um total de "
        + salvo.getMissaoDiariaPtcAtual()
        + "PTC visualizados");
       

        return salvo;
    }
    
    @Transactional
    public UsuarioBossBattle atualizarProgressoDano(Long usuarioId, long dano) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);
        
        usuario.setMissaoDiariaDanoAtual(usuario.getMissaoDiariaDanoAtual() + dano);

        System.out.println("Usuario-" + usuario.getUsername()
        +" Tem um total dano de -  " 
        + usuario.getMissaoDiariaDanoAtual());

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
                " Tem um total ataque de -  " + usuario.getMissaoDiariaAtaquesEspeciaisAtual());

        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public UsuarioBossBattle atualizarProgressoCacadorRecompensas(Long usuarioId, int quantidade) {
        UsuarioBossBattle usuario = buscarUsuario(usuarioId);

        validarResetDiario(usuario);

        int atualAntes = usuario.getMissaoDiariaCacadorRecompensasAtual();

        usuario.setMissaoDiariaCacadorRecompensasAtual(atualAntes + quantidade);

        UsuarioBossBattle salvo = usuarioRepository.saveAndFlush(usuario);

        System.out.println("🏆 CAÇADOR DE RECOMPENSAS ATUALIZADO");
        System.out.println("Usuário: " + salvo.getUsername()
        +  " Tem um total de "
        + salvo.getMissaoDiariaCacadorRecompensasAtual() 
        + " - Reconpensas");
       

        return salvo;
    }
    
}//--->