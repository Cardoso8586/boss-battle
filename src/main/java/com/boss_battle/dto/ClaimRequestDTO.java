package com.boss_battle.dto;

import java.math.BigDecimal;

public class ClaimRequestDTO {
    private Long usuarioId;
    private BigDecimal valor;
	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

    // getters e setters
}
