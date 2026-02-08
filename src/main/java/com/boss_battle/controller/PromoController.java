package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.ComprarPromoDTO;
import com.boss_battle.dto.PromoPrecoDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.PromoService;

@RestController
@RequestMapping("/promo")
public class PromoController {

    @Autowired
    private PromoService promoService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @PostMapping("/comprar/{usuarioId}/{tipoPromo}")
    public ResponseEntity<String> comprarPromo(
            @PathVariable Long usuarioId,
            @PathVariable String tipoPromo) {

        try {
            UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String resultado = promoService.comprarPromo(usuario, tipoPromo);

            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
   
    // 🔍 PREVIEW DO PREÇO (SEM COMPRAR)
    @GetMapping("/preco/{usuarioId}/{tipoPromo}")
    public ResponseEntity<PromoPrecoDTO> obterPrecoFinal(
            @PathVariable Long usuarioId,
            @PathVariable String tipoPromo) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(
            promoService.calcularPreviewPromo(usuario, tipoPromo)
        );
    }


}
