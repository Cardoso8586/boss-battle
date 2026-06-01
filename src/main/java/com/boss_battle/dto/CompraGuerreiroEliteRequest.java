package com.boss_battle.dto;


public class CompraGuerreiroEliteRequest {

    private Long guerreiroId;
    private int quantidade;

    public Long getGuerreiroId() {
        return guerreiroId;
    }

    public void setGuerreiroId(Long guerreiroId) {
        this.guerreiroId = guerreiroId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}