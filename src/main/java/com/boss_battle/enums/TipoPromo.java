package com.boss_battle.enums;

public enum TipoPromo {

    GUERREIRO(1),
    POCAO_VIGOR(1),
    ESPADA_FLANEJANTE(1),
    MACHADO_DILACERADOR(1),
    ARCO_CELESTIAL(1),
    CAPACIDADE_VIGOR(1);
	
    private final int quantidade;

    TipoPromo(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public static TipoPromo fromOrdinal(long ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            return null;
        }
        return values()[(int) ordinal];
    }
}
