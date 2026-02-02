package com.boss_battle.enums;

public enum TipoFlecha {
    FERRO,     // 0
    FOGO,      // 1
    VENENO,    // 2
    DIAMANTE;  // 3

    public int getPoder() {
        return switch (this) {
            case FERRO -> 3;
            case FOGO -> 5;
            case VENENO -> 7;
            case DIAMANTE -> 10;
        };
    }

    public static TipoFlecha fromOrdinal(long ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            return null;
        }
        return values()[(int) ordinal];
    }
}
