package com.boss_battle.service.global_boss;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.boss_battle.model.GlobalBossDrakzhor;
import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.model.GlobalBossGatalicos;
import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.model.GlobalBossKaelthor;
import com.boss_battle.model.GlobalBossLeviatanAbismo;
import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.model.GlobalBossMalevola;
import com.boss_battle.model.GlobalBossMalgryth;
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
import com.boss_battle.model.GlobalBossSHemogarth;
import com.boss_battle.model.GlobalBossTenebris;
import com.boss_battle.model.GlobalBossTharvok;
import com.boss_battle.model.GlobalBossThunderon;
import com.boss_battle.model.GlobalBossTrigonBaphydrax;
import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.model.GlobalBossVZurvio;
import com.boss_battle.model.GlobalBossVespera;
import com.boss_battle.model.GlobalBossVesta;
import com.boss_battle.model.GlobalBossVorgathon;
import com.boss_battle.model.GlobalBossVorlath;
import com.boss_battle.model.GlobalBossXarvokth;
import com.boss_battle.model.GlobalBossZargoth;
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
import com.boss_battle.service.bosses.DrakzhorService;
import com.boss_battle.service.bosses.FlamorService;
import com.boss_battle.service.bosses.GatalicosService;
import com.boss_battle.service.bosses.GlaciaraService;
import com.boss_battle.service.bosses.GlaciornService;
import com.boss_battle.service.bosses.IgnorathService;
import com.boss_battle.service.bosses.InfernaxService;
import com.boss_battle.service.bosses.KaelthorService;
import com.boss_battle.service.bosses.LeviatanAbismoService;
import com.boss_battle.service.bosses.LyxaraService;
import com.boss_battle.service.bosses.MalevolaService;
import com.boss_battle.service.bosses.MalgrythService;
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
import com.boss_battle.service.bosses.SHemogarthService;
import com.boss_battle.service.bosses.TenebrisService;
import com.boss_battle.service.bosses.TharvokService;
import com.boss_battle.service.bosses.ThunderonService;
import com.boss_battle.service.bosses.TrigonBaphydraxService;
import com.boss_battle.service.bosses.UmbrarService;
import com.boss_battle.service.bosses.UmbraxisService;
import com.boss_battle.service.bosses.VZurvioService;
import com.boss_battle.service.bosses.VesperaService;
import com.boss_battle.service.bosses.VestaService;
import com.boss_battle.service.bosses.VorgathonService;
import com.boss_battle.service.bosses.VorlathService;
import com.boss_battle.service.bosses.XarvokthService;
import com.boss_battle.service.bosses.ZargothService;

@Service
@Transactional
public class KillAllBossesService {
	

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
	    
