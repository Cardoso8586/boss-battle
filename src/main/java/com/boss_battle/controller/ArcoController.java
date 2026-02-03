package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ArcoService;

@RestController
@RequestMapping("/arco")
public class ArcoController {

    @Autowired
    private ArcoService arcoService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    // 🎯 Equipar arco
    @PostMapping("/equipar")
    public ResponseEntity<?> equiparArco(@RequestParam Long usuarioId) {

        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        arcoService.equiparArco(usuario);

        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "message", "Arco equipado com sucesso"
            )
        );
    }

    /*
    @PostMapping("/equipar")
    public ResponseEntity<?> equiparArco(
            @RequestParam Long usuarioId
            
    ) {
        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        arcoService.equiparArco(usuario);

        return ResponseEntity.ok("Arco equipado com sucesso");
    }
*/
 // 🔁 Reativar arco
    @PostMapping("/reativar/{usuarioId}")
    public ResponseEntity<?> reativarArco(
            @PathVariable Long usuarioId
    ) {
        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        arcoService.reativarArco(usuario);

        return ResponseEntity.ok("Arco reativado com sucesso");
    }


    // 🏹 Usar arco (teste / ataque manual)
    @PostMapping("/usar/{usuarioId}")
    public ResponseEntity<?> usarArco(
            @PathVariable Long usuarioId
    ) {
        UsuarioBossBattle usuario = usuarioRepository
                .findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        int bonus = arcoService.usarArco(usuario);

        return ResponseEntity.ok(
            "Flecha usada. Bônus de dano: " + bonus + "%"
        );
    }
}
