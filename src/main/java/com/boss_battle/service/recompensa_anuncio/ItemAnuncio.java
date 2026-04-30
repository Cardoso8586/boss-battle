package com.boss_battle.service.recompensa_anuncio;

import com.boss_battle.enums.RewardItem;

public class ItemAnuncio {

    private RewardItem rewardItem;
    private String nome;
    private int quantidade;
    private String imagemUrl;

    public ItemAnuncio(RewardItem rewardItem, String nome, int quantidade, String imagemUrl) {
        this.rewardItem = rewardItem;
        this.nome = nome;
        this.quantidade = quantidade;
        this.imagemUrl = imagemUrl;
    }

    public RewardItem getRewardItem() {
        return rewardItem;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }
}