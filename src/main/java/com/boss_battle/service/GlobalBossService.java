
package com.boss_battle.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.BossDamageLog;
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
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
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
    
    private final BossDamageLogRepository damageLogRepo;
    private final UsuarioBossBattleRepository usuarioRepo;
    
    
    private final ReferidosRecompensaService referidosService;
    private final UsuarioBossBattleService usuarioService;
    private final BossAttackService bossAttackService;
    private final BossDamageLogService bossDamageLogService;
    
   
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
            
            BossDamageLogRepository damageLogRepo,
            UsuarioBossBattleRepository usuarioRepo,
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
        this.bossDamageLogService = bossDamageLogService;
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
        
        return spawnRandomBoss();
    }

    // =============================
    // HIT NO BOSS ATIVO
    // =============================
    public Object hitActiveBoss(long usuarioId) {
        UsuarioBossBattle usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!bossAttackService.podeAtacar(usuario)) {
            return Map.of(
                "status", "COOLDOWN",
                "message", "Aguarde para atacar novamente",
                "segundosRestantes", bossAttackService.tempoRestanteSegundos(usuario)
            );
        }
        
        long ataqueBase = usuario.getAtaqueBase();
        long ataqueEspecial = retaguardaService.processarAtaqueRetaguarda(usuarioId);

        long damage = ataqueBase + ataqueEspecial;

        // aplicar diminuir  o vigor
        Long energia = usuario.getEnergiaGuerreiros();
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
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "O boss foi derrotado. Aguarde o respawn."
        );
        
        
    }

    // =============================
    // FUNÇÕES AUXILIARES
    // =============================
    private Object tryHitBoss(String bossName, BattleBoss boss, UsuarioBossBattle usuario, long damage) {
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
        
        
        
        
        registrarDano(bossName, usuario, damage);

        return processReward(bossName, boss, damage); // seu método continua para distribuir recompensas
    }


    private void registrarDano(String bossName, UsuarioBossBattle usuario, long damage) {
        BossDamageLog log = new BossDamageLog();
        log.setBossName(bossName);
        log.setUserId(usuario.getId());
        log.setDamage(damage);
        log.setUserName(usuario.getUsername());
        damageLogRepo.save(log);
        
        
        // getUserName
    }

    private Object processReward(String bossName, BattleBoss boss, long damage) {
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
            UsuarioBossBattle u = usuarioRepo.findById(entry.getKey()).orElse(null);
            if (u == null) continue;

            long danoUsuario = Math.min(entry.getValue(), bossHpMax);
            double proporcao = (double) danoUsuario / totalDamage;
            long rewardFinal = Math.round(bossReward * proporcao);
            long expFinal = Math.round(expReward * proporcao);

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

    private Object finalizeHit(long usuarioId, Object resultado) {
        bossAttackService.registrarAtaque(usuarioId);
        return resultado;
    }

    // =============================
    // SPAWN E RESET
    // =============================
    public BattleBoss spawnRandomBoss() {
        killAllBosses();

        int choice = random.nextInt(23);
        BattleBoss newBoss;

        switch (choice) {
            case 0 -> {
                GlobalBossIgnorath ig = ignorathService.get();
                ig.setAlive(true);
                ig.setCurrentHp(ig.getMaxHp());
                ig.setSpawnedAt(LocalDateTime.now());
                ignorathService.save(ig);
                newBoss = ig;
            }
            case 1 -> {
                GlobalBossDrakthor dr = drakthorService.get();
                dr.setAlive(true);
                dr.setCurrentHp(dr.getMaxHp());
                dr.setSpawnedAt(LocalDateTime.now());
                drakthorService.save(dr);
                newBoss = dr;
            }
            case 2 -> {
                GlobalBossAzurion az = azurionService.get();
                az.setAlive(true);
                az.setCurrentHp(az.getMaxHp());
                az.setSpawnedAt(LocalDateTime.now());
                azurionService.save(az);
                newBoss = az;
            }
            
            case 3 -> {
            	GlobalBossNightmare nm = nightmareService.get();
            	nm.setAlive(true);
            	nm.setCurrentHp(nm.getMaxHp());
            	nm.setSpawnedAt(LocalDateTime.now());
            	nightmareService.save(nm);
                newBoss = nm;
            }
            
            case 4 -> {
            	GlobalBossFlamor fl = flamorService.get();
            	fl.setAlive(true);
            	fl.setCurrentHp(fl.getMaxHp());
            	fl.setSpawnedAt(LocalDateTime.now());
                flamorService.save(fl);
                newBoss = fl;
            }
            
            case 5 -> {
            	GlobalBossOblivar ob = oblivarService.get();
            	ob.setAlive(true);
            	ob.setCurrentHp(ob.getMaxHp());
            	ob.setSpawnedAt(LocalDateTime.now());
                oblivarService.save(ob);
                newBoss = ob;
            }
            
            case 6 -> {
            	  GlobalBossUmbraxis um = umbraxisService.get();
                  um.setAlive(true);
                  um.setCurrentHp(um.getMaxHp());
                  um.setSpawnedAt(LocalDateTime.now());
                  umbraxisService.save(um);
                  newBoss = um;
            }
            
            case 7 -> {
            	GlobalBossLyxara lx = lyxaraService.get();
            	lx.setAlive(true);
            	lx.setCurrentHp(lx.getMaxHp());
            	lx.setSpawnedAt(LocalDateTime.now());
            	lyxaraService.save(lx);
                newBoss = lx;
          }
          
            
            case 8 -> {
            	GlobalBossNoxar nx = noxarService.get();
            	nx.setAlive(true);
            	nx.setCurrentHp(nx.getMaxHp());
            	nx.setSpawnedAt(LocalDateTime.now());
            	noxarService.save(nx);
                newBoss = nx;
          }
          
            case 9 -> {
            	GlobalBossUmbrar ub = umbrarService.get();
            	ub.setAlive(true);
            	ub.setCurrentHp(ub.getMaxHp());
            	ub.setSpawnedAt(LocalDateTime.now());
            	umbrarService.save(ub);
                newBoss = ub;
          }
            
            case 10 -> {
            	GlobalBossMorvath mv = morvathService.get();
            	mv.setAlive(true);
            	mv.setCurrentHp(mv.getMaxHp());
            	mv.setSpawnedAt(LocalDateTime.now());
            	morvathService.save(mv);
                newBoss = mv;
          }
            
            case 11 -> {
            	GlobalBossObliquo oq = obliquoService.get();
            	oq.setAlive(true);
            	oq.setCurrentHp(oq.getMaxHp());
            	oq.setSpawnedAt(LocalDateTime.now());
            	obliquoService.save(oq);
                newBoss = oq;
          }
            
            case 12 -> {
            	GlobalBossPyragon pg = pyragonService.get();
            	pg.setAlive(true);
            	pg.setCurrentHp(pg.getMaxHp());
            	pg.setSpawnedAt(LocalDateTime.now());
            	pyragonService.save(pg);
                newBoss = pg;
          }
            
            case 13-> {
            	GlobalBossGlaciorn gc = glaciornService.get();
            	gc.setAlive(true);
            	gc.setCurrentHp(gc.getMaxHp());
            	gc.setSpawnedAt(LocalDateTime.now());
            	glaciornService.save(gc);
                newBoss = gc;
          }
            
            case 14-> {
            	GlobalBossReflexa rx = reflexaService.get();
            	rx.setAlive(true);
            	rx.setCurrentHp(rx.getMaxHp());
            	rx.setSpawnedAt(LocalDateTime.now());
            	reflexaService.save(rx);
                newBoss = rx;
          }
            
            case 15-> {
            	GlobalBossMechadron mc = mechadronService.get();
            	mc.setAlive(true);
            	mc.setCurrentHp(mc.getMaxHp());
            	mc.setSpawnedAt(LocalDateTime.now());
            	mechadronService.save(mc);
                newBoss = mc;
          }
           
            case 16-> {
            	GlobalBossNoctyr nr = noctyrService.get();
            	nr.setAlive(true);
            	nr.setCurrentHp(nr.getMaxHp());
            	nr.setSpawnedAt(LocalDateTime.now());
            	noctyrService.save(nr);
                newBoss = nr;
          }
            
            case 17-> {
            	GlobalBossOblivion on = oblivionService.get();
            	on.setAlive(true);
            	on.setCurrentHp(on.getMaxHp());
            	on.setSpawnedAt(LocalDateTime.now());
            	oblivionService.save(on);
                newBoss = on;
          }
            
            case 18 -> {
            	GlobalBossVespera vs = vesperaService.get();
            	vs.setAlive(true);
            	vs.setCurrentHp(vs.getMaxHp());
            	vs.setSpawnedAt(LocalDateTime.now());
            	vesperaService.save(vs);
            	newBoss = vs;
            }
            
            case 19 -> {
            	GlobalBossTenebris ts = tenebrisService.get();
            	ts.setAlive(true);
            	ts.setCurrentHp(ts.getMaxHp());
            	ts.setSpawnedAt(LocalDateTime.now());
            	tenebrisService.save(ts);
            	newBoss = ts;
            }
            
            case 20 -> {
            	GlobalBossGlaciara gl = glaciaraService.get();
            	gl.setAlive(true);
            	gl.setCurrentHp(gl.getMaxHp());
            	gl.setSpawnedAt(LocalDateTime.now());
            	glaciaraService.save(gl);
            	newBoss = gl;
            }
            
            case 21 -> {
            	GlobalBossInfernax ix = infernaxService.get();
            	ix.setAlive(true);
            	ix.setCurrentHp(ix.getMaxHp());
            	ix.setSpawnedAt(LocalDateTime.now());
            	infernaxService.save(ix);
            	newBoss = ix;
            }
            
            case 22 -> {
            	GlobalBossThunderon td = thunderonService.get();
            	td.setAlive(true);
            	td.setCurrentHp(td.getMaxHp());
            	td.setSpawnedAt(LocalDateTime.now());
            	thunderonService.save(td);
            	newBoss = td;
            }
            
            
            
            default -> {
                GlobalBossUmbraxis um = umbraxisService.get();
                um.setAlive(true);
                um.setCurrentHp(um.getMaxHp());
                um.setSpawnedAt(LocalDateTime.now());
                umbraxisService.save(um);
                newBoss = um;
            }
        }

        return newBoss;
    }

    
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
    	
    	
    	
        ig.setAlive(false);
        dr.setAlive(false);
        az.setAlive(false);
        um.setAlive(false);
        nm.setAlive(false);
        fl.setAlive(false);
        ob.setAlive(false);
        lx.setAlive(false);
        nx.setAlive(false);
        ub.setAlive(false);
        mv.setAlive(false);
        oq.setAlive(false);
        pg.setAlive(false);
        gc.setAlive(false);
        rx.setAlive(false);
        mc.setAlive(false);
        nr.setAlive(false);
        on.setAlive(false);
        vs.setAlive(false);
        ts.setAlive(false);
        gl.setAlive(false);
        ix.setAlive(false);
        td.setAlive(false);
        
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
    
    //===============================  tryHitBossAuto =====================================================
    //=====================================================================================================
    
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
        
        registrarDano(bossName, usuario, damage);

        return processReward(bossName, boss, damage);
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
        
        
        
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "Nenhum boss ativo no momento"
        );
    }
    
    
    //============================== ataque do boss =======================================
 

    
}


