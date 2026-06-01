package com.boss_battle.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.repository.UsuarioGuerreiroRepository;
import com.boss_battle.service.auto_ataque.GuerreiroAutoAttackService;


@RestController
public class AtualizarStatusUsuarioController {

	
	  @Autowired
	    private  UsuarioGuerreiroRepository usuarioGuerreiroRepository;
	  
	  
	@Autowired
	GuerreiroAutoAttackService guerreiroAutoAttackService;
	
    @Autowired
    private UsuarioBossBattleRepository repo;

    @GetMapping("/api/atualizar/status/usuario/{usuarioId}")
    public Map<String, Object> getUsuario(@PathVariable Long usuarioId) {
        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

      
     

        Integer quantidadeSaques = usuario.getQuantidadeSaquesDiario() == null 
                ? 0 
                : usuario.getQuantidadeSaquesDiario();
/*
        String status = "Saques hoje: " + quantidadeSaquesHoje + "/" + DAILY_WITHDRAW_LIMIT +
                        " | Restantes: " + saquesRestantes;
        */
        
        BigDecimal ultimoGanho = usuario.getUltimoValorRecebido();
        if (ultimoGanho == null) ultimoGanho = BigDecimal.ZERO;
      
        long gueereirosAtaque =   usuario.getGuerreiros() + quantidadeTotalGuerreirosElite(usuario);
        
        
        return Map.ofEntries(
        	    Map.entry("totalGuerreiros", totalGuerreiros(usuarioId)),
        	    Map.entry("id", usuario.getId()),
        	    Map.entry("email", usuario.getEmail()),
        	    Map.entry("energiaGuerreiros", usuario.getEnergiaGuerreiros()),
        	    Map.entry("energiaGuerreirosPadrao", usuario.getEnergiaGuerreirosPadrao()),
        	    Map.entry("guerreiros",gueereirosAtaque),// guerreiros no ataque
        	    Map.entry("ataquePorMinuto", ataquePorMinuto(usuarioId)),
        	    Map.entry("xp", usuario.getExp()),
        	    Map.entry("nivel", usuario.getNivel()),
        	    Map.entry("bossCoin", usuario.getBossCoins()),
        	    Map.entry(
        	    	    "ataqueBase",
        	    	    calcularAtaqueBaseTotal(usuario)
        	    	),
        	    Map.entry("ganhosRef", ganhosRef(usuarioId)),
        	    Map.entry("guerreirosRetaguarda", usuario.getGuerreirosRetaguarda()),
        	    Map.entry("espadaflanejante", usuario.getEspadaFlanejante()),
        	    Map.entry("desgasteEspadaFlanejante", usuario.getEspadaFlanejanteDesgaste()),
        	    Map.entry("ativaEspadaFlanejante", usuario.getEspadaFlanejanteAtiva()),
         	    Map.entry("capacidadeVigor", usuario.getEnergiaGuerreirosPadrao()),
         	    Map.entry("pocaoVigor", usuario.getPocaoVigor()),
        	    
        	    Map.entry("machadoDilacerador", usuario.getMachadoDilacerador()),
        	    Map.entry("desgasteMachadoDilacerador", usuario.getMachadoDilaceradorDesgaste()),
        	    Map.entry("ativarMachadoDilacerador", usuario.getMachadoDilaceradorAtivo()),
        	    
        	    Map.entry("arcoInventario", usuario.getInventarioArco()),
        	    Map.entry("arcoAtivo", usuario.getArcoAtivo()),
        	    Map.entry("durabilidadeArco", usuario.getDurabilidadeArco()),
        	    Map.entry("aljava", usuario.getAljava()), 
        	    Map.entry("flechaFerro", usuario.getFlechaFerro()), 
        	    Map.entry("flechaFogo", usuario.getFlechaFogo()), 
        	    Map.entry("flechaDiamante", usuario.getFlechaDiamante()),
        	    Map.entry("flechaVeneno", usuario.getFlechaVeneno()), 
        	    Map.entry("tipoFlecha", TipoFlecha.fromOrdinal(usuario.getAljavaFlechaAtiva()).name()),

        	    Map.entry("quantidadeSaquesHoje", quantidadeSaques),
        	    Map.entry("ultimoValorRecebido", ultimoGanho),
        	    
        	    
        	    Map.entry("escudoPrimordial", usuario.getEscudoPrimordial()),
        	    Map.entry("desgasteEscudoPrimordial", usuario.getEscudoPrimordialDesgaste()),
        	    Map.entry("ativarEscudoPrimordial", usuario.getEscudoPrimordialAtivo())
        	    
        	);

    }
    
