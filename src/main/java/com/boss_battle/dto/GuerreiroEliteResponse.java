package com.boss_battle.dto;

import java.math.BigDecimal;

public class GuerreiroEliteResponse {

    private Long id;
    private String nome;
    private String tipo;
    private Long danoBase;
    private String imagem;
    private BigDecimal precoAtual;
    private Long quantidadeUsuario;
    private boolean ativo;

    public GuerreiroEliteResponse() {
    }

    public GuerreiroEliteResponse(
            Long id,
            String nome,
            String tipo,
            Long danoBase,
            String imagem,
            BigDecimal precoAtual,
            Long quantidadeUsuario,
            boolean ativo
    ) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.imagem = imagem;
        this.precoAtual = precoAtual;
        this.quantidadeUsuario = quantidadeUsuario;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getDanoBase() {
        return danoBase;
    }

    public void setDanoBase(Long danoBase) {
        this.danoBase = danoBase;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public Long getQuantidadeUsuario() {
        return quantidadeUsuario;
    }

    public void setQuantidadeUsuario(Long quantidadeUsuario) {
        this.quantidadeUsuario = quantidadeUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}