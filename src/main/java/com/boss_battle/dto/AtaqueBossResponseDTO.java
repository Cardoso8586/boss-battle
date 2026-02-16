package com.boss_battle.dto;

import java.time.LocalDateTime;

public class AtaqueBossResponseDTO {

    private String mensagem;
    private Long dano;
    private LocalDateTime dataAtaque;

    public AtaqueBossResponseDTO(String mensagem, Long dano, LocalDateTime dataAtaque) {
        this.mensagem = mensagem;
        this.dano = dano;
        this.dataAtaque = dataAtaque;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Long getDano() {
        return dano;
    }

    public LocalDateTime getDataAtaque() {
        return dataAtaque;
    }
}
