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
    private Integer quantidadeMaxima;
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
            boolean ativo,
            Integer quantidadeMaxima
    ) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.danoBase = danoBase;
        this.imagem = imagem;
        this.precoAtual = precoAtual;
        this.quantidadeUsuario = quantidadeUsuario;
        this.ativo = ativo;
        this.quantidadeMaxima = quantidadeMaxima;
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

    public Long getDanoBase() {
        return danoBase;
    }

    public String getImagem() {
        return imagem;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public Long getQuantidadeUsuario() {
        return quantidadeUsuario;
    }

    public Integer getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDanoBase(Long danoBase) {
        this.danoBase = danoBase;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public void setQuantidadeUsuario(Long quantidadeUsuario) {
        this.quantidadeUsuario = quantidadeUsuario;
    }

    public void setQuantidadeMaxima(Integer quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}