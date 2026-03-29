
package com.boss_battle.service.global_boss;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.BossDamageLog;
import com.boss_battle.model.GlobalBossAbaddon;
import com.boss_battle.model.GlobalBossAbissal;
import com.boss_battle.model.GlobalBossAbyssar;
import com.boss_battle.model.GlobalBossArenascor;
import com.boss_battle.model.GlobalBossAzraelPrime;
import com.boss_battle.model.GlobalBossAzuragon;
import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.model.GlobalBossCryophantasm;
import com.boss_battle.model.GlobalBossCyberion;
import com.boss_battle.model.GlobalBossDestruidor;
import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.model.GlobalBossKaelthor;
import com.boss_battle.model.GlobalBossLeviatanAbismo;
import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.model.GlobalBossMalevola;
import com.boss_battle.model.GlobalBossMalphion;
import com.boss_battle.model.GlobalBossMechadron;
import com.boss_battle.model.GlobalBossMorvath;
import com.boss_battle.model.GlobalBossNecrothar;
import com.boss_battle.model.GlobalBossNexarach;
import com.boss_battle.model.GlobalBossNightmare;
import com.boss_battle.model.GlobalBossNoctharion;
import com.boss_battle.model.GlobalBossNoctyr;
import com.boss_battle.model.GlobalBossNoxar;
import com.boss_battle.model.GlobalBossObliquo;
import com.boss_battle.model.GlobalBossOblivar;
import com.boss_battle.model.GlobalBossOblivion;
import com.boss_battle.model.GlobalBossObraserker;
import com.boss_battle.model.GlobalBossOculthar;
import com.boss_battle.model.GlobalBossPuppetrix;
import com.boss_battle.model.GlobalBossPyragon;
import com.boss_battle.model.GlobalBossReflexa;
import com.boss_battle.model.GlobalBossTenebris;
import com.boss_battle.model.GlobalBossThunderon;
import com.boss_battle.model.GlobalBossTrigonBaphydrax;
import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.model.GlobalBossVespera;
import com.boss_battle.model.GlobalBossVesta;
import com.boss_battle.model.GlobalBossZargoth;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BossDamageLogRepository;
import com.boss_battle.repository.BossRewardLockRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.BossDamageLogService;
import com.boss_battle.service.ReferidosRecompensaService;
import com.boss_battle.service.UsuarioBossBattleService;
import com.boss_battle.service.ativar_equipar.PocaoVigorService;
import com.boss_battle.service.auto_ataque.BossAttackService;
import com.boss_battle.service.auto_ataque.RetaguardaService;
import com.boss_battle.service.bosses.AbaddonService;
import com.boss_battle.service.bosses.AbissalService;
import com.boss_battle.service.bosses.AbyssarService;
import com.boss_battle.service.bosses.ArenascorService;
import com.boss_battle.service.bosses.AzraelPrimeService;
import com.boss_battle.service.bosses.AzuragonService;
import com.boss_battle.service.bosses.AzurionService;
import com.boss_battle.service.bosses.CryophantasmService;
import com.boss_battle.service.bosses.CyberionService;
import com.boss_battle.service.bosses.DestruidorService;
import com.boss_battle.service.bosses.DrakthorService;
import com.boss_battle.service.bosses.FlamorService;
import com.boss_battle.service.bosses.GlaciaraService;
import com.boss_battle.service.bosses.GlaciornService;
import com.boss_battle.service.bosses.IgnorathService;
import com.boss_battle.service.bosses.InfernaxService;
import com.boss_battle.service.bosses.KaelthorService;
import com.boss_battle.service.bosses.LeviatanAbismoService;
import com.boss_battle.service.bosses.LyxaraService;
import com.boss_battle.service.bosses.MalevolaService;
import com.boss_battle.service.bosses.MalphionService;
import com.boss_battle.service.bosses.MechadronService;
import com.boss_battle.service.bosses.MorvathService;
import com.boss_battle.service.bosses.NecrotharService;
import com.boss_battle.service.bosses.NexarachService;
import com.boss_battle.service.bosses.NightmareService;
import com.boss_battle.service.bosses.NoctharionService;
import com.boss_battle.service.bosses.NoctyrService;
import com.boss_battle.service.bosses.NoxarService;
import com.boss_battle.service.bosses.ObliquoService;
import com.boss_battle.service.bosses.OblivarService;
import com.boss_battle.service.bosses.OblivionService;
import com.boss_battle.service.bosses.ObraserkerService;
import com.boss_battle.service.bosses.OcultharService;
import com.boss_battle.service.bosses.PuppetrixService;
import com.boss_battle.service.bosses.PyragonService;
import com.boss_battle.service.bosses.ReflexaService;

