package com.boss_battle.dto.cacador_reconpensasDTO;

import java.math.BigDecimal;

public class RuinasPerdidasResponse {

    private boolean sucesso;
    private String mensagem;
    private String premio;
    private String tipoPremio;
    private Long valorGanho;
    private BigDecimal bossCoinsAtual;
    private Long ataqueEspecialBonus;
    private Integer tentativasRestantesHoje;
    private Long cooldownSegundos;
    private boolean emCooldown;

    public RuinasPerdidasResponse() {
    }

    public RuinasPerdidasResponse(
            boolean sucesso,
            String mensagem,
            String premio,
            String tipoPremio,
            Long valorGanho,
            BigDecimal bossCoinsAtual,
            Long ataqueEspecialBonus,
            Integer tentativasRestantesHoje,
            Long cooldownSegundos,
            boolean emCooldown
    ) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.premio = premio;
        this.tipoPremio = tipoPremio;
        this.valorGanho = valorGanho;
        this.bossCoinsAtual = bossCoinsAtual;
        this.ataqueEspecialBonus = ataqueEspecialBonus;
        this.tentativasRestantesHoje = tentativasRestantesHoje;
        this.cooldownSegundos = cooldownSegundos;
        this.emCooldown = emCooldown;
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

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public String getTipoPremio() {
        return tipoPremio;
    }

    public void setTipoPremio(String tipoPremio) {
        this.tipoPremio = tipoPremio;
    }

    public Long getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(Long valorGanho) {
        this.valorGanho = valorGanho;
    }

    public BigDecimal getBossCoinsAtual() {
        return bossCoinsAtual;
    }

    public void setBossCoinsAtual(BigDecimal bossCoinsAtual) {
        this.bossCoinsAtual = bossCoinsAtual;
    }

    public Long getAtaqueEspecialBonus() {
        return ataqueEspecialBonus;
    }

    public void setAtaqueEspecialBonus(Long ataqueEspecialBonus) {
        this.ataqueEspecialBonus = ataqueEspecialBonus;
    }

    public Integer getTentativasRestantesHoje() {
        return tentativasRestantesHoje;
    }

    public void setTentativasRestantesHoje(Integer tentativasRestantesHoje) {
        this.tentativasRestantesHoje = tentativasRestantesHoje;
    }

    public Long getCooldownSegundos() {
        return cooldownSegundos;
    }

    public void setCooldownSegundos(Long cooldownSegundos) {
        this.cooldownSegundos = cooldownSegundos;
    }

    public boolean isEmCooldown() {
        return emCooldown;
    }

    public void setEmCooldown(boolean emCooldown) {
        this.emCooldown = emCooldown;
    }
}