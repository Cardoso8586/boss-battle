package com.boss_battle.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.GuerreiroUsuarioCardResponse;
import com.boss_battle.service.loja_guerreiros_elite.LojaGuerreiroEliteService;

@RestController
public class GuerreiroUsuarioController {

    private final LojaGuerreiroEliteService lojaGuerreiroEliteService;

    public GuerreiroUsuarioController(LojaGuerreiroEliteService lojaGuerreiroEliteService) {
        this.lojaGuerreiroEliteService = lojaGuerreiroEliteService;
    }

    @GetMapping("/guerreiros/usuario/{usuarioId}")
    public List<GuerreiroUsuarioCardResponse> listarGuerreirosUsuario(
            @PathVariable Long usuarioId
    ) {
        return lojaGuerreiroEliteService.listarGuerreirosUsuario(usuarioId);
    }
}