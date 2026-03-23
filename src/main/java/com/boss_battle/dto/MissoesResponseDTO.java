package com.boss_battle.dto;

import java.util.List;

public class MissoesResponseDTO {

    private List<UsuarioMissaoDTO> diarias;
    private List<UsuarioMissaoDTO> semanais;
    private List<UsuarioMissaoDTO> surpresa;

    public List<UsuarioMissaoDTO> getDiarias() {
        return diarias;
    }

    public void setDiarias(List<UsuarioMissaoDTO> diarias) {
        this.diarias = diarias;
    }

    public List<UsuarioMissaoDTO> getSemanais() {
        return semanais;
    }

    public void setSemanais(List<UsuarioMissaoDTO> semanais) {
        this.semanais = semanais;
    }

    public List<UsuarioMissaoDTO> getSurpresa() {
        return surpresa;
    }

    public void setSurpresa(List<UsuarioMissaoDTO> surpresa) {
        this.surpresa = surpresa;
    }
    
    
    
}