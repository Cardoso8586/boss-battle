package com.boss_battle.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.GuerreiroAutoAttackService;

@RestController
public class AtualizarStatusUsuarioController {

	@Autowired
	GuerreiroAutoAttackService guerreiroAutoAttackService;
	
    @Autowired
    private UsuarioBossBattleRepository repo;

    @GetMapping("/api/atualizar/status/usuario/{usuarioId}")
    public Map<String, Object> getUsuario(@PathVariable Long usuarioId) {
        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

      
        
        return Map.ofEntries(
        	    Map.entry("id", usuario.getId()),
        	    Map.entry("energiaGuerreiros", usuario.getEnergiaGuerreiros()),
        	    Map.entry("energiaGuerreirosPadrao", usuario.getEnergiaGuerreirosPadrao()),
        	    Map.entry("guerreiros", usuario.getGuerreiros()),
        	    Map.entry("ataquePorMinuto", ataquePorMinuto(usuarioId)),
        	    Map.entry("xp", usuario.getExp()),
        	    Map.entry("nivel", usuario.getNivel()),
        	    Map.entry("bossCoin", usuario.getBossCoins()),
        	    Map.entry("ataqueBase", usuario.getAtaqueBase()),
        	    Map.entry("ganhosRef", ganhosRef(usuarioId)),
        	    Map.entry("guerreirosRetaguarda", usuario.getGuerreirosRetaguarda()),
        	    Map.entry("espadaflanejante", usuario.getEspadaFlanejante()),
        	    Map.entry("desgasteEspadaFlanejante", usuario.getEspadaFlanejanteDesgaste()),
        	    Map.entry("ativaEspadaFlanejante", usuario.getEspadaFlanejanteAtiva()),
        	    
        	    Map.entry("machadoDilacerador", usuario.getMachadoDilacerador()),
        	    Map.entry("desgasteMachadoDilacerador", usuario.getMachadoDilaceradorDesgaste()),
        	    Map.entry("ativarMachadoDilacerador", usuario.getMachadoDilaceradorAtivo())
        	    
        	    
        	);

    }
    
    @GetMapping("/api/atualizar/status/ajustes/{usuarioId}")
    public Map<String, Object> statusPocaoVigor(@PathVariable Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        long qtdEstoque = usuario.getPocaoVigor();
        long qtdAtiva = usuario.getPocaoVigorAtiva();
        long qtdMinima = 1;
        long estoqueGuerreiro = usuario.getGuerreirosInventario();
        boolean podeAtivar = qtdEstoque >= qtdMinima;
        long  guerreirosRetaguarda = usuario.getGuerreirosRetaguarda();
        long ativoGuerreiro = usuario.getGuerreiros();
        
        //espada flanejante
        long qtdMinimaEspada = 1L;
        long qtdEspadasEstoque = usuario.getEspadaFlanejante();
        boolean podeAtivarEspadaFlanejante =
                qtdEspadasEstoque >= qtdMinimaEspada
                && usuario.getEspadaFlanejanteAtiva() == 0;
        long qtdEspadasAtiva   = usuario.getEspadaFlanejanteAtiva();
       
        
        //machado dilacerador
        long qtdMinimaMachado = 1L;
        long qtdMachadoDilaceradorEstoque = usuario.getMachadoDilacerador();
        boolean podeAtivarMachadoDilacerador =
        		qtdMachadoDilaceradorEstoque >= qtdMinimaMachado
                && usuario.getMachadoDilaceradorAtivo() == 0;
       long qtdMachadoDilaceradorAtivo   = usuario.getMachadoDilaceradorAtivo();
        //--------------------------------------------------------------
        
        

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("estoque", qtdEstoque);
        resultado.put("ativa", qtdAtiva);
        resultado.put("quantidadeMinima", qtdMinima);
        resultado.put("podeAtivar", podeAtivar);
        resultado.put("estoqueGuerreiro", estoqueGuerreiro);
        resultado.put("guerreirosRetaguarda", guerreirosRetaguarda);
        resultado.put("ativoGuerreiro", ativoGuerreiro);

       // 🔥 ESPADA FLANEJANTE
       resultado.put("podeAtivarEspadaFlanejante", podeAtivarEspadaFlanejante);
       resultado.put("espadaFlanejanteEstoque", qtdEspadasEstoque);
       resultado.put("espadaFlanejanteAtiva", qtdEspadasAtiva);
    
       // 🔥 MACHADO DILACERADOR
       resultado.put("podeAtivarMachadoDilacerador", podeAtivarMachadoDilacerador);
       resultado.put("qtdMachadoDilaceradorEstoque", qtdMachadoDilaceradorEstoque);
       resultado.put("qtdMachadoDilaceradorAtivo", qtdMachadoDilaceradorAtivo);
    

 
        return resultado;
    }

  //====================================================================================
                                 //  METODOS AUXILIARES //
  //====================================================================================
    
    //--->ganhosRef
    private BigDecimal ganhosRef(@PathVariable Long usuarioId) {
    	
    	UsuarioBossBattle usuario = repo.findById(usuarioId)
	            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
    	
    	 BigDecimal ganhosRef = usuario.getGanhosPendentesReferral() != null ? usuario.getGanhosPendentesReferral() : BigDecimal.ZERO;
         DecimalFormat df = new DecimalFormat("#,##0");
         df.format(ganhosRef);
    	
		return ganhosRef;
    	
    	
    	
    }//---<
    
    //ataquePorMinuto
  private long ataquePorMinuto(@PathVariable Long usuarioId) {
	  UsuarioBossBattle usuario = repo.findById(usuarioId)
	            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
	  //aqtaque por minuto
      
      long quantGuerreiros = usuario.getGuerreiros();
      long quantAtaqueBaseGuerreiros = usuario.getAtaqueBaseGuerreiros();
      long totalAtaquePorMinuto =(quantGuerreiros)* (quantAtaqueBaseGuerreiros);
  
      //
	  
	  return totalAtaquePorMinuto;
	  
  }//---<
   


}