import com.boss_battle.service.bosses.TenebrisService;
import com.boss_battle.service.bosses.ThunderonService;
import com.boss_battle.service.bosses.TrigonBaphydraxService;
import com.boss_battle.service.bosses.UmbrarService;
import com.boss_battle.service.bosses.UmbraxisService;
import com.boss_battle.service.bosses.VesperaService;
import com.boss_battle.service.bosses.VestaService;
import com.boss_battle.service.bosses.ZargothService;
import com.boss_battle.service.missoes.MissaoDiariaService;
import com.boss_battle.service.missoes.RankingAtaqueEspecialService;



@Service
@Transactional
public class GlobalBossService {
	
    private final IgnorathService ignorathService;
    private final DrakthorService drakthorService;
    private final AzurionService azurionService;
    private final UmbraxisService umbraxisService;
    private final NightmareService nightmareService;
    private final FlamorService flamorService;
    private final OblivarService oblivarService;
    private final LyxaraService lyxaraService;
    private final NoxarService noxarService;
    private final UmbrarService umbrarService;
    private final MorvathService morvathService;
    private final ObliquoService obliquoService;
    private final PyragonService pyragonService;
    private final GlaciornService glaciornService;
    private final ReflexaService reflexaService;
    private final MechadronService mechadronService;
    private final NoctyrService noctyrService;
    private final OblivionService oblivionService;
    private final VesperaService vesperaService;
    private final TenebrisService tenebrisService;
    private final GlaciaraService glaciaraService;
    private final InfernaxService infernaxService;
    private final ThunderonService thunderonService;
    private final NoctharionService noctharionService;
    private final AzraelPrimeService azraelPrimeService;
    private final DestruidorService destruidorService;
    private final TrigonBaphydraxService trigonBaphydraxService;
    private final  MalphionService malphionService;
    private final AbyssarService abyssarService;
    private final NecrotharService necrotharService;
    private final KaelthorService kaelthorService;
    private final AbissalService abissalService;
    private final  LeviatanAbismoService leviatanAbismoService;
    private final ZargothService zargothService;
    private final NexarachService nexarachService;
    private final CyberionService cyberionService;
    private final AzuragonService azuragonService;
    private final OcultharService ocultharService;
    private final PuppetrixService puppetrixService;
    
     private final AbaddonService abaddonService;
     private final VestaService vestaService;
     private final MalevolaService malevolaService;
     private final ObraserkerService obraserkerService;
     private final CryophantasmService cryophantasmService;
     private final ArenascorService arenascorService;
     
    /*
  
   
    */
    
    //------------------------------------------------
    private final BossDamageLogRepository damageLogRepo;
    private final UsuarioBossBattleRepository usuarioRepo;
    private final ReferidosRecompensaService referidosService;
    private final UsuarioBossBattleService usuarioService;
    private final BossAttackService bossAttackService;

	private final PocaoVigorService pocaoVigorService;
    
    private final SpawRandomBossService spawRandomBossService;
    
   
    private final RetaguardaService retaguardaService;
    private final MissaoDiariaService missaoDiariaService;
    
    private final RankingAtaqueEspecialService rankingAtaqueEspecialService;
    