	    //
	    private final VorgathonService vorgathonService;
	    private final DrakzhorService drakzhorService;
	    private final XarvokthService xarvokthService;
	    private final MalgrythService malgrythService;
	    private final TharvokService tharvokService;
	    private final VorlathService vorlathService;
	    private final VZurvioService vZurvioService;
	    private final GatalicosService gatalicosService;
	    private final SHemogarthService sHemogarthService;
	    
	    
	    /*
	 
	   

*/
	    public KillAllBossesService(
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
	            
	            
	            //
	            VorgathonService vorgathonService,
	            DrakzhorService drakzhorService,
	            XarvokthService xarvokthService,
	            MalgrythService malgrythService,
	            TharvokService tharvokService,
	            VorlathService vorlathService,
	            VZurvioService vZurvioService,
	            GatalicosService gatalicosService,
	            SHemogarthService sHemogarthService
	            
	            
	            /*
	           
	           
	            */
	         
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
	        
	        
	        //
	        this.vorgathonService = vorgathonService;
	        this.drakzhorService = drakzhorService;
	        this.xarvokthService = xarvokthService;
	        this.malgrythService = malgrythService;
	        this.tharvokService = tharvokService;
	        this.vorlathService = vorlathService;
	        this.vZurvioService = vZurvioService;
	        this.gatalicosService = gatalicosService;
	        this.sHemogarthService = sHemogarthService;
	        
	        
	        
	        /*
	      
	        
	      */
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
	    	GlobalBossNoctharion nt = noctharionService.get();
	    	GlobalBossAzraelPrime ap = azraelPrimeService.get();
	    	GlobalBossDestruidor ds = destruidorService.get();
	    	GlobalBossTrigonBaphydrax tb = trigonBaphydraxService.get();
	    	GlobalBossMalphion mph = malphionService.get();
	    	GlobalBossAbyssar aby = abyssarService.get();
	    	GlobalBossNecrothar nthr = necrotharService.get();
	    	GlobalBossKaelthor kael = kaelthorService.get();
	    	GlobalBossAbissal abis = abissalService.get();
	    	GlobalBossLeviatanAbismo levi = leviatanAbismoService.get();
	    	GlobalBossZargoth zarg = zargothService.get();
	    	GlobalBossNexarach nexah = nexarachService.get();
	    	GlobalBossCyberion cyber = cyberionService.get();
	    	GlobalBossAzuragon azura = azuragonService.get();
	    	GlobalBossOculthar ocult = ocultharService.get();
	    	GlobalBossPuppetrix pupp = puppetrixService.get();
	    	GlobalBossAbaddon abadon = abaddonService.get();
	    	GlobalBossVesta vesta = vestaService.get();
	    	GlobalBossMalevola malev = malevolaService.get();
	    	GlobalBossObraserker obraser = obraserkerService.get();
	    	GlobalBossCryophantasm cryoph = cryophantasmService.get();
	    	GlobalBossArenascor arena = arenascorService.get();
	    	
	    	
	    	//
	    	GlobalBossVorgathon vorgat = vorgathonService.get();
	    	GlobalBossDrakzhor drakz = drakzhorService.get();
	    	GlobalBossXarvokth xarvo = xarvokthService.get();
	    	GlobalBossMalgryth malgry = malgrythService.get();
	    	GlobalBossTharvok tharvo = tharvokService.get();
	    	GlobalBossVorlath vorla = vorlathService.get();
	    	GlobalBossVZurvio vzrur = vZurvioService.get();
	    	GlobalBossGatalicos galat = gatalicosService.get();
	    	GlobalBossSHemogarth shemo = sHemogarthService.get();
	    	
	    	
	    	
	    	shemo.setAlive(false);
	    	shemo.setProcessingDeath(false);
	    
	    	galat.setAlive(false);
	    	galat.setProcessingDeath(false);
	    	
	    	vzrur.setAlive(false);
	    	vzrur.setProcessingDeath(false);
	    	
	    	vorla.setAlive(false);
	    	vorla.setProcessingDeath(false);
	    	
	    	tharvo.setAlive(false);
	    	tharvo.setProcessingDeath(false);
	    	
	    	malgry.setAlive(false);
	    	malgry.setProcessingDeath(false);
	    	
	    	xarvo.setAlive(false);
	    	xarvo.setProcessingDeath(false);
	    	
	    	drakz.setAlive(false);
	    	drakz.setProcessingDeath(false);
	    	
	    	vorgat.setAlive(false);
	    	vorgat.setProcessingDeath(false);
	    	
	    	//
	    	
	    	arena.setAlive(false);
	    	arena.setProcessingDeath(false);
	    	
	    	cryoph.setAlive(false);
	    	cryoph.setProcessingDeath(false);
	    	
	    	obraser.setAlive(false);
	    	obraser.setProcessingDeath(false);
	    	
	    	malev.setAlive(false);
	    	malev.setProcessingDeath(false);
	    	
	    	vesta.setAlive(false);
	    	vesta.setProcessingDeath(false);
	    	
	    	abadon.setAlive(false);
	    	abadon.setProcessingDeath(false);
	    	
	    	pupp.setAlive(false);
	    	pupp.setProcessingDeath(false);
	    	
	    	ocult.setAlive(false);
	    	ocult.setProcessingDeath(false);
	    	
	    	azura.setAlive(false);
	    	azura.setProcessingDeath(false);
	    	
	    	cyber.setAlive(false);
	    	cyber.setProcessingDeath(false);
	    	
	    	nexah.setAlive(false);
	    	nexah.setProcessingDeath(false);
	    	
	        zarg.setAlive(false);
	    	zarg.setProcessingDeath(false);
	    	
	    	levi.setAlive(false);
	    	levi.setProcessingDeath(false);
	    	
	    	abis.setAlive(false);
	    	abis.setProcessingDeath(false);
	    	
	    	kael.setAlive(false);
	    	kael.setProcessingDeath(false);
	    	
	    	nthr.setAlive(false);
	    	nthr.setProcessingDeath(false);
	    	
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
	    	
	    	tb.setAlive(false);
	    	tb.setProcessingDeath(false);
	    	
	    	mph.setAlive(false);
	    	mph.setProcessingDeath(false);
	    	
	    	aby.setAlive(false);
	    	aby.setProcessingDeath(false);

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
	        trigonBaphydraxService.save(tb);
	        malphionService.save(mph);
	        abyssarService.save(aby);
	        necrotharService.save(nthr);
	        kaelthorService.save(kael);
	        abissalService.save(abis);
	        leviatanAbismoService.save(levi);
	        zargothService.save(zarg);
	        nexarachService.save(nexah);
	        cyberionService.save(cyber);
	        azuragonService.save(azura);
	        ocultharService.save(ocult);
	        puppetrixService.save(pupp);
	        abaddonService.save(abadon);
	        vestaService.save(vesta);
	        malevolaService.save(malev);
	        obraserkerService.save(obraser);
	        cryophantasmService.save(cryoph);
	        arenascorService.save(arena);
	        
	        
	        //
	        vorgathonService.save(vorgat);
	        drakzhorService.save(drakz);
	        xarvokthService.save(xarvo);
	        malgrythService.save(malgry);
	        tharvokService.save(tharvo);
	        vorlathService.save(vorla);
	        gatalicosService.save(galat);
	        sHemogarthService.save(shemo);
	        
	        
	        
	        /*
	       
	       
	      */
	    }

	
	
	

}//--->kILL BOS FIM
