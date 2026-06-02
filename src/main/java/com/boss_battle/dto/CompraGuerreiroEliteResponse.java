package com.boss_battle.dto;

import java.math.BigDecimal;

public class CompraGuerreiroEliteResponse {

    private boolean sucesso;
    private String mensagem;
    private BigDecimal bossCoinsAtual;
    private BigDecimal precoAtual;
    private Long quantidadeAtual;

    public CompraGuerreiroEliteResponse() {
    }

    public CompraGuerreiroEliteResponse(
            boolean sucesso,
            String mensagem,
            BigDecimal bossCoinsAtual,
            BigDecimal precoAtual,
            Long quantidadeAtual
    ) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.bossCoinsAtual = bossCoinsAtual;
        this.precoAtual = precoAtual;
        this.quantidadeAtual = quantidadeAtual;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public BigDecimal getBossCoinsAtual() {
        return bossCoinsAtual;
    }

    public void setBossCoinsAtual(BigDecimal bossCoinsAtual) {
        this.bossCoinsAtual = bossCoinsAtual;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public Long getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Long quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
}