   // private final Random random = new Random();

    public GlobalBossService(
    		//Services dos boses--->
            IgnorathService ignorathService,
            DrakthorService drakthorService,
            AzurionService azurionService,
            UmbraxisService umbraxisService,
            NightmareService nightmareService,
            FlamorService flamorService,
            OblivarService oblivarService,
            LyxaraService lyxaraService ,
            NoxarService noxarService,
            UmbrarService umbrarService,
            MorvathService morvathService,
            ObliquoService obliquoService,
            PyragonService pyragonService,
            GlaciornService glaciornService,
            ReflexaService reflexaService,
            MechadronService mechadronService,
            NoctyrService noctyrService,
            OblivionService oblivionService,
            VesperaService vesperaService,
            TenebrisService tenebrisService,
            GlaciaraService glaciaraService,
            InfernaxService infernaxService,
            ThunderonService thunderonService,
            NoctharionService noctharionService,
            AzraelPrimeService azraelPrimeService,
            DestruidorService destruidorService,
            TrigonBaphydraxService trigonBaphydraxService,
            MalphionService malphionService,
            AbyssarService abyssarService,
            NecrotharService necrotharService,
            KaelthorService kaelthorService,
            AbissalService abissalService,
            LeviatanAbismoService leviatanAbismoService,
            ZargothService zargothService,
             NexarachService nexarachService,
            CyberionService cyberionService,
            AzuragonService azuragonService,
            OcultharService ocultharService,
            PuppetrixService puppetrixService,
            
            AbaddonService abaddonService,
            VestaService vestaService,
            MalevolaService malevolaService,
            ObraserkerService obraserkerService,
            CryophantasmService cryophantasmService,
            ArenascorService arenascorService,
            
            /*
         
           
            */
            
            //Outros Services--->
            BossDamageLogRepository damageLogRepo,
            UsuarioBossBattleRepository usuarioRepo,
            BossRewardLockRepository bossRewardLockRepo,
            ReferidosRecompensaService referidosService,
            UsuarioBossBattleService usuarioService,
            BossAttackService bossAttackService,
            BossDamageLogService bossDamageLogService,
            RetaguardaService retaguardaService,
            PocaoVigorService pocaoVigorService,  
            SpawRandomBossService spawRandomBossService,
            MissaoDiariaService missaoDiariaService,
            RankingAtaqueEspecialService rankingAtaqueEspecialService
            
    ) {
    	this.retaguardaService = retaguardaService;
    	//================ BOSS ===================
        this.ignorathService = ignorathService;
        this.drakthorService = drakthorService;
        this.azurionService = azurionService;
        this.umbraxisService = umbraxisService;
        this.damageLogRepo = damageLogRepo;
        this.usuarioRepo = usuarioRepo;
        this.referidosService = referidosService;
        this.usuarioService = usuarioService;
        this.bossAttackService = bossAttackService;
        this.pocaoVigorService = pocaoVigorService;
        this.nightmareService = nightmareService;
        this.flamorService = flamorService;
        this.oblivarService = oblivarService;
        this.lyxaraService = lyxaraService;
        this.noxarService = noxarService;
        this.umbrarService = umbrarService;
        this.morvathService = morvathService;
        this.obliquoService = obliquoService;
        this.pyragonService = pyragonService;
        this.glaciornService = glaciornService;
        this.reflexaService = reflexaService;
        this.mechadronService = mechadronService;
        this.noctyrService = noctyrService;
        this.oblivionService = oblivionService;
        this.vesperaService = vesperaService;
        this.tenebrisService = tenebrisService;
        this.glaciaraService = glaciaraService;
        this.infernaxService = infernaxService;
        this.thunderonService = thunderonService;
        this.noctharionService = noctharionService;
        this.azraelPrimeService = azraelPrimeService;
        this.destruidorService = destruidorService;
        this.trigonBaphydraxService = trigonBaphydraxService;
        this.malphionService = malphionService;
        this. abyssarService = abyssarService ;
        this.necrotharService = necrotharService;
        this.kaelthorService = kaelthorService; 
        this.abissalService = abissalService;
        this.leviatanAbismoService = leviatanAbismoService;
        this.zargothService = zargothService;
        this.nexarachService = nexarachService;
        this.cyberionService = cyberionService;
        this.azuragonService = azuragonService;
        this.ocultharService = ocultharService;
        this.puppetrixService = puppetrixService;
        
        
        this.abaddonService = abaddonService;
        this.vestaService = vestaService;
        this.malevolaService = malevolaService;
        this.obraserkerService = obraserkerService;
        this.cryophantasmService = cryophantasmService;
        this.arenascorService = arenascorService;
        
        /*
        
       
        */
        
        this.spawRandomBossService = spawRandomBossService;
        this.missaoDiariaService = missaoDiariaService;
        this.rankingAtaqueEspecialService = rankingAtaqueEspecialService;
        
        
    }

