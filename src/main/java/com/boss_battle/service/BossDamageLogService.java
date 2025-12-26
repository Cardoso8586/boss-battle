package com.boss_battle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.boss_battle.model.BossDamageLog;
import com.boss_battle.repository.BossDamageLogRepository;

import jakarta.transaction.Transactional;

@Service
public class BossDamageLogService {

    @Autowired
    private BossDamageLogRepository repo;

    @Transactional
    public void clearBossDamage(String bossName) {
        repo.deleteByBossName(bossName);
    }
    
    
    /**
     * Retorna os últimos n ataques de um boss específico.
     */
    public List<BossDamageLog> ultimosAtaques(int quantidade) {
        return repo.findAllByOrderByIdDesc(
            PageRequest.of(0, quantidade)
        );
    }
    
    
}
