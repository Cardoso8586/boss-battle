package com.boss_battle.enums.enumscacador_reconpensas;
public enum InimigoMasmorra {

    GOBLIN(
            "Goblin das Sombras",
            2, 4,
            3, 6,
            "/images/cacador-recompensas/masmorra/goblin-das-sombras.webp"
    ),

    ESQUELETO(
            "Esqueleto Antigo",
            5, 8,
            6, 10,
            "/images/cacador-recompensas/masmorra/esqueleto-antigo.webp"
    ),

    GUARDIAO(
            "Guardião Sombrio",
            10, 15,
            10, 18,
            "/images/cacador-recompensas/masmorra/guardiao-sombrio.webp"
    ),

    CHEFE_OCULTO(
            "Chefe Oculto",
            20, 40,
            25, 50,
            "/images/cacador-recompensas/masmorra/chefe-oculto.webp"
    ),
	
	  GNOMO_X(
	            "Gnomo-X",
	            30, 50,
	            25, 50,
	            "/images/cacador-recompensas/masmorra/gnomo-x.webp"
	    ),
	
	  GOBLIN_Z(
	            "Goblin-Z",
	            30, 50,
	            25, 50,
	            "/images/cacador-recompensas/masmorra/goblin-z.webp"
	    ),
	

	  GUARDIAO_MONTANHA(
	            "Guardião da Montanha",
	            30, 70,
	            25, 75,
	            "/images/cacador-recompensas/masmorra/guardiao-montanha.webp"
	    ),
	
	
	  GARDIAO_CORRENTES(
	            "O Guardião de Correntes",
	            30, 90,
	            25, 95,
	            "/images/cacador-recompensas/masmorra/guardiao-de-correntes.webp"
	    ),

	  SUSSURRADOR_DAS_SOMBRAS(
	            "O Sussurrador das Sombras",
	            30, 90,
	            25, 95,
	            "/images/cacador-recompensas/masmorra/sussurrador-das-sombras.webp"
	    );

	
	
	
    private final String nome;
    private final int hpMultiplicadorMin;
    private final int hpMultiplicadorMax;
    private final int ataqueMin;
    private final int ataqueMax;
    private final String imagem;

    InimigoMasmorra(
            String nome,
            int hpMultiplicadorMin,
            int hpMultiplicadorMax,
            int ataqueMin,
            int ataqueMax,
            String imagem
    ) {
        this.nome = nome;
        this.hpMultiplicadorMin = hpMultiplicadorMin;
        this.hpMultiplicadorMax = hpMultiplicadorMax;
        this.ataqueMin = ataqueMin;
        this.ataqueMax = ataqueMax;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public int getHpMultiplicadorMin() {
        return hpMultiplicadorMin;
    }

    public int getHpMultiplicadorMax() {
        return hpMultiplicadorMax;
    }

    public int getAtaqueMin() {
        return ataqueMin;
    }

    public int getAtaqueMax() {
        return ataqueMax;
    }

    public String getImagem() {
        return imagem;
    }
}