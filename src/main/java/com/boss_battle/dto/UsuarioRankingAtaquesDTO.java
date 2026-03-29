package com.boss_battle.dto;

public class UsuarioRankingAtaquesDTO {

    private Long id;
    private String username;
    private Integer quantidadeAtaquesSemanal;
    private Integer posicao;

    public UsuarioRankingAtaquesDTO() {
    }

    public UsuarioRankingAtaquesDTO(
            Long id,
            String username,
            Integer quantidadeAtaquesSemanal,
            Integer posicao
    ) {
        this.id = id;
        this.username = username;
        this.quantidadeAtaquesSemanal = quantidadeAtaquesSemanal;
        this.posicao = posicao;
    }

    // getters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getQuantidadeAtaquesSemanal() {
        return quantidadeAtaquesSemanal;
    }

    public Integer getPosicao() {
        return posicao;
    }

    // setters (se precisar)

    public void setQuantidadeAtaquesSemanal(Integer quantidadeAtaquesSemanal) {
        this.quantidadeAtaquesSemanal = quantidadeAtaquesSemanal;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }
}