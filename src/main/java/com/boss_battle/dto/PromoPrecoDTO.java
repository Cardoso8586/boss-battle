package com.boss_battle.dto;

import java.math.BigDecimal;

public class PromoPrecoDTO {

    private BigDecimal precoBruto;
    private BigDecimal precoFinal;
    private int descontoPercentual;

    public PromoPrecoDTO(
            BigDecimal precoBruto,
            BigDecimal precoFinal,
            int descontoPercentual) {

        this.precoBruto = precoBruto;
        this.precoFinal = precoFinal;
        this.descontoPercentual = descontoPercentual;
    }

    public BigDecimal getPrecoBruto() {
        return precoBruto;
    }

    public BigDecimal getPrecoFinal() {
        return precoFinal;
    }

    public int getDescontoPercentual() {
        return descontoPercentual;
    }
}
