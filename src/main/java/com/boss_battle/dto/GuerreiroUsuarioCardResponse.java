
package com.boss_battle.dto;

public class GuerreiroUsuarioCardResponse {

    private Long id;
    private String nome;
    private String tipo;
    private Long quantidade;
    private Long danoBase;
    private Long danoTotal;
    private String imagem;
    private boolean elite;

    public GuerreiroUsuarioCardResponse() {
    }

    public GuerreiroUsuarioCardResponse(
            Long id,
            String nome,
            String tipo,
            Long quantidade,
            Long danoBase,
            Long danoTotal,
            String imagem,
            boolean elite
    ) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.danoBase = danoBase;
        this.danoTotal = danoTotal;
        this.imagem = imagem;
        this.elite = elite;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public Long getDanoBase() {
        return danoBase;
    }

    public Long getDanoTotal() {
        return danoTotal;
    }

    public String getImagem() {
        return imagem;
    }

    public boolean isElite() {
        return elite;
    }
}