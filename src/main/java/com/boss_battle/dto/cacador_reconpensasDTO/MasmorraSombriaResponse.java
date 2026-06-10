package com.boss_battle.dto.cacador_reconpensasDTO;

import java.math.BigDecimal;
import java.util.Map;

public class MasmorraSombriaResponse {

    private boolean sucesso;
    private String status;
    private String mensagem;

    private String inimigoNome;
    private String inimigoImagem;

    private Long inimigoHpAtual;
    private Long inimigoHpMax;
    private Long inimigoAtaque;

    private Long danoUsuario;
    private Long danoRecebido;

    private Long vigorAtual;
    private Long ataqueBase;

    private BigDecimal bossCoinsAtual;

    private Integer tentativasHoje;
    private Integer limite;

    private Map<String, Object> recompensa;

    public MasmorraSombriaResponse() {
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getInimigoNome() {
        return inimigoNome;
    }

    public void setInimigoNome(String inimigoNome) {
        this.inimigoNome = inimigoNome;
    }

    public String getInimigoImagem() {
        return inimigoImagem;
    }

    public void setInimigoImagem(String inimigoImagem) {
        this.inimigoImagem = inimigoImagem;
    }

    public Long getInimigoHpAtual() {
        return inimigoHpAtual;
    }

    public void setInimigoHpAtual(Long inimigoHpAtual) {
        this.inimigoHpAtual = inimigoHpAtual;
    }

    public Long getInimigoHpMax() {
        return inimigoHpMax;
    }

    public void setInimigoHpMax(Long inimigoHpMax) {
        this.inimigoHpMax = inimigoHpMax;
    }

    public Long getInimigoAtaque() {
        return inimigoAtaque;
    }

    public void setInimigoAtaque(Long inimigoAtaque) {
        this.inimigoAtaque = inimigoAtaque;
    }

    public Long getDanoUsuario() {
        return danoUsuario;
    }

    public void setDanoUsuario(Long danoUsuario) {
        this.danoUsuario = danoUsuario;
    }

    public Long getDanoRecebido() {
        return danoRecebido;
    }

    public void setDanoRecebido(Long danoRecebido) {
        this.danoRecebido = danoRecebido;
    }

    public Long getVigorAtual() {
        return vigorAtual;
    }

    public void setVigorAtual(Long vigorAtual) {
        this.vigorAtual = vigorAtual;
    }

    public Long getAtaqueBase() {
        return ataqueBase;
    }

    public void setAtaqueBase(Long ataqueBase) {
        this.ataqueBase = ataqueBase;
    }

    public BigDecimal getBossCoinsAtual() {
        return bossCoinsAtual;
    }

    public void setBossCoinsAtual(BigDecimal bossCoinsAtual) {
        this.bossCoinsAtual = bossCoinsAtual;
    }

    public Integer getTentativasHoje() {
        return tentativasHoje;
    }

    public void setTentativasHoje(Integer tentativasHoje) {
        this.tentativasHoje = tentativasHoje;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public Map<String, Object> getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(Map<String, Object> recompensa) {
        this.recompensa = recompensa;
    }
}