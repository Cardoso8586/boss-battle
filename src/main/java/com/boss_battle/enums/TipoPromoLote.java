package com.boss_battle.enums;

import java.util.Map;

public enum TipoPromoLote {

    NORMAL(
        1_000,
        Map.of(
            TipoPromo.GUERREIRO, 2,
            TipoPromo.  POCAO_VIGOR, 2,
            TipoPromo.ESPADA_FLANEJANTE, 1
        )
    ),

    AVANCADA(
        3_000,
        Map.of(
            TipoPromo.GUERREIRO, 4,
            TipoPromo.  POCAO_VIGOR, 3,
            TipoPromo.ESPADA_FLANEJANTE, 2,
            TipoPromo.MACHADO_DILACERADOR, 1
        )
    ),

    ESPECIAL(
        7_000,
        Map.of(
            TipoPromo.GUERREIRO, 6,
            TipoPromo.  POCAO_VIGOR, 5,
            TipoPromo.ESPADA_FLANEJANTE, 3,
            TipoPromo.MACHADO_DILACERADOR, 2
        )
    ),

    LENDARIA(
        15_000,
        Map.of(
            TipoPromo.GUERREIRO, 10,
            TipoPromo.POCAO_VIGOR, 8,
            TipoPromo.ESPADA_FLANEJANTE, 5,
            TipoPromo.MACHADO_DILACERADOR, 3,
            TipoPromo.ARCO_CELESTIAL, 3
           
        )
    );

    private final long preco;
    private final Map<TipoPromo, Integer> itens;

    TipoPromoLote(long preco, Map<TipoPromo, Integer> itens) {
        this.preco = preco;
        this.itens = itens;
    }

    public long getPreco() {
        return preco;
    }

    public Map<TipoPromo, Integer> getItens() {
        return itens;
    }

    public static TipoPromoLote fromString(String valor) {
        try {
            return TipoPromoLote.valueOf(valor.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
