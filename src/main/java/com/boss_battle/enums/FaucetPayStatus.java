package com.boss_battle.enums;

public enum FaucetPayStatus {

    APROVADO("APROVADO"),
    REJEITADO_ADMINISTRADOR("REJEITADO PELO ADMINISTRADOR"),
    NAO_AUTORIZADO("NÃO AUTORIZADO"),
    ERRO_DESCONHECIDO("ERRO INESPERADO");

    private final String mensagemUsuario;

    FaucetPayStatus(String mensagemUsuario) {
        this.mensagemUsuario = mensagemUsuario;
    }

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }
}
