package com.boss_battle.dto;

import com.boss_battle.enums.TipoFlecha;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ColocarFlechasAljavaRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private TipoFlecha tipo;

    @Min(1)
    private int quantidade;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public TipoFlecha getTipo() {
        return tipo;
    }

    public void setTipo(TipoFlecha tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
