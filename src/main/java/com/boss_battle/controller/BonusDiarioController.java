package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.service.missoes.BonusDiarioService;



@RestController
@RequestMapping("/api/bonus")
public class BonusDiarioController {
	
	 @Autowired
	    private BonusDiarioService bonusService;
	
	 
	 // 🔥 Coletar bônus diário
	 @PostMapping("/coletar/{usuarioId}")
	 public ResponseEntity<?> coletarBonus(@PathVariable Long usuarioId) {

	     try {

	         String resposta = bonusService.coletarBonusDiario(usuarioId);

	         return ResponseEntity.ok(Map.of(
	             "sucesso", true,
	             "mensagem", resposta
	         ));

	     } catch (Exception e) {

	         return ResponseEntity.badRequest().body(Map.of(
	             "sucesso", false,
	             "mensagem", e.getMessage()
	         ));

	     }
	 }

	    // 🟢 Verificar se bônus está disponível (opcional, mas útil no frontend)
	 @GetMapping("/verificar/{usuarioId}")
	 public Map<String,Object> verificarBonus(@PathVariable Long usuarioId) {

	     boolean disponivel = bonusService.verificarDisponibilidade(usuarioId);

	     return Map.of(
	         "disponivel", disponivel,
	         "mensagem", disponivel 
	             ? "🎁 Bônus diário disponível!" 
	             : "Você já coletou seu bônus hoje."
	     );
	 }
	

}//BonusDiarioController
