package com.boss_battle.service.global_boss;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.BattleBoss;
import com.boss_battle.model.GlobalBossAbissal;
import com.boss_battle.model.GlobalBossAbyssar;
import com.boss_battle.model.GlobalBossAzraelPrime;
import com.boss_battle.model.GlobalBossAzurion;
import com.boss_battle.model.GlobalBossDestruidor;
import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.model.GlobalBossKaelthor;
import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.model.GlobalBossMalphion;
import com.boss_battle.model.GlobalBossMechadron;
import com.boss_battle.model.GlobalBossMorvath;
import com.boss_battle.model.GlobalBossNecrothar;
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
import com.boss_battle.model.GlobalBossTrigonBaphydrax;
import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.model.GlobalBossVespera;
import com.boss_battle.service.bosses.AbissalService;
import com.boss_battle.service.bosses.AbyssarService;
import com.boss_battle.service.bosses.AzraelPrimeService;
import com.boss_battle.service.bosses.AzurionService;
import com.boss_battle.service.bosses.DestruidorService;
import com.boss_battle.service.bosses.DrakthorService;
import com.boss_battle.service.bosses.FlamorService;
import com.boss_battle.service.bosses.GlaciaraService;
import com.boss_battle.service.bosses.GlaciornService;
import com.boss_battle.service.bosses.IgnorathService;
import com.boss_battle.service.bosses.InfernaxService;
import com.boss_battle.service.bosses.KaelthorService;
import com.boss_battle.service.bosses.LyxaraService;
import com.boss_battle.service.bosses.MalphionService;
import com.boss_battle.service.bosses.MechadronService;
import com.boss_battle.service.bosses.MorvathService;
import com.boss_battle.service.bosses.NecrotharService;
import com.boss_battle.service.bosses.NightmareService;
import com.boss_battle.service.bosses.NoctharionService;
import com.boss_battle.service.bosses.NoctyrService;
import com.boss_battle.service.bosses.NoxarService;
import com.boss_battle.service.bosses.ObliquoService;
import com.boss_battle.service.bosses.OblivarService;
import com.boss_battle.service.bosses.OblivionService;
import com.boss_battle.service.bosses.PyragonService;
import com.boss_battle.service.bosses.ReflexaService;
import com.boss_battle.service.bosses.TenebrisService;
import com.boss_battle.service.bosses.ThunderonService;
import com.boss_battle.service.bosses.TrigonBaphydraxService;
import com.boss_battle.service.bosses.UmbrarService;
import com.boss_battle.service.bosses.UmbraxisService;
import com.boss_battle.service.bosses.VesperaService;


@Service
@Transactional
public class SpawRandomBossService {
	
	
	    @Autowired
	    KillAllBossesService KillAllBossesService;
	 
	    
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
	    
	 
	    private final Random random = new Random();

	    
	  

