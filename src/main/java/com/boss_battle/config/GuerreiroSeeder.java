package com.boss_battle.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.boss_battle.model.Guerreiro;
import com.boss_battle.repository.GuerreiroRepository;

@Component
public class GuerreiroSeeder implements CommandLineRunner {

    private final GuerreiroRepository guerreiroRepository;

    public GuerreiroSeeder(GuerreiroRepository guerreiroRepository) {
        this.guerreiroRepository = guerreiroRepository;
    }

    @Override
    public void run(String... args) {
    	// ID 1
    	criarSeNaoExistir(
    	        "Valgard",
    	        "GUERREIRO",
    	        8L,
    	        5_000L,
    	        "valgard.webp",
    	        true,
    	        100
    	);

    	// ID 2
    	criarSeNaoExistir(
    	        "Guerreira Mística",
    	        "GUERREIRA",
    	        8L,
    	        5_000L,
    	        "guerreira-mistica.webp",
    	        false,
    	        100
    	);
    	
    	// ID 3
    	criarSeNaoExistir(
    	        "Caçador de Boss",
    	        "GUERREIRO",
    	        9L,
    	        7_000L,
    	        "cacador-boss.webp",
    	        false,
    	        40
    	);
    	
    	
    	// ID 4
    	criarSeNaoExistir(
    	        "Arqueira Real",
    	        "ARQUEIRA",
    	        7L,
    	        6_000L,
    	        "arqueira-real.webp",
    	        false,
    	        70
    	);
    	

    	// ID 5
    	criarSeNaoExistir(
    	        "Guerreiro Guardião",
    	        "GUERREIRO",
    	        7L,
    	        6_000L,
    	        "guerreiro-guardiao.webp",
    	        false,
    	        70
    	);
    	
    	// ID 6
    	criarSeNaoExistir(
    	        "Sentinela Nyara",
    	        "GUERREIRA",
    	        10L,
    	        30_000L,
    	        "sentinela-nyara.webp",
    	        false,
    	        50
    	);
    	
    }

    private void criarSeNaoExistir(
            String nome,
            String tipo,
            Long danoBase,
            Long custoCompra,
            String imagem,
            boolean padrao,
            Integer quantidadeMaxima
    ) {
        if (guerreiroRepository.existsByNome(nome)) {
            return;
        }

        Guerreiro guerreiro = new Guerreiro();
        guerreiro.setNome(nome);
        guerreiro.setTipo(tipo);
        guerreiro.setDanoBase(danoBase);
        guerreiro.setCustoCompra(custoCompra);
        guerreiro.setImagem(imagem);
        guerreiro.setPadrao(padrao);
        guerreiro.setAtivo(true);
        guerreiro.setQuantidadeMaxima(quantidadeMaxima);

        guerreiroRepository.save(guerreiro);
    }
}



/**


criarSeNaoExistir(
"Guerreiro Raro",
"RARO",
15L,
500L,
"guerreiro-raro.webp",
false
);

criarSeNaoExistir(
"Guerreiro Épico",
"EPICO",
50L,
2500L,
"guerreiro-epico.webp",
false
);

criarSeNaoExistir(
"Guerreiro Lendário",
"LENDARIO",
150L,
10000L,
"guerreiro-lendario.webp",
false
);

criarSeNaoExistir(
"Guerreira Arqueira",
"GUERREIRA",
30L,
2000L,
"guerreira-arqueira.webp",
false
);
*/