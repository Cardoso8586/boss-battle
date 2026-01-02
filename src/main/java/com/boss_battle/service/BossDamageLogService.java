package com.boss_battle.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.boss_battle.dto.BossDamageLogDTO;
import com.boss_battle.repository.BossDamageLogRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BossDamageLogService {

    @Autowired
    private BossDamageLogRepository repo;

  
    @Transactional
    public List<Map<String, Object>> top10Dano() {
        return repo.top10Geral(PageRequest.of(0, 10))
            .stream()
            .map(r -> Map.of(
                "userName", r[1], // já é String
                "ataques", r[2] != null ? convertToLong(r[2]) : 0,
                "damage", r[3] != null ? convertToLong(r[3]) : 0
            ))
            .toList();
    }

    
    public String posicaoUsuario(Long userId) {
        Integer posicao = repo.buscarPosicaoUsuario(userId);
        Integer damage = repo.buscarResumoUsuario(userId);
        DecimalFormat df = new DecimalFormat("#,##0");
        
        if (damage == null) {
            return "Sem dano";
        }
        if (posicao == null) {
            return "Sem ranking | Dano: " + damage;
        }

        return posicao + "º | Dano: " + df.format(damage);
    }


    /**
    public String posicaoUsuario(Long userId) {
        Integer posicao = repo.buscarPosicaoUsuario(userId);
        Integer[] dano = repo.buscarResumoUsuario(userId);
        
        
        if (dano == null) {
            return "Sem dano";
        }
        if (posicao == null) {
            return "Sem ranking";
        }
        return posicao + "º";
    }
*/
    
        // utilitário
    private long convertToLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        } else if (value instanceof String s) {
            return Long.parseLong(s);
        } else {
            throw new IllegalArgumentException("Tipo inesperado: " + value.getClass());
        }
    }

    
    
    
    public List<BossDamageLogDTO> ultimosAtaques(int quantidade) {
        return repo.ultimosAtaques(
            PageRequest.of(0, quantidade)
        );
    }


}
