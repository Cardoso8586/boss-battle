package com.boss_battle.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "controle_sistema")
public class ControleSistema {

    @Id
    private Long id;

    private LocalDate ultimaExecucaoRankingSemanal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getUltimaExecucaoRankingSemanal() {
        return ultimaExecucaoRankingSemanal;
    }

    public void setUltimaExecucaoRankingSemanal(LocalDate ultimaExecucaoRankingSemanal) {
        this.ultimaExecucaoRankingSemanal = ultimaExecucaoRankingSemanal;
    }
}