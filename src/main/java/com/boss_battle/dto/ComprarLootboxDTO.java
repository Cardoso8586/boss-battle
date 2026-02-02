package com.boss_battle.dto;

public class ComprarLootboxDTO {
    private String tipoLootbox; // basica, avancada, especial, lendaria
    private int quantidade;      // quantas lootboxes abrir

    public String getTipoLootbox() {
        return tipoLootbox;
    }

    public void setTipoLootbox(String tipoLootbox) {
        this.tipoLootbox = tipoLootbox;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