	    public SpawRandomBossService(
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
	            AbissalService abissalService
	            
	            
	           
	            
	    ) {
	    	
	    	//================ BOSS ===================
	        this.ignorathService = ignorathService;
	        this.drakthorService = drakthorService;
	        this.azurionService = azurionService;
	        this.umbraxisService = umbraxisService;
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
	      
	    }

	
	 public BattleBoss spawnRandomBoss() {
	    	
	      KillAllBossesService.killAllBosses();
	     
	        
	        int choice = random.nextInt(30);
	        BattleBoss newBoss;

	        switch (choice) {
	        
	            case 0 -> {
	            	 GlobalBossIgnorath ig = ignorathService.get();
	            	 ignorathService.aplicarEscalamentoIgnorath(ig);
	                 ig.setProcessingDeath(false);
	                 ig.setAlive(true);
	                 ig.setCurrentHp(ig.getMaxHp());
	                 ig.setSpawnedAt(LocalDateTime.now());
	                 ignorathService.save(ig);
	                 newBoss = ig;
	            }
	            case 1 -> {
	                GlobalBossDrakthor dr = drakthorService.get();
	                drakthorService.aplicarEscalamentoDrakthor(dr);
	                dr.setProcessingDeath(false);
	                dr.setAlive(true);
	                dr.setCurrentHp(dr.getMaxHp());
	                dr.setSpawnedAt(LocalDateTime.now());
	                drakthorService.save(dr);
	                newBoss = dr;
	            }
	            case 2 -> {
	                GlobalBossAzurion az = azurionService.get();
	                azurionService.aplicarEscalamentoAzurion(az);
	                az.setProcessingDeath(false);
	                az.setAlive(true);
	                az.setCurrentHp(az.getMaxHp());
	                az.setSpawnedAt(LocalDateTime.now());
	                azurionService.save(az);
	                newBoss = az;
	            }
	            
	            case 3 -> {
	            	GlobalBossNightmare nm = nightmareService.get();
	            	nightmareService.aplicarEscalamentoNightmare(nm);
	            	nm.setProcessingDeath(false);
	            	nm.setAlive(true);
	            	nm.setCurrentHp(nm.getMaxHp());
	            	nm.setSpawnedAt(LocalDateTime.now());
	            	nightmareService.save(nm);
	                newBoss = nm;
	            }
	            
	            case 4 -> {
	            	GlobalBossFlamor fl = flamorService.get();
	            	flamorService.aplicarEscalamentoFlamor(fl);
	            	fl.setProcessingDeath(false);
	            	fl.setAlive(true);
	            	fl.setCurrentHp(fl.getMaxHp());
	            	fl.setSpawnedAt(LocalDateTime.now());
	                flamorService.save(fl);
	                newBoss = fl;
	            }
	            
	            case 5 -> {
	            	GlobalBossOblivar ob = oblivarService.get();
	            	oblivarService.aplicarEscalamentoOblivar(ob);
	            	ob.setProcessingDeath(false);
	            	ob.setAlive(true);
	            	ob.setCurrentHp(ob.getMaxHp());
	            	ob.setSpawnedAt(LocalDateTime.now());
	                oblivarService.save(ob);
	                newBoss = ob;
	            }
	            
	            case 6 -> {
	            	  GlobalBossUmbraxis um = umbraxisService.get();
	            	  umbraxisService.aplicarEscalamentoUmbraxis(um);
	            	  um.setProcessingDeath(false);
	                  um.setAlive(true);
	                  um.setCurrentHp(um.getMaxHp());
	                  um.setSpawnedAt(LocalDateTime.now());
	                  umbraxisService.save(um);
	                  newBoss = um;
	            }
	            
	            case 7 -> {
	            	GlobalBossLyxara lx = lyxaraService.get();
	            	lyxaraService.aplicarEscalamentoLyxara(lx);
	            	lx.setProcessingDeath(false);
	            	lx.setAlive(true);
	            	lx.setCurrentHp(lx.getMaxHp());
	            	lx.setSpawnedAt(LocalDateTime.now());
	            	lyxaraService.save(lx);
	                newBoss = lx;
	          }
	          
	            
	            case 8 -> {
	            	GlobalBossNoxar nx = noxarService.get();
	            	noxarService.aplicarEscalamentoNoxar(nx);
	            	nx.setProcessingDeath(false);
	            	nx.setAlive(true);
	            	nx.setCurrentHp(nx.getMaxHp());
	            	nx.setSpawnedAt(LocalDateTime.now());
	            	noxarService.save(nx);
	                newBoss = nx;
	          }
	          
	            case 9 -> {
	            	GlobalBossUmbrar ub = umbrarService.get();
	            	umbrarService.aplicarEscalamentoUmbrar(ub);
	            	ub.setProcessingDeath(false);
	            	ub.setAlive(true);
	            	ub.setCurrentHp(ub.getMaxHp());
	            	ub.setSpawnedAt(LocalDateTime.now());
	            	umbrarService.save(ub);
	                newBoss = ub;
	          }
	            
	            case 10 -> {
	            	GlobalBossMorvath mv = morvathService.get();
	            	morvathService.aplicarEscalamentoMorvat(mv);
	            	mv.setProcessingDeath(false);
	            	mv.setAlive(true);
	            	mv.setCurrentHp(mv.getMaxHp());
	            	mv.setSpawnedAt(LocalDateTime.now());
	            	morvathService.save(mv);
	                newBoss = mv;
	          }
	            
	            case 11 -> {
	            	GlobalBossObliquo oq = obliquoService.get();
	            	obliquoService.aplicarEscalamentoObliquo(oq);
	            	oq.setProcessingDeath(false);
	            	oq.setAlive(true);
	            	oq.setCurrentHp(oq.getMaxHp());
	            	oq.setSpawnedAt(LocalDateTime.now());
	            	obliquoService.save(oq);
	                newBoss = oq;
	          }
	            
	            case 12 -> {
	            	GlobalBossPyragon pg = pyragonService.get();
	            	pyragonService.aplicarEscalamentoPyragon(pg);
	            	pg.setProcessingDeath(false);
	            	pg.setAlive(true);
	            	pg.setCurrentHp(pg.getMaxHp());
	            	pg.setSpawnedAt(LocalDateTime.now());
	            	pyragonService.save(pg);
	                newBoss = pg;
	          }
	            
	            case 13-> {
	            	GlobalBossGlaciorn gc = glaciornService.get();
	            	glaciornService.aplicarEscalamentoGlaciorn(gc);
	            	gc.setProcessingDeath(false);
	            	gc.setAlive(true);
	            	gc.setCurrentHp(gc.getMaxHp());
	            	gc.setSpawnedAt(LocalDateTime.now());
	            	glaciornService.save(gc);
	                newBoss = gc;
	          }
	            
	            case 14-> {
	            	GlobalBossReflexa rx = reflexaService.get();
	            	reflexaService.aplicarEscalamentoReflexa(rx);
	            	rx.setProcessingDeath(false);
	            	rx.setAlive(true);
	            	rx.setCurrentHp(rx.getMaxHp());
	            	rx.setSpawnedAt(LocalDateTime.now());
	            	reflexaService.save(rx);
	                newBoss = rx;
	          }
	            
	            case 15-> {
	            	GlobalBossMechadron mc = mechadronService.get();
	            	mechadronService.aplicarEscalamentoMechadron(mc);
	            	mc.setProcessingDeath(false);
	            	mc.setAlive(true);
	            	mc.setCurrentHp(mc.getMaxHp());
	            	mc.setSpawnedAt(LocalDateTime.now());
	            	mechadronService.save(mc);
	                newBoss = mc;
	          }
	           
	            case 16-> {
	            	GlobalBossNoctyr nr = noctyrService.get();
	            	noctyrService.aplicarEscalamentoNoctyr(nr);
	            	nr.setProcessingDeath(false);
	            	nr.setAlive(true);
	            	nr.setCurrentHp(nr.getMaxHp());
	            	nr.setSpawnedAt(LocalDateTime.now());
	            	noctyrService.save(nr);
	                newBoss = nr;
	          }
	            
	            case 17-> {
	            	GlobalBossOblivion on = oblivionService.get();
	            	oblivionService.aplicarEscalamentoOblivion(on);
	            	on.setProcessingDeath(false);
	            	on.setAlive(true);
	            	on.setCurrentHp(on.getMaxHp());
	            	on.setSpawnedAt(LocalDateTime.now());
	            	oblivionService.save(on);
	                newBoss = on;
	          }
	            
	            case 18 -> {
	            	GlobalBossVespera vs = vesperaService.get();
	            	vesperaService.aplicarEscalamentoVespera(vs);
	            	vs.setProcessingDeath(false);
	            	vs.setAlive(true);
	            	vs.setCurrentHp(vs.getMaxHp());
	            	vs.setSpawnedAt(LocalDateTime.now());
	            	vesperaService.save(vs);
	            	newBoss = vs;
	            }
	            
	            case 19 -> {
	            	GlobalBossTenebris ts = tenebrisService.get();
	            	tenebrisService.aplicarEscalamentoTenebris(ts);
	            	ts.setProcessingDeath(false);
	            	ts.setAlive(true);
	            	ts.setCurrentHp(ts.getMaxHp());
	            	ts.setSpawnedAt(LocalDateTime.now());
	            	tenebrisService.save(ts);
	            	newBoss = ts;
	            }
	            
	            case 20 -> {
	            	GlobalBossGlaciara gl = glaciaraService.get();
	            	glaciaraService.aplicarEscalamentoGlaciara(gl);
	            	gl.setProcessingDeath(false);
	            	gl.setAlive(true);
	            	gl.setCurrentHp(gl.getMaxHp());
	            	gl.setSpawnedAt(LocalDateTime.now());
	            	glaciaraService.save(gl);
	            	newBoss = gl;
	            }
	            
	            case 21 -> {
	            	GlobalBossInfernax ix = infernaxService.get();
	            	infernaxService.aplicarEscalamentoInfernax(ix);
	            	ix.setProcessingDeath(false);
	            	ix.setAlive(true);
	            	ix.setCurrentHp(ix.getMaxHp());
	            	ix.setSpawnedAt(LocalDateTime.now());
	            	infernaxService.save(ix);
	            	newBoss = ix;
	            }
	            
	            case 22 -> {
	            	GlobalBossThunderon td = thunderonService.get();
	            	thunderonService.aplicarEscalamentoThunderon(td);
	            	td.setProcessingDeath(false);
	            	td.setAlive(true);
	            	td.setCurrentHp(td.getMaxHp());
	            	td.setSpawnedAt(LocalDateTime.now());
	            	thunderonService.save(td);
	            	newBoss = td;
	            }
	            
	            case 23 -> {
	            	GlobalBossNoctharion nt = noctharionService.get();
	            	noctharionService.aplicarEscalamentoNoctharion(nt);
	            	nt.setProcessingDeath(false);
	            	nt.setAlive(true);
	            	nt.setCurrentHp(nt.getMaxHp());
	            	nt.setSpawnedAt(LocalDateTime.now());
	            	noctharionService.save(nt);
	            	newBoss = nt;
	            }
	            
	            case 24 -> {
	            	GlobalBossAzraelPrime ap = azraelPrimeService.get();
	            	azraelPrimeService.aplicarEscalamentoAzraelPrime(ap);
	            	ap.setProcessingDeath(false);
	            	ap.setAlive(true);
	            	ap.setCurrentHp(ap.getMaxHp());
	            	ap.setSpawnedAt(LocalDateTime.now());
	            	azraelPrimeService.save(ap);
	            	newBoss = ap;
	            }
	            case 25 -> {
	            	GlobalBossDestruidor ds = destruidorService.get();
	            	destruidorService.aplicarEscalamentoDestruidor(ds);
	            	ds.setProcessingDeath(false);
	            	ds.setAlive(true);
	            	ds.setCurrentHp(ds.getMaxHp());
	            	ds.setSpawnedAt(LocalDateTime.now());
	            	destruidorService.save(ds);
	            	newBoss = ds;
	            }
	            
	            case 26 -> {
	            	GlobalBossTrigonBaphydrax tb = trigonBaphydraxService.get();
	            	trigonBaphydraxService.aplicarEscalamentoTrigon(tb);
	            	tb.setProcessingDeath(false);
	            	tb.setAlive(true);
	            	tb.setCurrentHp(tb.getMaxHp());
	            	tb.setSpawnedAt(LocalDateTime.now());
	            	trigonBaphydraxService.save(tb);
	            	newBoss = tb;
	            }
	            case 27 -> {
	            	GlobalBossMalphion mph = malphionService.get();
	            	malphionService.aplicarEscalamentoMalphion(mph);
	            	mph.setProcessingDeath(false);
	            	mph.setAlive(true);
	            	mph.setCurrentHp(mph.getMaxHp());
	            	mph.setSpawnedAt(LocalDateTime.now());
	            	malphionService.save(mph);
	            	newBoss = mph;

	            }
	            
	            case 28 -> {
	            	GlobalBossAbyssar aby = abyssarService.get();
	            	abyssarService.aplicarEscalamentoAbyssar(aby);
	            	aby.setProcessingDeath(false);
	            	aby.setAlive(true);
	            	aby.setCurrentHp(aby.getMaxHp());
	            	aby.setSpawnedAt(LocalDateTime.now());
	            	abyssarService.save(aby);
	            	newBoss = aby;

	            }
	            
	            case 29 -> {
	            	GlobalBossNecrothar nthr = necrotharService.get();
	            	necrotharService.aplicarEscalamentoNecrothar(nthr);
	            	nthr.setProcessingDeath(false);
	            	nthr.setAlive(true);
	            	nthr.setCurrentHp(nthr.getMaxHp());
	            	nthr.setSpawnedAt(LocalDateTime.now());
	            	necrotharService.save(nthr);
	            	newBoss = nthr;

	            }
	            case 30 -> {
	            	GlobalBossKaelthor kael = kaelthorService.get();
	            	kaelthorService.aplicarEscalamentoTempestade(kael);
	            	kael.setProcessingDeath(false);
	            	kael.setAlive(true);
	            	kael.setCurrentHp(kael.getMaxHp());
	            	kael.setSpawnedAt(LocalDateTime.now());
	            	kaelthorService.save(kael);
	            	newBoss = kael;

	            }
	            
	            case 31 -> {
	            	GlobalBossAbissal abis = abissalService.get();
	            	abissalService.aplicarEscalamentoAbissal(abis);
	            	abis.setProcessingDeath(false);
	            	abis.setAlive(true);
	            	abis.setCurrentHp(abis.getMaxHp());
	            	abis.setSpawnedAt(LocalDateTime.now());
	            	abissalService.save(abis);
	            	newBoss = abis;

	            }
	            
	            
	 
	            default -> {
	                GlobalBossUmbraxis um = umbraxisService.get();
	                umbraxisService.aplicarEscalamentoUmbraxis(um);
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

	
	

}
