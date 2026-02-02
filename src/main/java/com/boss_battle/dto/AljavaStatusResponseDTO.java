package com.boss_battle.dto;

import java.util.Map;

import com.boss_battle.enums.TipoFlecha;

public class AljavaStatusResponseDTO {

    private Long usuarioId;
    private long aljava;
    private TipoFlecha tipoFlecha;
    private Map<String, Long> flechasRestantesInventario;

    public AljavaStatusResponseDTO(
            Long usuarioId,
            long aljava,
            TipoFlecha tipoFlecha,
            Map<String, Long> flechasRestantesInventario
    ) {
        this.usuarioId = usuarioId;
        this.aljava = aljava;
        this.tipoFlecha = tipoFlecha;
        this.flechasRestantesInventario = flechasRestantesInventario;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public long getAljava() {
        return aljava;
    }

    public TipoFlecha getTipoFlecha() {
        return tipoFlecha;
    }

    public Map<String, Long> getFlechasRestantesInventario() {
        return flechasRestantesInventario;
    }
}
