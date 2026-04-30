package com.boss_battle.dto;

public class RecompensaAnuncioDTO {

    private boolean success;
    private String tipo;
    private long valor;
    private String descricao;
    private int streakAtual;
    private boolean ganhouItem;
    private String itemGanho;
    private String imagemItem;
    private int quantidadeItem;

    public RecompensaAnuncioDTO(
            boolean success,
            String tipo,
            long valor,
            String descricao,
            int streakAtual,
            boolean ganhouItem,
            String itemGanho,
            String imagemItem,
            int quantidadeItem
    ) {
        this.success = success;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.streakAtual = streakAtual;
        this.ganhouItem = ganhouItem;
        this.itemGanho = itemGanho;
        this.imagemItem = imagemItem;
        this.quantidadeItem = quantidadeItem;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTipo() {
        return tipo;
    }

    public long getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getStreakAtual() {
        return streakAtual;
    }

    public boolean isGanhouItem() {
        return ganhouItem;
    }

    public String getItemGanho() {
        return itemGanho;
    }

    public String getImagemItem() {
        return imagemItem;
    }

    public int getQuantidadeItem() {
        return quantidadeItem;
    }
}