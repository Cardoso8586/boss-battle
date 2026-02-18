package com.boss_battle.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.AtaqueBossResponseDTO;

import com.boss_battle.service.GlobalBossService;
import com.boss_battle.service.auto_ataque.BossAutoAttackService;
@RestController
@RequestMapping("/api/boss")
public class GlobalBossController {

    private final GlobalBossService globalBossService;
    private final BossAutoAttackService bossAutoAttackService;

    public GlobalBossController(
    		GlobalBossService globalBossService, 
    		BossAutoAttackService bossAutoAttackService)
    {
        this.globalBossService = globalBossService;
        this.bossAutoAttackService = bossAutoAttackService;
    }

    @GetMapping("/active")
    public Object getActiveBoss() {
        return globalBossService.getActiveBoss();
    }

    @PostMapping("/hit")
    public Object hitActiveBoss(@RequestParam long usuarioId) {
        return globalBossService.hitActiveBoss(usuarioId);
    }

    @GetMapping("/cooldown")
    public Map<String, Object> cooldown(@RequestParam Long usuarioId) {
        return globalBossService.cooldown(usuarioId);
    }

    @GetMapping("/ultimo-ataque")
    public ResponseEntity<AtaqueBossResponseDTO> getUltimoAtaque() {

        AtaqueBossResponseDTO ataque = bossAutoAttackService.getUltimoAtaque();

        if (ataque == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ataque);
    }




}


