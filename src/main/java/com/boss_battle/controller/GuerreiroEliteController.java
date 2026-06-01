package com.boss_battle.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.CompraGuerreiroEliteRequest;
import com.boss_battle.dto.CompraGuerreiroEliteResponse;
import com.boss_battle.service.loja_guerreiros_elite.LojaGuerreiroEliteService;
@RestController
@RequestMapping("/boss-battle/guerreiros-elite")
public class GuerreiroEliteController {

    private final LojaGuerreiroEliteService service;

    public GuerreiroEliteController(LojaGuerreiroEliteService service) {
        this.service = service;
    }

    @PostMapping("/comprar/{usuarioId}")
    public CompraGuerreiroEliteResponse comprar(
            @PathVariable Long usuarioId,
            @RequestBody CompraGuerreiroEliteRequest request
    ) {
        return service.comprarGuerreiroElite(
                usuarioId,
                request.getGuerreiroId(),
                request.getQuantidade()
        );
    }
}