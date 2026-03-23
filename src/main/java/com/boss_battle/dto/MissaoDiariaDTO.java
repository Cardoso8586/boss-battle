package com.boss_battle.dto;


public class MissaoDiariaDTO {

    private long danoAtual;
    private long danoObjetivo;
    private int nivelDano;
    private int recompensaDano;
    private boolean podeResgatarDano;

    private int ataquesAtual;
    private int ataquesObjetivo;
    private int nivelAtaques;
    private int recompensaAtaques;
    private boolean podeResgatarAtaques;

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
}