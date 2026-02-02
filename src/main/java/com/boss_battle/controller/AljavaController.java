package com.boss_battle.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.AljavaStatusResponseDTO;
import com.boss_battle.dto.ColocarFlechasAljavaRequest;
import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.AljavaService;

import jakarta.validation.Valid;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/aljava")
public class AljavaController {

    @Autowired
    private AljavaService aljavaService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @PostMapping("/colocar")
    @Transactional
    public ResponseEntity<?> colocarFlechas(
            @RequestBody @Valid ColocarFlechasAljavaRequest request
    ) {
        try {
            UsuarioBossBattle usuario = usuarioRepository
                    .findByIdForUpdate(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            aljavaService.colocarFlechasNaAljava(
                    usuario,
                    request.getTipo(),
                    request.getQuantidade()
            );

            AljavaStatusResponseDTO response = new AljavaStatusResponseDTO(
                    usuario.getId(),
                    usuario.getAljava(),
                    usuario.getAljava() > 0
                            ? TipoFlecha.fromOrdinal(usuario.getAljavaFlechaAtiva())
                            : null,
                    Map.of(
                            "ferro", usuario.getFlechaFerro(),
                            "fogo", usuario.getFlechaFogo(),
                            "veneno", usuario.getFlechaVeneno(),
                            "diamante", usuario.getFlechaDiamante()
                    )
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "error", e.getMessage()
                    ));
        }

    }

    
    /*
    @PostMapping("/colocar")
    @Transactional // ⚡ aqui
    public ResponseEntity<AljavaStatusResponseDTO> colocarFlechas(
            @RequestBody @Valid ColocarFlechasAljavaRequest request
    ) {
        UsuarioBossBattle usuario = usuarioRepository
                .findByIdForUpdate(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        aljavaService.colocarFlechasNaAljava(
                usuario,
                request.getTipo(),
                request.getQuantidade()
        );

        AljavaStatusResponseDTO response = new AljavaStatusResponseDTO(
                usuario.getId(),
                usuario.getAljava(),
                usuario.getAljava() > 0
                    ? TipoFlecha.fromOrdinal(usuario.getAljavaFlechaAtiva())
                    : null,
                Map.of(
                    "ferro", usuario.getFlechaFerro(),
                    "fogo", usuario.getFlechaFogo(),
                    "veneno", usuario.getFlechaVeneno(),
                    "diamante", usuario.getFlechaDiamante()
                )
        );

        return ResponseEntity.ok(response);
    }
    
    */
}