    // =============================
    // RETORNA O BOSS ATIVO
    // =============================
    public synchronized BattleBoss getActiveBoss() {
        if (ignorathService.get().isAlive()) return ignorathService.get();
        if (drakthorService.get().isAlive()) return drakthorService.get();
        if (azurionService.get().isAlive()) return azurionService.get();
        if (umbraxisService.get().isAlive()) return umbraxisService.get();
        if (nightmareService.get().isAlive()) return nightmareService.get();
        if (flamorService.get().isAlive()) return flamorService.get();
        if (oblivarService.get().isAlive()) return oblivarService.get();
        if (lyxaraService.get().isAlive()) return lyxaraService.get();
        if (noxarService.get().isAlive()) return noxarService.get();
        if (umbrarService.get().isAlive()) return umbrarService.get();
        if (morvathService.get().isAlive()) return morvathService.get();
        if (obliquoService.get().isAlive()) return obliquoService.get();
        if (pyragonService.get().isAlive()) return pyragonService.get();
        if (glaciornService.get().isAlive()) return glaciornService.get();
        if (reflexaService.get().isAlive()) return reflexaService.get();
        if (mechadronService.get().isAlive()) return mechadronService.get();
        if (noctyrService.get().isAlive()) return noctyrService.get();
        if (oblivionService.get().isAlive()) return oblivionService.get();
        if (vesperaService.get().isAlive()) return vesperaService.get();
        if (tenebrisService.get().isAlive()) return tenebrisService.get();
        if (glaciaraService.get().isAlive()) return glaciaraService.get();
        if (infernaxService.get().isAlive()) return infernaxService.get();
        if (thunderonService.get().isAlive()) return thunderonService.get();
        if (noctharionService.get().isAlive()) return noctharionService.get();
        if (azraelPrimeService.get().isAlive()) return azraelPrimeService.get();
        if (destruidorService.get().isAlive()) return destruidorService.get();
        if (trigonBaphydraxService.get().isAlive()) return trigonBaphydraxService.get();
        if (malphionService.get().isAlive()) return malphionService.get();
        if (abyssarService.get().isAlive()) return abyssarService.get();
        if (necrotharService.get().isAlive()) return necrotharService.get();
        if (kaelthorService.get().isAlive()) return kaelthorService.get();
        if (abissalService.get().isAlive()) return abissalService.get();
        if (leviatanAbismoService.get().isAlive()) return leviatanAbismoService.get();
        if (zargothService.get().isAlive()) return zargothService.get();
        if (nexarachService.get().isAlive()) return nexarachService.get();
        if (cyberionService.get().isAlive()) return cyberionService.get();
        if (azuragonService.get().isAlive()) return azuragonService.get();
        if (ocultharService.get().isAlive()) return ocultharService.get();
        if (puppetrixService.get().isAlive()) return puppetrixService.get();
        
        
        if (abaddonService.get().isAlive()) return abaddonService.get();
        if (vestaService.get().isAlive()) return vestaService.get();
        if (malevolaService.get().isAlive()) return malevolaService.get();
        if (obraserkerService.get().isAlive()) return obraserkerService.get();
        if (cryophantasmService.get().isAlive()) return cryophantasmService.get();
        if (arenascorService.get().isAlive()) return arenascorService.get();
        
        
        
        
        
        /*
      
       
        */
        
        return spawRandomBossService.spawnRandomBoss();
    }

