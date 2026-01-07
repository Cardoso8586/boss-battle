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
        
       
        
        

        Map<String, Object> loja = new HashMap<>();

        loja.put("precoGuerreiros", usuario.getPrecoGuerreiros());
        loja.put("precoEnergia", usuario.getPrecoEnergia());
        loja.put("precoAtaqueEspecial", usuario.getPrecoAtaqueEspecial());
        loja.put("PrecoPocaoAutomaticaVigor", lojaAprimoramentosService.getPrecoBasePorcaovigor());
        loja.put("PrecoEspadaFlanejante", lojaAprimoramentosService.getPrecoBaseEspadaFlanejante());


        loja.put("nivel", usuario.getNivel());

        return ResponseEntity.ok(loja);
    }
    
    
}
