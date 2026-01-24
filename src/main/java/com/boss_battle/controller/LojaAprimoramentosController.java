package com.boss_battle.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.LojaAprimoramentosService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LojaAprimoramentosController {

	@Autowired
	LojaAprimoramentosService lojaAprimoramentosService;
	
	
    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @GetMapping("/api/loja/{usuarioId}")
    public ResponseEntity<?> obterLoja(@PathVariable Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        long precoPocaoVigor = usuario.getPrecoPocaoVigor();
        if (precoPocaoVigor <= 0) {
            precoPocaoVigor = 5_000L;
            usuario.setPrecoPocaoVigor(precoPocaoVigor);
            usuarioRepository.save(usuario);
        }
        long precoEspadaFlanejante = usuario.getPrecoEspadaFlanejante();
        if (precoEspadaFlanejante <= 0) {
        	precoEspadaFlanejante = 5_000L;
            usuario.setPrecoEspadaFlanejante(precoEspadaFlanejante);
            usuarioRepository.save(usuario);
        }
        long precoMachadoDilacerador = usuario.getPrecoMachadoDilacerador();
        if (precoMachadoDilacerador <= 0) {
        	precoMachadoDilacerador = 5_000L;
            usuario.setPrecoMachadoDilacerador(precoMachadoDilacerador);
            usuarioRepository.save(usuario);
        }
        
        long precoGuerreiros = usuario.getPrecoGuerreiros();
        if (precoGuerreiros <= 0) {
        	precoGuerreiros = 10_000L;
            usuario.setPrecoGuerreiros(precoGuerreiros);
            usuarioRepository.save(usuario);
        }
        
        long precoAtaqueEspecial = usuario.getPrecoAtaqueEspecial();
        if (precoAtaqueEspecial <= 0) {
        	precoAtaqueEspecial = 1_000L;
            usuario.setPrecoAtaqueEspecial(precoAtaqueEspecial);
            usuarioRepository.save(usuario);
        }
        
        long precoEnergia = usuario.getPrecoEnergia();
        if (precoEnergia <= 0) {
        	precoEnergia = 1_000L;
            usuario.setPrecoEnergia(precoEnergia);
              usuarioRepository.save(usuario);
        }
      
        //-------------------------------------------------------------------------------------------------
          Map<String, Object> loja = new HashMap<>();
        loja.put("precoGuerreiros", precoGuerreiros);
        loja.put("precoEnergia", precoEnergia);
        loja.put("precoAtaqueEspecial", precoAtaqueEspecial);
        loja.put("PrecoPocaoAutomaticaVigor", precoPocaoVigor);
        loja.put("PrecoEspadaFlanejante", precoEspadaFlanejante);
        loja.put("PrecoMachadoDilacerador", precoMachadoDilacerador);
        

      
/*
 *   Map<String, Object> loja = new HashMap<>();
        loja.put("precoGuerreiros", usuario.getPrecoGuerreiros());
        loja.put("precoEnergia", usuario.getPrecoEnergia());
        loja.put("precoAtaqueEspecial", usuario.getPrecoAtaqueEspecial());
        loja.put("PrecoPocaoAutomaticaVigor", usuario.getPrecoPocaoVigor());
        loja.put("PrecoEspadaFlanejante", usuario.getPrecoEspadaFlanejante());
        loja.put("PrecoMachadoDilacerador", usuario.getPrecoMachadoDilacerador());
*/

        loja.put("nivel", usuario.getNivel());

        return ResponseEntity.ok(loja);
    }
    
    
}