    //totalGuerreiros
    private long totalGuerreiros(@PathVariable Long usuarioId) {
  	  UsuarioBossBattle usuario = repo.findById(usuarioId)
  	            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
  	  //aqtaque por minuto
        
  	long guerreiroAtaque = usuario.getGuerreiros();
    long guerreiroRetaguarda = usuario.getGuerreirosRetaguarda();
    long guerreiroInventario= usuario.getGuerreirosInventario();
    long quantidadeTotalGuerriro =
            guerreiroAtaque
          + guerreiroRetaguarda
          + guerreiroInventario
          + quantidadeTotalGuerreirosElite(usuario);
    
        //
  	  
  	  return quantidadeTotalGuerriro;
  	  
    }//---<
    
    public long quantidadeTotalGuerreirosElite(UsuarioBossBattle usuario) {

        return usuarioGuerreiroRepository
                .findByUsuario(usuario)
                .stream()
                .mapToLong(ug -> ug.getQuantidade() == null ? 0L : ug.getQuantidade())
                .sum();
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
        
       long arcoAtivo = usuario.getArcoAtivo();

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
    
       resultado.put("arcoAtivo", arcoAtivo);
       
       
       resultado.put("escudoPrimordial", usuario.getEscudoPrimordial());
       resultado.put("desgasteEscudoPrimordial", usuario.getEscudoPrimordialDesgaste());
       resultado.put("ativarEscudoPrimordial", usuario.getEscudoPrimordialAtivo());
 
        return resultado;
    }
    
    public long calcularAtaqueBaseTotal(UsuarioBossBattle usuario) {

        long ataqueBaseJogador = Math.max(0L, usuario.getAtaqueBase());

        return ataqueBaseJogador + calcularAtaqueBaseElite(usuario);
    }
    
    public long calcularAtaqueBaseElite(UsuarioBossBattle usuario) {

        return usuarioGuerreiroRepository
                .findByUsuario(usuario)
                .stream()
                .mapToLong(ug -> {

                    if (ug.getQuantidade() == null) return 0L;
                    if (ug.getGuerreiro() == null) return 0L;
                    if (ug.getGuerreiro().getDanoBase() == null) return 0L;

                    return ug.getQuantidade() *
                            ug.getGuerreiro().getDanoBase();
                })
                .sum();
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
	  
	  long danoElite = calcularDanoElite(usuario);
      
      long quantGuerreiros = usuario.getGuerreiros();
      long quantAtaqueBaseGuerreiros = usuario.getAtaqueBaseGuerreiros();
      
      long totalAtaquePorMinuto =(quantGuerreiros * quantAtaqueBaseGuerreiros ) + danoElite ;
  
      //
	  
	  return totalAtaquePorMinuto;
	  
  }//---<
   
  public long calcularDanoElite(UsuarioBossBattle usuario) {

	    return usuarioGuerreiroRepository
	            .findByUsuario(usuario)
	            .stream()
	            .mapToLong(ug -> {

	                if (ug.getQuantidade() == null) return 0L;
	                if (ug.getGuerreiro() == null) return 0L;
	                if (ug.getGuerreiro().getDanoBase() == null) return 0L;

	                return ug.getQuantidade() * ug.getGuerreiro().getDanoBase();
	            })
	            .sum();
	}

}
