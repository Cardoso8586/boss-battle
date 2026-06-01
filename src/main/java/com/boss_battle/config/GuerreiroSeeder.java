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
    	        5000L,
    	        "valgard.webp",
    	        true
    	);

    	// ID 2
    	criarSeNaoExistir(
    	        "Guerreira Mística",
    	        "GUERREIRA",
    	        8L,
    	        5000L,
    	        "guerreira-mistica.webp",
    	        false
    	);
    	
    	// ID 3
    	criarSeNaoExistir(
    	        "Caçador de Boss",
    	        "GUERREIRO",
    	        9L,
    	        7000L,
    	        "cacador-boss.webp",
    	        false
    	);
    	
    	
    	// ID 4
    	criarSeNaoExistir(
    	        "Arqueira Real",
    	        "ARQUEIRA",
    	        7L,
    	        6000L,
    	        "arqueira-real.webp",
    	        false
    	);
    	

    	// ID 5
    	criarSeNaoExistir(
    	        "Guerreiro Guardião",
    	        "GUERREIRO",
    	        7L,
    	        6000L,
    	        "guerreiro-guardiao.webp",
    	        false
    	);
    	
    }

    private void criarSeNaoExistir(
            String nome,
            String tipo,
            Long danoBase,
            Long custoCompra,
            String imagem,
            boolean padrao
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