/*
package com.boss_battle.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.BossDamageLog;
import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BossDamageLogRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.AzurionService;
import com.boss_battle.service.BossAttackService;
import com.boss_battle.service.BossDamageLogService;
import com.boss_battle.service.DrakthorService;
import com.boss_battle.service.IgnorathService;
import com.boss_battle.service.ReferidosRecompensaService;
import com.boss_battle.service.UmbraxisService;
import com.boss_battle.service.UsuarioBossBattleService;

@RestController
@RequestMapping("/api/boss")
public class GlobalBossController {

	@Autowired
	ReferidosRecompensaService referidosRecompensaService;
	
	@Autowired
	UsuarioBossBattleService usuarioBossBattleService;
	
	@Autowired
    private BossAttackService bossAttackService;
	  
	@Autowired
	BossDamageLogService bossDamageLogService;
	
	@Autowired
	private BossDamageLogRepository damageLogRepo;

    @Autowired
    private UsuarioBossBattleRepository repo;

    private final IgnorathService ignorathService;
    private final DrakthorService drakthorService;
    private final AzurionService azurionService;
    private final UmbraxisService umbraxisService;

    public GlobalBossController(
            IgnorathService ignorathService,
            DrakthorService drakthorService,
            AzurionService azurionService,
            UmbraxisService umbraxisService
    ) {
        this.ignorathService = ignorathService;
        this.drakthorService = drakthorService;
        this.azurionService = azurionService;
        this.umbraxisService = umbraxisService;
    }

    // ============================================================
    //   RETORNAR O BOSS ATIVO
    // ============================================================
    @GetMapping("/active")
    public synchronized Object getActiveBoss() {

        GlobalBossIgnorath ig = ignorathService.get();
        if (ig.isAlive()) return ig;

        GlobalBossDrakthor dr = drakthorService.get();
        if (dr.isAlive()) return dr;

        GlobalBossAzurion az = azurionService.get();
        if (az.isAlive()) return az;
        
        GlobalBossUmbraxis um = umbraxisService.get();
        if (az.isAlive()) return um;

        // Apenas aqui nasce um novo boss
        return spawnRandomBoss();
    }
    
    

    // ============================================================
    //   HIT NO BOSS ATIVO
    // ============================================================
    
    @PostMapping("/hit")
    public Object hitActiveBoss(@RequestParam long usuarioId) {

        // Buscar o usuário
        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se pode atacar
        if (!bossAttackService.podeAtacar(usuario)) {
            return Map.of(
                "status", "COOLDOWN",
                "message", "Aguarde para atacar novamente",
                "segundosRestantes", bossAttackService.tempoRestanteSegundos(usuario)
            );
        }

        long damage = usuario.getAtaqueBase();
        Object resultado = null;

        // 1 → Ignorath
        GlobalBossIgnorath ig = ignorathService.get();
        if (ig.isAlive()) {
            resultado = processBossHit("IGNORATH", ig, ignorathService, usuario, damage);
        }

        // 2 → Drakthor
        if (resultado == null) {
            GlobalBossDrakthor dr = drakthorService.get();
            if (dr.isAlive()) {
                resultado = processBossHit("DRAKTHOR", dr, drakthorService, usuario, damage);
            }
        }

        // 3 → Azurion
        if (resultado == null) {
            GlobalBossAzurion az = azurionService.get();
            if (az.isAlive()) {
                resultado = processBossHit("AZURION", az, azurionService, usuario, damage);
            }
        }
        
        // 4 → UMBRAXIS"
        if (resultado == null) {
        	GlobalBossUmbraxis um = umbraxisService.get();
            if (um.isAlive()) {
                resultado = processBossHit("UMBRAXIS", um, azurionService, usuario, damage);
            }
        }

        // Se não há boss, retorna que o boss foi derrotado
        if (resultado == null) {
            return Map.of(
                "status", "NO_BOSS",
                "message", "O boss foi derrotado. Aguarde o respawn."
            );
        }

        // ✅ Registra o ataque após realizar
        bossAttackService.registrarAtaque(usuarioId);

        return resultado;
    }
    
    /**
    @PostMapping("/hit")
    public Object hitActiveBoss(@RequestParam long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        long damage = usuario.getAtaqueBase();

        // 1 → Ignorath
        GlobalBossIgnorath ig = ignorathService.get();
        if (ig.isAlive()) {
            return processBossHit("IGNORATH", ig, ignorathService, usuario, damage);
        }

        // 2 → Drakthor
        GlobalBossDrakthor dr = drakthorService.get();
        if (dr.isAlive()) {
            return processBossHit("DRAKTHOR", dr, drakthorService, usuario, damage);
        }

        // 3 → Azurion
        GlobalBossAzurion az = azurionService.get();
        if (az.isAlive()) {
            return processBossHit("AZURION", az, azurionService, usuario, damage);
        }

        return Map.of(
        	    "status", "NO_BOSS",
        	    "message", "O boss foi derrotado. Aguarde o respawn."
        	);

    }

    
    @GetMapping("/cooldown")
    public Map<String, Object> cooldown(@RequestParam Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean podeAtacar = bossAttackService.podeAtacar(usuario);
        long segundos = bossAttackService.tempoRestanteSegundos(usuario);

        return Map.of(
            "podeAtacar", podeAtacar,
            "segundosRestantes", segundos
        );
    }

    
    // ============================================================
    //   FUNÇÃO INTERNA — PROCESSAR HIT
    // ============================================================
    public Object processBossHit(String bossName, Object bossObj, Object service,
                                  UsuarioBossBattle usuario, long damage) {

    	
    	BattleBoss boss;

    	if (bossObj instanceof GlobalBossIgnorath ig) {
    	    ig.applyDamage(damage);
    	    ignorathService.save(ig);
    	    boss = ig;
    	}
    	else if (bossObj instanceof GlobalBossDrakthor dr) {
    	    dr.applyDamage(damage);
    	    drakthorService.save(dr);
    	    boss = dr;
    	}
    	else if (bossObj instanceof GlobalBossAzurion az) {
    	    az.applyDamage(damage);
    	    azurionService.save(az);
    	    boss = az;
    	}
    	else {
    	    throw new RuntimeException("Tipo de boss inválido!");
    	}

    	
       registrarDano(bossName, usuario, damage);
    
    	
 
       
        // SE O BOSS MORREU → DISTRIBUIR RECOMPENSA
        // =====================================================
        if (!boss.isAlive() || boss.getCurrentHp() <= 0) {
        	
        	

        	long bossReward = boss.getRewardBoss();
        	long expReward = boss.getRewardExp();

        	List<BossDamageLog> logs =
        	        damageLogRepo.findByBossName(bossName);

        	long bossHpMax = boss.getMaxHp();

        	long totalDamage = Math.min(
        	        logs.stream()
        	            .mapToLong(BossDamageLog::getDamage)
        	            .sum(),
        	        bossHpMax
        	);

        	Map<Long, Long> damagePorUsuario = logs.stream()
        	        .collect(Collectors.groupingBy(
        	                BossDamageLog::getUserId,
        	                Collectors.summingLong(BossDamageLog::getDamage)
        	        ));

        	for (var entry : damagePorUsuario.entrySet()) {

        	    UsuarioBossBattle u = repo.findById(entry.getKey()).orElse(null);
        	    if (u == null) continue;

        	    long danoUsuario = Math.min(entry.getValue(), bossHpMax);

        	    double proporcao = (double) danoUsuario / totalDamage;

        	    long rewardFinal = Math.round(bossReward * proporcao);
        	    long expFinal    = Math.round(expReward * proporcao);
        	    
        	   

        	    if (u.getBossCoins() == null) {
        	        u.setBossCoins(BigDecimal.ZERO);
        	    }

        	    u.setBossCoins(
        	        u.getBossCoins().add(BigDecimal.valueOf(rewardFinal))
        	    );
        	    //adiconar ganhos ao referidos
        	    BigDecimal rewardReferidos =  BigDecimal.valueOf(rewardFinal);
        	    referidosRecompensaService.adicionarGanho(u, rewardReferidos);
        	    
        	    // Adiciona XP usando o ID correto do usuário do loop
        	    usuarioBossBattleService.adicionarExp(u.getId(), expFinal);

        	    repo.save(u);
        	}

            bossDamageLogService.clearBossDamage(bossName);
            // 🧹 limpa logs do boss morto
            damageLogRepo.deleteByBossName(bossName);
            // ✅ reset global (se existir)
        


            return Map.of(
                    "boss", bossName,
                    "status", "DEFEATED",
                    "message", "Boss derrotado!",
                     "rewardTotal", bossReward,
                     "expTotal", expReward,
                    "participantes", damagePorUsuario.size()
            );
        }
     // Boss vivo
        return Map.of(
            "boss", bossName,
            "damage", damage,
            "currentHp", getHp(bossObj),
            "rewardBoss", boss.getRewardBoss(),
            "rewardExp", boss.getRewardExp(),  
            "nivel", usuario.getNivel(),
            "ataqueAtual", usuario.getAtaqueBase()
        );

    }
    
    
    private void registrarDano(String bossName, UsuarioBossBattle usuario, long damage) {

        BossDamageLog log = new BossDamageLog();
        log.setBossName(bossName);
        log.setUserId(usuario.getId());
        log.setDamage(damage);
       

        damageLogRepo.save(log);
    }


    private long getHp(Object boss) {
        if (boss instanceof GlobalBossIgnorath ig) return ig.getCurrentHp();
        if (boss instanceof GlobalBossDrakthor dr) return dr.getCurrentHp();
        if (boss instanceof GlobalBossAzurion az) return az.getCurrentHp();
        return 0;
    }
    
  

    
    private Object spawnRandomBoss() {

        killAllBosses();

        int random = new java.util.Random().nextInt(3);

        if (random == 0) {
            GlobalBossIgnorath ig = ignorathService.get();
            ig.setAlive(true);
            ig.setCurrentHp(ig.getMaxHp());
            ig.setSpawnedAt(LocalDateTime.now());
      
            return ignorathService.save(ig);
        }

        if (random == 1) {
            GlobalBossDrakthor dr = drakthorService.get();
            dr.setAlive(true);
            dr.setCurrentHp(dr.getMaxHp());
            dr.setSpawnedAt(LocalDateTime.now());
            return drakthorService.save(dr);
        }

        GlobalBossAzurion az = azurionService.get();
        az.setAlive(true);
        az.setCurrentHp(az.getMaxHp());
        az.setSpawnedAt(LocalDateTime.now());
        return azurionService.save(az);
    }

    
 
    private void killAllBosses() {
        GlobalBossIgnorath ig = ignorathService.get();
        GlobalBossDrakthor dr = drakthorService.get();
        GlobalBossAzurion az = azurionService.get();

        ig.setAlive(false);
        dr.setAlive(false);
        az.setAlive(false);

        ignorathService.save(ig);
        drakthorService.save(dr);
        azurionService.save(az);
    }

    
    public Object atacarBossAtivo(UsuarioBossBattle usuario, long damage) {

        Object resultado = null;

        // 1 → Ignorath
        GlobalBossIgnorath ig = ignorathService.get();
        if (ig != null && ig.isAlive()) {
            return processBossHit("IGNORATH", ig, ignorathService, usuario, damage);
        }

        // 2 → Drakthor
        GlobalBossDrakthor dr = drakthorService.get();
        if (dr != null && dr.isAlive()) {
            return processBossHit("DRAKTHOR", dr, drakthorService, usuario, damage);
        }

        // 3 → Azurion
        GlobalBossAzurion az = azurionService.get();
        if (az != null && az.isAlive()) {
            return processBossHit("AZURION", az, azurionService, usuario, damage);
        }

        return Map.of(
            "status", "NO_BOSS",
            "message", "Nenhum boss ativo no momento"
        );
    }
 

//=====================================================

  
}
*/

