package com.boss_battle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.model.BossDamageLog;
import com.boss_battle.service.BossDamageLogService;

@RestController
@RequestMapping("/api/boss")
public class BossDamageLogController {

    @Autowired
    private BossDamageLogService service;

    


    // GET /api/boss/ataques-recentes
    @GetMapping("/ataques-recentes")
    public List<BossDamageLog> ataquesRecentes() {
        return service.ultimosAtaques(10);
    }

}

