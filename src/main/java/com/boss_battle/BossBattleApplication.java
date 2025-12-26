package com.boss_battle;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
@EnableScheduling
public class BossBattleApplication {
    public static void main(String[] args) {
        SpringApplication.run(BossBattleApplication.class, args);
        
        

        // ✅ Tenta carregar o .env apenas se existir (modo local)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // ✅ Carrega variáveis do .env como propriedades do sistema (para local)
        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) { 
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });

        // ✅ Agora o Spring pode ler tanto do ambiente (servidor) quanto do .env (local)
        
    }
    
    
    
}
