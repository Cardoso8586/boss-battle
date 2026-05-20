package com.boss_battle.dto;

public class MissaoDiariaDTO {

    //====================== MISSÃO DANO ======================

    private long danoAtual;
    private long danoObjetivo;
    private int nivelDano;
    private int recompensaDano;
    private boolean podeResgatarDano;

    //====================== MISSÃO ATAQUES ======================

    private int ataquesAtual;
    private int ataquesObjetivo;
    private int nivelAtaques;
    private int recompensaAtaques;
    private boolean podeResgatarAtaques;

    //====================== MISSÃO PTC ======================

    private int ptcAtual;
    private int ptcObjetivo;
    private int nivelPtc;
    private int recompensaPtc;
    private boolean podeResgatarPtc;

    //====================== GETTERS / SETTERS DANO ======================

    public long getDanoAtual() {
        return danoAtual;
    }

    public void setDanoAtual(long danoAtual) {
        this.danoAtual = danoAtual;
    }

    public long getDanoObjetivo() {
        return danoObjetivo;
    }

    public void setDanoObjetivo(long danoObjetivo) {
        this.danoObjetivo = danoObjetivo;
    }

    public int getNivelDano() {
        return nivelDano;
    }

    public void setNivelDano(int nivelDano) {
        this.nivelDano = nivelDano;
    }

    public int getRecompensaDano() {
        return recompensaDano;
    }

    public void setRecompensaDano(int recompensaDano) {
        this.recompensaDano = recompensaDano;
    }

    public boolean isPodeResgatarDano() {
        return podeResgatarDano;
    }

    public void setPodeResgatarDano(boolean podeResgatarDano) {
        this.podeResgatarDano = podeResgatarDano;
    }

    //====================== GETTERS / SETTERS ATAQUES ======================

    public int getAtaquesAtual() {
        return ataquesAtual;
    }

    public void setAtaquesAtual(int ataquesAtual) {
        this.ataquesAtual = ataquesAtual;
    }

    public int getAtaquesObjetivo() {
        return ataquesObjetivo;
    }

    public void setAtaquesObjetivo(int ataquesObjetivo) {
        this.ataquesObjetivo = ataquesObjetivo;
    }

    public int getNivelAtaques() {
        return nivelAtaques;
    }

    public void setNivelAtaques(int nivelAtaques) {
        this.nivelAtaques = nivelAtaques;
    }

    public int getRecompensaAtaques() {
        return recompensaAtaques;
    }

    public void setRecompensaAtaques(int recompensaAtaques) {
        this.recompensaAtaques = recompensaAtaques;
    }

    public boolean isPodeResgatarAtaques() {
        return podeResgatarAtaques;
    }

    public void setPodeResgatarAtaques(boolean podeResgatarAtaques) {
        this.podeResgatarAtaques = podeResgatarAtaques;
    }

    //====================== GETTERS / SETTERS PTC ======================

    public int getPtcAtual() {
        return ptcAtual;
    }

    public void setPtcAtual(int ptcAtual) {
        this.ptcAtual = ptcAtual;
    }

    public int getPtcObjetivo() {
        return ptcObjetivo;
    }

    public void setPtcObjetivo(int ptcObjetivo) {
        this.ptcObjetivo = ptcObjetivo;
    }

    public int getNivelPtc() {
        return nivelPtc;
    }

    public void setNivelPtc(int nivelPtc) {
        this.nivelPtc = nivelPtc;
    }

    public int getRecompensaPtc() {
        return recompensaPtc;
    }

    public void setRecompensaPtc(int recompensaPtc) {
        this.recompensaPtc = recompensaPtc;
    }

    public boolean isPodeResgatarPtc() {
        return podeResgatarPtc;
    }

    public void setPodeResgatarPtc(boolean podeResgatarPtc) {
        this.podeResgatarPtc = podeResgatarPtc;
    }
}