package com.boss_battle.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ativar_equipar.RecarregarEnergiaService;


@Controller
public class EnergiaController {

	
	 @Autowired
	    private UsuarioBossBattleRepository repo;
    @Autowired
    private RecarregarEnergiaService energiaService;


    
    @PostMapping("/recarregar-energia")
    @ResponseBody
    public UsuarioBossBattle recarregarEnergia(@RequestParam Long usuarioId) {
        energiaService.recarregarEnergia(usuarioId);
        return repo.findById(usuarioId)
                   .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));
    }


}
