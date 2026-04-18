package com.boss_battle.service.ativar_equipar;

public class ResultadoDano {
    private long danoFinal;
    private boolean usouEscudo;

    public ResultadoDano(long danoFinal, boolean usouEscudo) {
        this.danoFinal = danoFinal;
        this.usouEscudo = usouEscudo;
    }

    public long getDanoFinal() {
        return danoFinal;
    }

    public boolean isUsouEscudo() {
        return usouEscudo;
    }
}