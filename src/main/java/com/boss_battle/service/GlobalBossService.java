
package com.boss_battle.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.BossDamageLog;
import com.boss_battle.model.BossRewardLock;
import com.boss_battle.model.GlobalBossDestruidor;
import com.boss_battle.model.GlobalBossAzraelPrime;
import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.model.GlobalBossMechadron;
import com.boss_battle.model.GlobalBossMorvath;
import com.boss_battle.model.GlobalBossNightmare;
import com.boss_battle.model.GlobalBossNoctharion;
import com.boss_battle.model.GlobalBossNoctyr;
import com.boss_battle.model.GlobalBossNoxar;
import com.boss_battle.model.GlobalBossObliquo;
import com.boss_battle.model.GlobalBossOblivar;
import com.boss_battle.model.GlobalBossOblivion;
import com.boss_battle.model.GlobalBossPyragon;
import com.boss_battle.model.GlobalBossReflexa;
import com.boss_battle.model.GlobalBossTenebris;
import com.boss_battle.model.GlobalBossThunderon;
import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.model.GlobalBossVespera;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BossDamageLogRepository;
import com.boss_battle.repository.BossRewardLockRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;


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
    
    private final BossDamageLogRepository damageLogRepo;
    private final UsuarioBossBattleRepository usuarioRepo;
    private final BossRewardLockRepository bossRewardLockRepo;
    
    private final ReferidosRecompensaService referidosService;
    private final UsuarioBossBattleService usuarioService;
    private final BossAttackService bossAttackService;
    //private final BossDamageLogService bossDamageLogService;
    
   
    private final RetaguardaService retaguardaService;

    
    private final Random random = new Random();

    public GlobalBossService(
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
            
            BossDamageLogRepository damageLogRepo,
            UsuarioBossBattleRepository usuarioRepo,
            BossRewardLockRepository bossRewardLockRepo,
            ReferidosRecompensaService referidosService,
            UsuarioBossBattleService usuarioService,
            BossAttackService bossAttackService,
            BossDamageLogService bossDamageLogService,
            RetaguardaService retaguardaService
            
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
        this.bossRewardLockRepo = bossRewardLockRepo;
        //this.bossDamageLogService = bossDamageLogService;
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
        
        
        return spawnRandomBoss();
    }

    // =============================
    // HIT NO BOSS ATIVO
    // =============================

    @Transactional
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

        // aplicar diminuir  o vigor
      
        usuario.setEnergiaGuerreiros(energia - damage);
        	
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
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "O boss foi derrotado. Aguarde o respawn."
           
        );
        
        
       }
             
    }//fim hitActiveBoss

   
    // =============================
    // FUNÇÕES AUXILIARES
    // =============================
    @Transactional
    public Object tryHitBoss(String bossName, BattleBoss boss, UsuarioBossBattle usuario, long damage) {
        if (boss == null || !boss.isAlive()) return null;

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
        
        
        registrarDano(bossName, usuario, damage);
        
       
        
        return processReward(bossName, boss, usuario, damage); 
        
    }

    @Transactional
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
    
    @Transactional
    private Object processReward(
            String bossName,
            BattleBoss boss,
            UsuarioBossBattle usuario,
            long damage
    ) {

        // 🔒 lock transacional real
    	BossRewardLock lock = getOrCreateLock(bossName);

    	if (lock.isRewardDistributed()) {
    	    return Map.of(
    	        "status", "ALREADY_DISTRIBUTED",
    	        "boss", bossName
    	    );
    	}

        // Ainda vivo → fluxo normal
        if (boss.isAlive() && boss.getCurrentHp() > 0) {
            return Map.of(
                "boss", bossName,
                "currentHp", boss.getCurrentHp(),
                "rewardBoss", boss.getRewardBoss(),
                "rewardExp", boss.getRewardExp(),
                "damage", damage
            );
        }

        long bossReward = boss.getRewardBoss();
        long expReward  = boss.getRewardExp();

        List<BossDamageLog> logs = damageLogRepo.findByBossName(bossName);
        if (logs.isEmpty()) {
            lock.setRewardDistributed(true);
            return Map.of("status", "NO_DAMAGE_LOG");
        }

        long bossHpMax = boss.getMaxHp();

        Map<Long, Long> damagePorUsuario = logs.stream()
            .collect(Collectors.groupingBy(
                BossDamageLog::getUserId,
                Collectors.summingLong(BossDamageLog::getDamage)
            ));

        for (var entry : damagePorUsuario.entrySet()) {

            UsuarioBossBattle u = usuarioRepo.findById(entry.getKey()).orElse(null);
            if (u == null) continue;

            long danoUsuario = Math.min(entry.getValue(), bossHpMax);

            long rewardFinal = Math.max(1, (bossReward * danoUsuario) / bossHpMax);
            long expFinal    = Math.max(1, (expReward * danoUsuario) / bossHpMax);

            u.setBossCoins(
                u.getBossCoins().add(BigDecimal.valueOf(rewardFinal))
            );

            referidosService.adicionarGanho(u, BigDecimal.valueOf(rewardFinal));
            usuarioService.adicionarExp(u.getId(), (int) expFinal);

            usuarioRepo.save(u);
        }

        // 🔐 idempotência REAL (1 única vez)
        lock.setRewardDistributed(true);

        resetBoss();

        return Map.of(
            "boss", bossName,
            "status", "DEFEATED",
            "rewardTotal", bossReward,
            "expTotal", expReward,
            "participantes", damagePorUsuario.size()
        );
    }
    
    @Transactional
    private BossRewardLock getOrCreateLock(String bossName) {

        BossRewardLock lock = bossRewardLockRepo.lockByBossName(bossName);

        if (lock == null) {
            lock = new BossRewardLock();
            lock.setBossName(bossName);
            lock.setRewardDistributed(false);

            bossRewardLockRepo.saveAndFlush(lock);

            // 🔒 reaplica o lock depois de criar
            lock = bossRewardLockRepo.lockByBossName(bossName);
        }

        return lock;
    }


    /*
    @Transactional
    private Object processReward(
            String bossName,
            BattleBoss boss,
            UsuarioBossBattle usuario,
            long damage
    ) {
    	
    	

    	 // Ainda vivo → fluxo normal
        if (boss.isAlive() && boss.getCurrentHp() > 0) {
            return Map.of(
                "boss", bossName,
                "currentHp", boss.getCurrentHp(),
                "rewardBoss", boss.getRewardBoss(),
                "rewardExp", boss.getRewardExp(),
                "damage", damage
            );
        }

        // 🔐 LOCK: se já está processando morte
        if (boss.isProcessingDeath()) {
            return Map.of(
                "status", "DEFEATED",
                "message", "Boss já foi derrotado"
            );
        }

        // 🔒 Trava a morte
        boss.setProcessingDeath(true);
        
        long bossReward = boss.getRewardBoss();
        long expReward = boss.getRewardExp();

        //BigDecimal rewardTotal = BigDecimal.valueOf(boss.getRewardBoss());
       // BigDecimal hpMax       = BigDecimal.valueOf(boss.getMaxHp());

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

            
              //TESTE 3  
            //OBSERVAR
            long danoUsuario = Math.min(entry.getValue(), bossHpMax);

            long rewardFinal = (bossReward * danoUsuario) / bossHpMax;
            long expFinal    = (expReward * danoUsuario) / bossHpMax;

            // garante pagamento mínimo
            rewardFinal = Math.max(1, rewardFinal);
            expFinal    = Math.max(1, expFinal);
            
        
            
            
        
            u.setBossCoins( u.getBossCoins().add(BigDecimal.valueOf(rewardFinal))
            );
            
          

         
            referidosService.adicionarGanho(u,BigDecimal.valueOf(rewardFinal));
            
            usuarioService.adicionarExp(u.getId(), (int) expFinal);

            usuarioRepo.save(u);
            
            // 🔐 idempotência garantida
           boss.setRewardDistributed(true);
        }
     
        resetBoss(); 

     // NÃO libera processingDeath aqui se não houver respawn imediato
     return Map.of(
         "boss", bossName,
         "status", "DEFEATED",
         "rewardTotal", bossReward,
         "expTotal", expReward,
         "participantes", damagePorUsuario.size()
     );

    }
    

    */

    /*
    private Object processReward(String bossName, BattleBoss boss,UsuarioBossBattle usuario, long damage) {
        if (boss.isAlive() && boss.getCurrentHp() > 0) {
          
			return Map.of(
                "boss", bossName,
                "currentHp", boss.getCurrentHp(),
                "rewardBoss", boss.getRewardBoss(),
                "rewardExp", boss.getRewardExp(),
                "damage", damage
            );
        }

        long bossReward = boss.getRewardBoss();
        long expReward = boss.getRewardExp();

        List<BossDamageLog> logs = damageLogRepo.findByBossName(bossName);
        long bossHpMax = boss.getMaxHp();
        long totalDamage = Math.min(
            logs.stream().mapToLong(BossDamageLog::getDamage).sum(),
            bossHpMax
        );

        Map<Long, Long> damagePorUsuario = logs.stream()
            .collect(Collectors.groupingBy(
                BossDamageLog::getUserId,
                Collectors.summingLong(BossDamageLog::getDamage)
            ));

        for (var entry : damagePorUsuario.entrySet()) {
            //UsuarioBossBattle u = usuarioRepo.findById(entry.getKey()).orElse(null);
        	UsuarioBossBattle u = usuarioRepo.findById(entry.getKey())
        		    .map(usuarioRepo::saveAndFlush)
        		    .orElse(null);

            if (u == null) continue;

           // long danoUsuario = Math.min(entry.getValue(), bossHpMax);
            //double proporcao = (double) danoUsuario / totalDamage;
           // long rewardFinal = Math.round(bossReward * proporcao);
           // long expFinal = Math.round(expReward * proporcao);

            long danoUsuario = entry.getValue(); 

            long rewardFinal = (bossReward * danoUsuario) / totalDamage;
            long expFinal = (expReward * danoUsuario) / totalDamage;

            // garante que quem causou dano receba algo
            if (rewardFinal == 0 && danoUsuario > 0) rewardFinal = 1;
            if (expFinal == 0 && danoUsuario > 0) expFinal = 1;

            
            
            if (u.getBossCoins() == null) u.setBossCoins(BigDecimal.ZERO);
            u.setBossCoins(u.getBossCoins().add(BigDecimal.valueOf(rewardFinal)));

            referidosService.adicionarGanho(u, BigDecimal.valueOf(rewardFinal));
            usuarioService.adicionarExp(u.getId(), (int) expFinal);

            usuarioRepo.save(u);
        }

        bossDamageLogService.clearBossDamage(bossName);
        damageLogRepo.deleteByBossName(bossName);

        return Map.of(
            "boss", bossName,
            "status", "DEFEATED",
            "rewardTotal", bossReward,
            "expTotal", expReward,
            "participantes", damagePorUsuario.size()
        );
    }
*/
    //=============================================================
    // finalizeHit
    //=============================================================
    @Transactional
    private Object finalizeHit(long usuarioId, Object resultado) {
        bossAttackService.registrarAtaque(usuarioId);
        return resultado;
    }

    // =============================
    // SPAWN E RESET
    // =============================
    

    @Transactional
    public void resetBoss() {
        damageLogRepo.deleteAllLogs();
      
    }
    
    @Transactional
    public BattleBoss spawnRandomBoss() {
        killAllBosses();
        //resetBoss();
        
       
        int choice = random.nextInt(26);
        BattleBoss newBoss;

        switch (choice) {
            case 0 -> {
                GlobalBossIgnorath ig = ignorathService.get();
                ig.setProcessingDeath(false);
                ig.setAlive(true);
                ig.setCurrentHp(ig.getMaxHp());
                ig.setSpawnedAt(LocalDateTime.now());
                ignorathService.save(ig);
                newBoss = ig;
            }
            case 1 -> {
                GlobalBossDrakthor dr = drakthorService.get();
                dr.setProcessingDeath(false);
                dr.setAlive(true);
                dr.setCurrentHp(dr.getMaxHp());
                dr.setSpawnedAt(LocalDateTime.now());
                drakthorService.save(dr);
                newBoss = dr;
            }
            case 2 -> {
                GlobalBossAzurion az = azurionService.get();
                az.setProcessingDeath(false);
                az.setAlive(true);
                az.setCurrentHp(az.getMaxHp());
                az.setSpawnedAt(LocalDateTime.now());
                azurionService.save(az);
                newBoss = az;
            }
            
            case 3 -> {
            	GlobalBossNightmare nm = nightmareService.get();
            	nm.setProcessingDeath(false);
            	nm.setAlive(true);
            	nm.setCurrentHp(nm.getMaxHp());
            	nm.setSpawnedAt(LocalDateTime.now());
            	nightmareService.save(nm);
                newBoss = nm;
            }
            
            case 4 -> {
            	GlobalBossFlamor fl = flamorService.get();
            	fl.setProcessingDeath(false);
            	fl.setAlive(true);
            	fl.setCurrentHp(fl.getMaxHp());
            	fl.setSpawnedAt(LocalDateTime.now());
                flamorService.save(fl);
                newBoss = fl;
            }
            
            case 5 -> {
            	GlobalBossOblivar ob = oblivarService.get();
            	ob.setProcessingDeath(false);
            	ob.setAlive(true);
            	ob.setCurrentHp(ob.getMaxHp());
            	ob.setSpawnedAt(LocalDateTime.now());
                oblivarService.save(ob);
                newBoss = ob;
            }
            
            case 6 -> {
            	  GlobalBossUmbraxis um = umbraxisService.get();
            	  um.setProcessingDeath(false);
                  um.setAlive(true);
                  um.setCurrentHp(um.getMaxHp());
                  um.setSpawnedAt(LocalDateTime.now());
                  umbraxisService.save(um);
                  newBoss = um;
            }
            
            case 7 -> {
            	GlobalBossLyxara lx = lyxaraService.get();
            	lx.setProcessingDeath(false);
            	lx.setAlive(true);
            	lx.setCurrentHp(lx.getMaxHp());
            	lx.setSpawnedAt(LocalDateTime.now());
            	lyxaraService.save(lx);
                newBoss = lx;
          }
          
            
            case 8 -> {
            	GlobalBossNoxar nx = noxarService.get();
            	nx.setProcessingDeath(false);
            	nx.setAlive(true);
            	nx.setCurrentHp(nx.getMaxHp());
            	nx.setSpawnedAt(LocalDateTime.now());
            	noxarService.save(nx);
                newBoss = nx;
          }
          
            case 9 -> {
            	GlobalBossUmbrar ub = umbrarService.get();
            	ub.setProcessingDeath(false);
            	ub.setAlive(true);
            	ub.setCurrentHp(ub.getMaxHp());
            	ub.setSpawnedAt(LocalDateTime.now());
            	umbrarService.save(ub);
                newBoss = ub;
          }
            
            case 10 -> {
            	GlobalBossMorvath mv = morvathService.get();
            	mv.setProcessingDeath(false);
            	mv.setAlive(true);
            	mv.setCurrentHp(mv.getMaxHp());
            	mv.setSpawnedAt(LocalDateTime.now());
            	morvathService.save(mv);
                newBoss = mv;
          }
            
            case 11 -> {
            	GlobalBossObliquo oq = obliquoService.get();
            	oq.setProcessingDeath(false);
            	oq.setAlive(true);
            	oq.setCurrentHp(oq.getMaxHp());
            	oq.setSpawnedAt(LocalDateTime.now());
            	obliquoService.save(oq);
                newBoss = oq;
          }
            
            case 12 -> {
            	GlobalBossPyragon pg = pyragonService.get();
            	pg.setProcessingDeath(false);
            	pg.setAlive(true);
            	pg.setCurrentHp(pg.getMaxHp());
            	pg.setSpawnedAt(LocalDateTime.now());
            	pyragonService.save(pg);
                newBoss = pg;
          }
            
            case 13-> {
            	GlobalBossGlaciorn gc = glaciornService.get();
            	gc.setProcessingDeath(false);
            	gc.setAlive(true);
            	gc.setCurrentHp(gc.getMaxHp());
            	gc.setSpawnedAt(LocalDateTime.now());
            	glaciornService.save(gc);
                newBoss = gc;
          }
            
            case 14-> {
            	GlobalBossReflexa rx = reflexaService.get();
            	rx.setProcessingDeath(false);
            	rx.setAlive(true);
            	rx.setCurrentHp(rx.getMaxHp());
            	rx.setSpawnedAt(LocalDateTime.now());
            	reflexaService.save(rx);
                newBoss = rx;
          }
            
            case 15-> {
            	GlobalBossMechadron mc = mechadronService.get();
            	mc.setProcessingDeath(false);
            	mc.setAlive(true);
            	mc.setCurrentHp(mc.getMaxHp());
            	mc.setSpawnedAt(LocalDateTime.now());
            	mechadronService.save(mc);
                newBoss = mc;
          }
           
            case 16-> {
            	GlobalBossNoctyr nr = noctyrService.get();
            	nr.setProcessingDeath(false);
            	nr.setAlive(true);
            	nr.setCurrentHp(nr.getMaxHp());
            	nr.setSpawnedAt(LocalDateTime.now());
            	noctyrService.save(nr);
                newBoss = nr;
          }
            
            case 17-> {
            	GlobalBossOblivion on = oblivionService.get();
            	on.setProcessingDeath(false);
            	on.setAlive(true);
            	on.setCurrentHp(on.getMaxHp());
            	on.setSpawnedAt(LocalDateTime.now());
            	oblivionService.save(on);
                newBoss = on;
          }
            
            case 18 -> {
            	GlobalBossVespera vs = vesperaService.get();
            	vs.setProcessingDeath(false);
            	vs.setAlive(true);
            	vs.setCurrentHp(vs.getMaxHp());
            	vs.setSpawnedAt(LocalDateTime.now());
            	vesperaService.save(vs);
            	newBoss = vs;
            }
            
            case 19 -> {
            	GlobalBossTenebris ts = tenebrisService.get();
            	ts.setProcessingDeath(false);
            	ts.setAlive(true);
            	ts.setCurrentHp(ts.getMaxHp());
            	ts.setSpawnedAt(LocalDateTime.now());
            	tenebrisService.save(ts);
            	newBoss = ts;
            }
            
            case 20 -> {
            	GlobalBossGlaciara gl = glaciaraService.get();
            	gl.setProcessingDeath(false);
            	gl.setAlive(true);
            	gl.setCurrentHp(gl.getMaxHp());
            	gl.setSpawnedAt(LocalDateTime.now());
            	glaciaraService.save(gl);
            	newBoss = gl;
            }
            
            case 21 -> {
            	GlobalBossInfernax ix = infernaxService.get();
            	ix.setProcessingDeath(false);
            	ix.setAlive(true);
            	ix.setCurrentHp(ix.getMaxHp());
            	ix.setSpawnedAt(LocalDateTime.now());
            	infernaxService.save(ix);
            	newBoss = ix;
            }
            
            case 22 -> {
            	GlobalBossThunderon td = thunderonService.get();
            	td.setProcessingDeath(false);
            	td.setAlive(true);
            	td.setCurrentHp(td.getMaxHp());
            	td.setSpawnedAt(LocalDateTime.now());
            	thunderonService.save(td);
            	newBoss = td;
            }
            
            case 23 -> {
            	GlobalBossNoctharion nt = noctharionService.get();
            	nt.setProcessingDeath(false);
            	nt.setAlive(true);
            	nt.setCurrentHp(nt.getMaxHp());
            	nt.setSpawnedAt(LocalDateTime.now());
            	noctharionService.save(nt);
            	newBoss = nt;
            }
            
            case 24 -> {
            	GlobalBossAzraelPrime ap = azraelPrimeService.get();
            	ap.setProcessingDeath(false);
            	ap.setAlive(true);
            	ap.setCurrentHp(ap.getMaxHp());
            	ap.setSpawnedAt(LocalDateTime.now());
            	azraelPrimeService.save(ap);
            	newBoss = ap;
            }
            case 25 -> {
            	GlobalBossDestruidor ds = destruidorService.get();
            	ds.aplicarEscalamentoDestruidor();
            	ds.setProcessingDeath(false);
            	ds.setAlive(true);
            	ds.setCurrentHp(ds.getMaxHp());
            	ds.setSpawnedAt(LocalDateTime.now());
            	destruidorService.save(ds);
            	newBoss = ds;
            }
            
           
            
            default -> {
                GlobalBossUmbraxis um = umbraxisService.get();
                um.setProcessingDeath(false);
                um.setAlive(true);
                um.setCurrentHp(um.getMaxHp());
                um.setSpawnedAt(LocalDateTime.now());
                umbraxisService.save(um);
                newBoss = um;
            }
        }

        return newBoss;
    }

    @Transactional
    public void killAllBosses() {
        GlobalBossIgnorath ig = ignorathService.get();
        GlobalBossDrakthor dr = drakthorService.get();
        GlobalBossAzurion az = azurionService.get();
        GlobalBossUmbraxis um = umbraxisService.get();
        GlobalBossNightmare nm = nightmareService.get();
        GlobalBossFlamor fl = flamorService.get();
        GlobalBossOblivar ob = oblivarService.get();
        GlobalBossLyxara lx = lyxaraService.get();
        GlobalBossNoxar nx  = noxarService.get();
        GlobalBossUmbrar ub = umbrarService.get();
    	GlobalBossMorvath mv = morvathService.get();
    	GlobalBossObliquo oq = obliquoService.get();
    	GlobalBossPyragon pg = pyragonService.get();
    	GlobalBossGlaciorn gc = glaciornService.get();
    	GlobalBossReflexa rx = reflexaService.get();
    	GlobalBossMechadron mc = mechadronService.get();
    	GlobalBossNoctyr nr = noctyrService.get();
    	GlobalBossOblivion on = oblivionService.get();
    	GlobalBossVespera vs = vesperaService.get();
    	GlobalBossTenebris ts = tenebrisService.get();
    	GlobalBossGlaciara gl = glaciaraService.get();
    	GlobalBossInfernax ix = infernaxService.get();
    	GlobalBossThunderon td = thunderonService.get();
    	GlobalBossNoctharion nt = noctharionService.get();
    	GlobalBossAzraelPrime ap = azraelPrimeService.get();
    	GlobalBossDestruidor ds = destruidorService.get();
    	
    	
    	
    	
    	
    	ig.setAlive(false);
    	ig.setProcessingDeath(false);

    	dr.setAlive(false);
    	dr.setProcessingDeath(false);

    	az.setAlive(false);
    	az.setProcessingDeath(false);

    	um.setAlive(false);
    	um.setProcessingDeath(false);

    	nm.setAlive(false);
    	nm.setProcessingDeath(false);

    	fl.setAlive(false);
    	fl.setProcessingDeath(false);

    	ob.setAlive(false);
    	ob.setProcessingDeath(false);

    	lx.setAlive(false);
    	lx.setProcessingDeath(false);

    	nx.setAlive(false);
    	nx.setProcessingDeath(false);

    	ub.setAlive(false);
    	ub.setProcessingDeath(false);

    	mv.setAlive(false);
    	mv.setProcessingDeath(false);

    	oq.setAlive(false);
    	oq.setProcessingDeath(false);

    	pg.setAlive(false);
    	pg.setProcessingDeath(false);

    	gc.setAlive(false);
    	gc.setProcessingDeath(false);

    	rx.setAlive(false);
    	rx.setProcessingDeath(false);

    	mc.setAlive(false);
    	mc.setProcessingDeath(false);

    	nr.setAlive(false);
    	nr.setProcessingDeath(false);

    	on.setAlive(false);
    	on.setProcessingDeath(false);

    	vs.setAlive(false);
    	vs.setProcessingDeath(false);

    	ts.setAlive(false);
    	ts.setProcessingDeath(false);

    	gl.setAlive(false);
    	gl.setProcessingDeath(false);

    	ix.setAlive(false);
    	ix.setProcessingDeath(false);

    	td.setAlive(false);
    	td.setProcessingDeath(false);

    	nt.setAlive(false);
    	nt.setProcessingDeath(false);

    	ap.setAlive(false);
    	ap.setProcessingDeath(false);

    	ds.setAlive(false);
    	ds.setProcessingDeath(false);

        ignorathService.save(ig);
        drakthorService.save(dr);
        azurionService.save(az);
        umbraxisService.save(um);
        nightmareService.save(nm);
        flamorService.save(fl);
        oblivarService.save(ob);
        lyxaraService.save(lx);
        noxarService.save(nx);
        umbrarService.save(ub);
        morvathService.save(mv);
        obliquoService.save(oq);
        pyragonService.save(pg);
        glaciornService.save(gc);
        reflexaService.save(rx);
        mechadronService.save(mc);
        noctyrService.save(nr);
        oblivionService.save(on);
        vesperaService.save(vs);
        tenebrisService.save(ts);
        glaciaraService.save(gl);
        infernaxService.save(ix);
        thunderonService.save(td);
        noctharionService.save(nt);
        azraelPrimeService.save(ap);
        destruidorService.save(ds);
        
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


/**
   
    //===============================  tryHitBossAuto =====================================================
    //=====================================================================================================
    @Transactional
    private Object tryHitBossAuto(String bossName, BattleBoss boss, UsuarioBossBattle usuario, long damage) {
        if (boss == null || !boss.isAlive()) return null;

        boss.applyDamage(damage);

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
        
        registrarDano(bossName, usuario, damage);

        if (!boss.isAlive() || boss.getCurrentHp() <= 0) {
            spawnRandomBoss();
        }
        
   
        return processReward(bossName, boss, usuario, damage);
    }

    
    public Object atacarBossAtivo(UsuarioBossBattle usuario, long damage) {
        Object resultado = tryHitBossAuto("IGNORATH", ignorathService.get(), usuario, damage);
        if (resultado != null) return resultado;

        resultado = tryHitBossAuto("DRAKTHOR", drakthorService.get(), usuario, damage);
        if (resultado != null) return resultado;

        resultado = tryHitBossAuto("AZURION", azurionService.get(), usuario, damage);
        if (resultado != null) return resultado;

        resultado = tryHitBossAuto("UMBRAXIS", umbraxisService.get(), usuario, damage);
        if (resultado != null) return resultado;

        resultado = tryHitBoss("NIGHTMARE", nightmareService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("FLAMOR", flamorService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("OBLIVAR", oblivarService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("LYXARA", lyxaraService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("NOXAR", noxarService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("UMBRAR", umbrarService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("MORVATH", morvathService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("OBLÍQUO", obliquoService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("PYRAGON", pyragonService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("GLACIORN", glaciornService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("REFLEXA", reflexaService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("MECHADRON", mechadronService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("NOCTYR", noctyrService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("OBLIVION", oblivionService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("VESPERA", vesperaService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("TENEBRIS", tenebrisService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("GLACIARA", glaciaraService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("INFERNAX", infernaxService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("THUNDERON", thunderonService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("NOCTHARION", noctharionService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        resultado = tryHitBoss("AZRAEL PRIME", azraelPrimeService.get(), usuario, damage);
        if (resultado != null) return resultado;
        
        
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "Nenhum boss ativo no momento"
        );
    }
    
    
    //============================== ataque do boss =======================================
 



*/

/**
//testar pagamento com bigDecimal
long danoUsuario = Math.min(entry.getValue(), bossHpMax);

BigDecimal dano = BigDecimal.valueOf(danoUsuario);

BigDecimal percentual = dano.divide(hpMax, 18, RoundingMode.HALF_UP);

BigDecimal rewardUsuario =rewardTotal.multiply(percentual);

long expFinal    = (expReward * danoUsuario) / bossHpMax;
*/




/**
//codigo com  Math.ceil paga pelo menos 1 proporcional, funciona/PARECE!
long danoUsuario = Math.min(entry.getValue(), bossHpMax);

double percentual = (double) danoUsuario / bossHpMax;

long rewardFinal = Math.max(1, (long) Math.ceil(bossReward * percentual));
long expFinal    = Math.max(1, (long) Math.ceil(expReward * percentual));

*/
/***
// codigo com Math.round arredondando pra baixo, não esta bom
long danoUsuario = Math.min(entry.getValue(), bossHpMax);

double percentual = (double) danoUsuario / bossHpMax;
long rewardFinal = Math.max(1, Math.round(bossReward * percentual));



double percentualXP = (double) danoUsuario / bossHpMax;
long expFinal = Math.max(1, Math.round(expReward * percentualXP));
*/