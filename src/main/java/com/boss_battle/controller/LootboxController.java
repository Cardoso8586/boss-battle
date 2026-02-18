package com.boss_battle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.dto.ComprarLootboxDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.aprimoramentos_loja.LootboxService;


@RestController
@RequestMapping("/lootbox")
public class LootboxController {

    @Autowired
    private LootboxService lootboxService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @PostMapping("/abrir/{usuarioId}")
    public ResponseEntity<String> abrirLootbox(
            @PathVariable Long usuarioId,
            @RequestBody ComprarLootboxDTO dto) {

        try {
            UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            StringBuilder resultado = new StringBuilder();

            for (int i = 0; i < dto.getQuantidade(); i++) {
                resultado.append(
                    lootboxService.abrirLootboxPorNivel(
                        usuario,
                        dto.getTipoLootbox()
                    )
                ).append("<br>");
            }

            return ResponseEntity.ok(resultado.toString());

        } catch (RuntimeException e) {
            // ✅ ERRO LIMPO, SEM HTML, SEM STACKTRACE
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    

    
}