    // =============================
    // HIT NO BOSS ATIVO
    // =============================
    

    public Object hitActiveBoss(long usuarioId) {
        UsuarioBossBattle usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        BattleBoss boss = getActiveBoss();

      if (boss == null || !boss.isAlive()) {
         return Map.of(
        "status", "NO_BOSS",
        "message", "Boss derrotado. Aguarde o respawn."
          );
       }
        
         
        if (!bossAttackService.podeAtacar(usuario)) {
        	
            return Map.of(
                "status", "COOLDOWN",
                "message", "Aguarde para atacar novamente",
                "segundosRestantes", bossAttackService.tempoRestanteSegundos(usuario)
            );
        }
        
        
        
        Long energia = usuario.getEnergiaGuerreiros();

        if (energia == null || energia <= 0) {
            return Map.of(
                "success", false,
                "message", "Sem vigor! Recarregue para continuar."
            );
        }

        else {
        	long ataqueBase = usuario.getAtaqueBase();
        	long ataqueEspecial = retaguardaService.ataqueSurpresaRetaguarda(usuarioId);

        	long damage = ataqueBase + ataqueEspecial;
        	
            missaoDiariaService.atualizarProgressoDano(usuarioId, damage);
            missaoDiariaService.atualizarProgressoQuantidade(usuarioId, 1);
            rankingAtaqueEspecialService.incrementarAtaquesSemanais(usuario);
          
            
            	
            	usuarioRepo.save(usuario);
            
            
        	System.out.println("Usuario -" + usuario.getUsername() +
        	        " atacou -" + boss.getBossName() +
        	        " causando " + damage + " de dano");
        	
        	
        	
        	// 🔥 garante que nunca fique negativo
        	long novaEnergia = Math.max(0, energia - damage);
        	usuario.setEnergiaGuerreiros(novaEnergia);

        	/*
        long ataqueBase = usuario.getAtaqueBase();
        long ataqueEspecial = retaguardaService.ataqueSurpresaRetaguarda(usuarioId);
        long damage = ataqueBase + ataqueEspecial;
        System.out.println("Usario" +"-"+ usuario.getUsername()+"-"+  "Atacou com ataque especial" +"-"+  boss.getBossName()+"-"+  "Causou " + damage+"-"+ "Dano");
        
        
        // aplicar diminuir  o vigor
        usuario.setEnergiaGuerreiros(energia - damage);
        */
        // 🧪 poção automática
	    pocaoVigorService.verificarEUsarPocaoSeAtiva(usuario);
        // usar o valor retornado
        Object resultado = null;

        // Tenta atacar cada boss na ordem
        resultado = tryHitBoss("IGNORATH", ignorathService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);

        resultado = tryHitBoss("DRAKTHOR", drakthorService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);

        resultado = tryHitBoss("AZURION", azurionService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);

        resultado = tryHitBoss("UMBRAXIS", umbraxisService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);

        resultado = tryHitBoss("NIGHTMARE", nightmareService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("FLAMOR", flamorService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("OBLIVAR", oblivarService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("LYXARA", lyxaraService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("NOXAR", noxarService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("UMBRAR", umbrarService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("MORVATH", morvathService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("OBLÍQUO", obliquoService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("PYRAGON", pyragonService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("GLACIORN", glaciornService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("REFLEXA", reflexaService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("MECHADRON", mechadronService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("NOCTYR", noctyrService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("OBLIVION", oblivionService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("VESPERA", vesperaService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("TENEBRIS", tenebrisService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("GLACIARA", glaciaraService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("INFERNAX", infernaxService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("THUNDERON", thunderonService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("NOCTHARION", noctharionService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("AZRAEL PRIME", azraelPrimeService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("DESTRUIDOR", destruidorService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("TRÍGON BAPHYDRAX", trigonBaphydraxService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("MALPHION", malphionService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("ABYSSAR", abyssarService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("NECROTHAR", necrotharService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("KAELTHOR", kaelthorService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("SOBERANO ABISSAL", abissalService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("LEVIATÃ DO ABISMO", leviatanAbismoService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
         resultado = tryHitBoss("ZARGOTH", zargothService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
         resultado = tryHitBoss("NEXARACH", nexarachService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("CYBERION", cyberionService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("AZURAGON", azuragonService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("OCULTHAR", ocultharService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("PUPPETRIX", puppetrixService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("ABADDON", abaddonService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("VESTA", vestaService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("MALEVOLA", malevolaService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("OBRASERKER", obraserkerService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("CRYOPHANTASM", cryophantasmService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        resultado = tryHitBoss("ARENASCOR", arenascorService.get(), usuario, damage);
        if (resultado != null) return finalizeHit(usuarioId, resultado);
        
        

        /*
       
        
       
        */
        
        
        //===============================================================================
        //===============================================================================

     
      
      
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "O boss foi derrotado. Aguarde o respawn."
           
        );
        
        
        
       }
        
        
             
    }//fim hitActiveBoss

    //===========================  tryHitBoss ================================

    public Object tryHitBoss(String bossName, BattleBoss boss,
                             UsuarioBossBattle usuario, long damage) {

        if (boss == null) return null;

        synchronized (boss) {

            if (!boss.isAlive()) return null;

            boss.applyDamage(damage);

            // salva a instância no serviço correto
            if (boss instanceof GlobalBossIgnorath) ignorathService.save((GlobalBossIgnorath) boss);
            if (boss instanceof GlobalBossDrakthor) drakthorService.save((GlobalBossDrakthor) boss);
            if (boss instanceof GlobalBossAzurion) azurionService.save((GlobalBossAzurion) boss);
            if (boss instanceof GlobalBossUmbraxis) umbraxisService.save((GlobalBossUmbraxis) boss);
            if (boss instanceof GlobalBossNightmare) nightmareService.save((GlobalBossNightmare) boss);
            if (boss instanceof GlobalBossFlamor) flamorService.save((GlobalBossFlamor) boss);
            if (boss instanceof GlobalBossOblivar) oblivarService.save((GlobalBossOblivar) boss);
            if (boss instanceof GlobalBossLyxara) lyxaraService.save((GlobalBossLyxara) boss);
            if (boss instanceof GlobalBossNoxar) noxarService.save((GlobalBossNoxar) boss);
            if (boss instanceof GlobalBossUmbrar) umbrarService.save((GlobalBossUmbrar) boss);
            if (boss instanceof GlobalBossMorvath) morvathService.save((GlobalBossMorvath) boss);
            if (boss instanceof GlobalBossObliquo) obliquoService.save((GlobalBossObliquo) boss);
            if (boss instanceof GlobalBossPyragon) pyragonService.save((GlobalBossPyragon) boss);
            if (boss instanceof GlobalBossGlaciorn) glaciornService.save((GlobalBossGlaciorn) boss);
            if (boss instanceof GlobalBossReflexa) reflexaService.save((GlobalBossReflexa) boss);
            if (boss instanceof GlobalBossMechadron) mechadronService.save((GlobalBossMechadron) boss);
            if (boss instanceof GlobalBossNoctyr) noctyrService.save((GlobalBossNoctyr) boss);
            if (boss instanceof GlobalBossOblivion) oblivionService.save((GlobalBossOblivion) boss);
            if (boss instanceof GlobalBossVespera) vesperaService.save((GlobalBossVespera) boss);
            if (boss instanceof GlobalBossTenebris) tenebrisService.save((GlobalBossTenebris) boss);
            if (boss instanceof GlobalBossGlaciara) glaciaraService.save((GlobalBossGlaciara) boss);
            if (boss instanceof GlobalBossInfernax) infernaxService.save((GlobalBossInfernax) boss);
            if (boss instanceof GlobalBossThunderon) thunderonService.save((GlobalBossThunderon) boss);
            if (boss instanceof GlobalBossNoctharion) noctharionService.save((GlobalBossNoctharion) boss);
            if (boss instanceof GlobalBossAzraelPrime) azraelPrimeService.save((GlobalBossAzraelPrime) boss);
            if (boss instanceof GlobalBossDestruidor) destruidorService.save((GlobalBossDestruidor) boss);
            if (boss instanceof GlobalBossTrigonBaphydrax) trigonBaphydraxService.save((GlobalBossTrigonBaphydrax) boss);
            if (boss instanceof GlobalBossMalphion) malphionService.save((GlobalBossMalphion) boss);
            if (boss instanceof GlobalBossAbyssar) abyssarService.save((GlobalBossAbyssar) boss);
            if (boss instanceof GlobalBossNecrothar) necrotharService.save((GlobalBossNecrothar) boss);
            if (boss instanceof GlobalBossKaelthor) kaelthorService.save((GlobalBossKaelthor) boss);
            if (boss instanceof GlobalBossAbissal) abissalService.save((GlobalBossAbissal) boss);
            if (boss instanceof GlobalBossLeviatanAbismo) leviatanAbismoService.save((GlobalBossLeviatanAbismo) boss);
            if (boss instanceof GlobalBossZargoth) zargothService.save((GlobalBossZargoth) boss);
            if (boss instanceof GlobalBossNexarach) nexarachService.save((GlobalBossNexarach) boss);
            if (boss instanceof GlobalBossCyberion) cyberionService.save((GlobalBossCyberion) boss);
            if (boss instanceof GlobalBossAzuragon) azuragonService.save((GlobalBossAzuragon) boss);
            if (boss instanceof GlobalBossOculthar) ocultharService.save((GlobalBossOculthar) boss);
            if (boss instanceof GlobalBossPuppetrix) puppetrixService.save((GlobalBossPuppetrix) boss);
            
            if (boss instanceof GlobalBossAbaddon) abaddonService.save((GlobalBossAbaddon) boss);
            if (boss instanceof GlobalBossVesta) vestaService.save((GlobalBossVesta) boss);
            if (boss instanceof GlobalBossMalevola) malevolaService.save((GlobalBossMalevola) boss);
            if (boss instanceof GlobalBossObraserker) obraserkerService.save((GlobalBossObraserker) boss);
            if (boss instanceof GlobalBossCryophantasm) cryophantasmService.save((GlobalBossCryophantasm) boss);
            if (boss instanceof GlobalBossArenascor) arenascorService.save((GlobalBossArenascor) boss);
            
       
            /*
          
            
            */
            
            
            registrarDano(bossName, usuario, damage);

            return processReward(bossName, boss, usuario, damage);
        
        
          
        }
        
    }//--->tryHitBoss


    public void registrarDano(String bossName, UsuarioBossBattle usuario, long damage) {
        BossDamageLog log = new BossDamageLog();
        log.setBossName(bossName);
        log.setUserId(usuario.getId());
        log.setDamage(damage);
        log.setUserName(usuario.getUsername());
       
        damageLogRepo.save(log);
        
        
       
    }

    
    //===================================
    //processReward
    //==================================
    

    public Object processReward(
            String bossName,
            BattleBoss boss,
            UsuarioBossBattle usuario,
            long damage
    ) {
    	
 
    	// Ainda vivo → fluxo normal
    	if (boss.getCurrentHp() > 0) {
    	    return Map.of(
    	        "boss", bossName,
    	        "currentHp", boss.getCurrentHp(),
    	        "rewardBoss", boss.getRewardBoss(),
    	        "rewardExp", boss.getRewardExp(),
    	        "damage", damage
    	    );
    	}
    	
    	// TRATAR O ULTIMO ATQUE. 
    	/*
    	 * Jogador A dá o último hit, Jogador B dá o último hit, Jogador C também
    	 * 
    	 *       if (boss.isProcessingDeath() || boss.isRewardDistributed()) {
    	            return Map.of(
    	                "status", "DEFEATED",
    	                "message", "Recompensa já distribuída"
    	            );
    	  }
    	 * */
    	

    	  

    	boss.setProcessingDeath(true);

        

        
        long bossReward = boss.getRewardBoss();
        long expReward = boss.getRewardExp();

  
        List<BossDamageLog> logs = damageLogRepo.findByBossName(bossName);
        if (logs.isEmpty()) return Map.of("status", "NO_DAMAGE_LOG");

        long bossHpMax = boss.getMaxHp();

        Map<Long, Long> damagePorUsuario = logs.stream()
            .collect(Collectors.groupingBy(
                BossDamageLog::getUserId,
                Collectors.summingLong(BossDamageLog::getDamage)
            ));

        for (var entry : damagePorUsuario.entrySet()) {

            UsuarioBossBattle u = usuarioRepo.findById(entry.getKey()).orElse(null);
            if (u == null) continue;

            long danoUsuario = entry.getValue(); 

            long rewardFinal = (bossReward * danoUsuario) / bossHpMax;
            long expFinal    = (expReward * danoUsuario) / bossHpMax;

            rewardFinal = Math.max(1, rewardFinal);
            expFinal    = Math.max(1, expFinal);
            
            try {
            	if (u.getBossCoins() == null) {
            	    u.setBossCoins(BigDecimal.ZERO);
            	}

                u.setBossCoins(
                    u.getBossCoins().add(BigDecimal.valueOf(rewardFinal))
                );

                referidosService.adicionarGanho(u, BigDecimal.valueOf(rewardFinal));
                usuarioService.adicionarExp(u, expFinal);

             

            } catch (Exception e) {
               
                

                System.out.println("Erro ao creditar reward para user {}"+ u.getId() + e);
                throw e; // força rollback visível
            }


 
            usuarioRepo.save(u);

            //logs
            System.out.println(
                "O usuario " + u.getUsername() +
                " recebeu " + rewardFinal + " Boss Coin"
            );
            
            System.out.println(
                    "O usuario " + u.getUsername() +
                    " recebeu " + expFinal + " exp"
                );
        }

        // ✅ FINALIZA O BOSS UMA ÚNICA VEZ
        boss.setRewardDistributed(true);
        boss.setProcessingDeath(false);
        resetBoss();
     
        
     // NÃO libera processingDeath aqui se não houver respawn imediato
     return Map.of(
         "boss", bossName,
         "status", "DEFEATED",
         "rewardTotal", bossReward,
         "expTotal", expReward,
         "participantes", damagePorUsuario.size()
    	  );

    
    	  
    	  
 }//---> fim processReward
   
    //=============================================================
    // finalizeHit
    //=============================================================

    private Object finalizeHit(long usuarioId, Object resultado) {
        bossAttackService.registrarAtaque(usuarioId);
        return resultado;
    }

    // =============================
    // SPAWN E RESET
    // =============================
    


    public void resetBoss() {
        damageLogRepo.deleteAllLogs();
      
    }
    


    
    // =============================
    // COOLDOWN
    // =============================

    public Map<String, Object> cooldown(Long usuarioId) {
        UsuarioBossBattle usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean podeAtacar = bossAttackService.podeAtacar(usuario);
        long segundos = bossAttackService.tempoRestanteSegundos(usuario);

        return Map.of(
            "podeAtacar", podeAtacar,
            "segundosRestantes", segundos
        );
    }
 
    
}