/**
 * package com.boss_battle.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.boss_battle.model.*;
import com.boss_battle.repository.BossDamageLogRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
public class GlobalBossService {

    private final IgnorathService ignorathService;
    private final DrakthorService drakthorService;
    private final AzurionService azurionService;
    private final UmbraxisService umbraxisService;
    private final NightmareService nightmareService;
    private final FlamorService flamorService;
    private final OblivarService oblivarService;
    private final LyxaraService lyxaraService;
    
    
    private final BossDamageLogRepository damageLogRepo;
    private final UsuarioBossBattleRepository usuarioRepo;
    
    
    private final ReferidosRecompensaService referidosService;
    private final UsuarioBossBattleService usuarioService;
    private final BossAttackService bossAttackService;
    private final BossDamageLogService bossDamageLogService;

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
            
            BossDamageLogRepository damageLogRepo,
            UsuarioBossBattleRepository usuarioRepo,
            ReferidosRecompensaService referidosService,
            UsuarioBossBattleService usuarioService,
            BossAttackService bossAttackService,
            BossDamageLogService bossDamageLogService
            
    ) {
        this.ignorathService = ignorathService;
        this.drakthorService = drakthorService;
        this.azurionService = azurionService;
        this.umbraxisService = umbraxisService;
        this.damageLogRepo = damageLogRepo;
        this.usuarioRepo = usuarioRepo;
        this.referidosService = referidosService;
        this.usuarioService = usuarioService;
        this.bossAttackService = bossAttackService;
        this.bossDamageLogService = bossDamageLogService;
        this.nightmareService = nightmareService;
        this.flamorService = flamorService;
        this.oblivarService = oblivarService;
        this.lyxaraService = lyxaraService;
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

        return spawnRandomBoss();
    }

    // =============================
    // HIT NO BOSS ATIVO
    // =============================
    public Object hitActiveBoss(long usuarioId) {
        UsuarioBossBattle usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!bossAttackService.podeAtacar(usuario)) {
            return Map.of(
                "status", "COOLDOWN",
                "message", "Aguarde para atacar novamente",
                "segundosRestantes", bossAttackService.tempoRestanteSegundos(usuario)
            );
        }

        long damage = usuario.getAtaqueBase();
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
        
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "O boss foi derrotado. Aguarde o respawn."
        );
        
        
    }

    // =============================
    // FUNÇÕES AUXILIARES
    // =============================
    private Object tryHitBoss(String bossName, BattleBoss boss, UsuarioBossBattle usuario, long damage) {
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
        
        

        registrarDano(bossName, usuario, damage);

        return processReward(bossName, boss, damage); // seu método continua para distribuir recompensas
    }


    private void registrarDano(String bossName, UsuarioBossBattle usuario, long damage) {
        BossDamageLog log = new BossDamageLog();
        log.setBossName(bossName);
        log.setUserId(usuario.getId());
        log.setDamage(damage);
        damageLogRepo.save(log);
    }

    private Object processReward(String bossName, BattleBoss boss, long damage) {
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
            UsuarioBossBattle u = usuarioRepo.findById(entry.getKey()).orElse(null);
            if (u == null) continue;

            long danoUsuario = Math.min(entry.getValue(), bossHpMax);
            double proporcao = (double) danoUsuario / totalDamage;
            long rewardFinal = Math.round(bossReward * proporcao);
            long expFinal = Math.round(expReward * proporcao);

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

    private Object finalizeHit(long usuarioId, Object resultado) {
        bossAttackService.registrarAtaque(usuarioId);
        return resultado;
    }

    // =============================
    // SPAWN E RESET
    // =============================
    public BattleBoss spawnRandomBoss() {
        killAllBosses();

        int choice = random.nextInt(7);
        BattleBoss newBoss;

        switch (choice) {
            case 0 -> {
                GlobalBossIgnorath ig = ignorathService.get();
                ig.setAlive(true);
                ig.setCurrentHp(ig.getMaxHp());
                ig.setSpawnedAt(LocalDateTime.now());
                ignorathService.save(ig);
                newBoss = ig;
            }
            case 1 -> {
                GlobalBossDrakthor dr = drakthorService.get();
                dr.setAlive(true);
                dr.setCurrentHp(dr.getMaxHp());
                dr.setSpawnedAt(LocalDateTime.now());
                drakthorService.save(dr);
                newBoss = dr;
            }
            case 2 -> {
                GlobalBossAzurion az = azurionService.get();
                az.setAlive(true);
                az.setCurrentHp(az.getMaxHp());
                az.setSpawnedAt(LocalDateTime.now());
                azurionService.save(az);
                newBoss = az;
            }
            
            case 3 -> {
            	GlobalBossNightmare nm = nightmareService.get();
            	nm.setAlive(true);
            	nm.setCurrentHp(nm.getMaxHp());
            	nm.setSpawnedAt(LocalDateTime.now());
            	nightmareService.save(nm);
                newBoss = nm;
            }
            
            case 4 -> {
            	GlobalBossFlamor fl = flamorService.get();
            	fl.setAlive(true);
            	fl.setCurrentHp(fl.getMaxHp());
            	fl.setSpawnedAt(LocalDateTime.now());
                flamorService.save(fl);
                newBoss = fl;
            }
            
            case 5 -> {
            	GlobalBossOblivar ob = oblivarService.get();
            	ob.setAlive(true);
            	ob.setCurrentHp(ob.getMaxHp());
            	ob.setSpawnedAt(LocalDateTime.now());
                oblivarService.save(ob);
                newBoss = ob;
            }
            
            case 6 -> {
            	  GlobalBossUmbraxis um = umbraxisService.get();
                  um.setAlive(true);
                  um.setCurrentHp(um.getMaxHp());
                  um.setSpawnedAt(LocalDateTime.now());
                  umbraxisService.save(um);
                  newBoss = um;
            }
            
            case 7 -> {
            	GlobalBossLyxara lx = lyxaraService.get();
            	lx.setAlive(true);
            	lx.setCurrentHp(lx.getMaxHp());
            	lx.setSpawnedAt(LocalDateTime.now());
            	lyxaraService.save(lx);
                newBoss = lx;
          }
          
          
            
            default -> {
                GlobalBossUmbraxis um = umbraxisService.get();
                um.setAlive(true);
                um.setCurrentHp(um.getMaxHp());
                um.setSpawnedAt(LocalDateTime.now());
                umbraxisService.save(um);
                newBoss = um;
            }
        }

        return newBoss;
    }

    public void killAllBosses() {
        GlobalBossIgnorath ig = ignorathService.get();
        GlobalBossDrakthor dr = drakthorService.get();
        GlobalBossAzurion az = azurionService.get();
        GlobalBossUmbraxis um = umbraxisService.get();
        GlobalBossNightmare nm = nightmareService.get();
        GlobalBossFlamor fl = flamorService.get();
        GlobalBossOblivar ob = oblivarService.get();
        GlobalBossLyxara lx = lyxaraService.get();
        
        ig.setAlive(false);
        dr.setAlive(false);
        az.setAlive(false);
        um.setAlive(false);
        nm.setAlive(false);
        fl.setAlive(false);
        ob.setAlive(false);
        lx.setAlive(false);
        
        
        ignorathService.save(ig);
        drakthorService.save(dr);
        azurionService.save(az);
        umbraxisService.save(um);
        nightmareService.save(nm);
        flamorService.save(fl);
        oblivarService.save(ob);
        lyxaraService.save(lx);
        
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
    
    //===============================  tryHitBossAuto =====================================================
    //=====================================================================================================
    
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
        
        
        
        registrarDano(bossName, usuario, damage);

        return processReward(bossName, boss, damage);
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
        
        
        return Map.of(
            "status", "NO_BOSS",
            "message", "Nenhum boss ativo no momento"
        );
    }
 
    
}

 */

