package com.boss_battle.enums.enumscacador_reconpensas;
public enum PremioRuinasPerdidas {

    BOSS_COINS_PEQUENO(
            "Moedinhas",
            "Você encontrou algumas moedas antigas nas ruínas.",
            "BOSS_COINS"
    ),

    BOSS_COINS_MEDIO(
            "Poucas Moedas",
            "Você encontrou um baú escondido com moedas.",
            "BOSS_COINS"
    ),

    BOSS_COINS_GRANDE(
            "Tesouro Antigo",
            "Você encontrou um tesouro escondido nas profundezas das ruínas.",
            "BOSS_COINS"
    ),

    XP_PEQUENO(
            "XP",
            "Você encontrou inscrições antigas e ganhou experiência.",
            "XP"
    ),

    XP_GRANDE(
            "XP Rara",
            "As ruínas revelaram um antigo conhecimento esquecido.",
            "XP"
    ),

    ATAQUE_ESPECIAL_PLUS_1(
            "Ataque Especial +1",
            "Uma energia antiga fortaleceu seu ataque especial.",
            "ATAQUE_ESPECIAL"
    ),

    NADA_ENCONTRADO(
            "Nada Encontrado",
            "Você explorou as ruínas, mas não encontrou nada dessa vez.",
            "NADA"
    );

    private final String nome;
    private final String descricao;
    private final String tipo;

    PremioRuinasPerdidas(String nome, String descricao, String tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }
}