
package com.boss_battle.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
public class RetaguardaService {

    @Autowired
    private UsuarioBossBattleRepository repo;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processarRetaguardaUsuario(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow();

        long energiaMaxima = usuario.getEnergiaGuerreirosPadrao();
        long energiaAtual = usuario.getEnergiaGuerreiros();

        Long guerreiros = usuario.getGuerreirosRetaguarda();
        Long recuperacao = usuario.getRecuperacaoRetaguarda();

        if (guerreiros == null || recuperacao == null) return;
        if (guerreiros <= 0 || recuperacao <= 0) return;

        long totalRecuperacao = guerreiros * recuperacao;

        long novaEnergia = Math.min(
            energiaAtual + totalRecuperacao,
            energiaMaxima
        );

        usuario.setEnergiaGuerreiros(novaEnergia);
        repo.save(usuario);
    }
    @Scheduled(fixedRate = 80000)
    public void processarRetaguarda() {

        List<Long> ids = repo.findIdsAtivos(); // ideal
      
        for (Long id : ids) {
            try {
                processarRetaguardaUsuario(id);
            } catch (Exception e) {
                // log e segue
            }
        }
    }

    /**
    // Executa a cada 1 minuto
    @Scheduled(fixedRate = 80000)
    @Transactional
    public void processarRetaguarda() {

        List<UsuarioBossBattle> usuarios = repo.findAll();

        for (UsuarioBossBattle usuario : usuarios) {

            long energiaMaxima = usuario.getEnergiaGuerreirosPadrao();
            long energiaAtual = usuario.getEnergiaGuerreiros();

            Long guerreiros = usuario.getGuerreirosRetaguarda();
            Long recuperacao = usuario.getRecuperacaoRetaguarda();

            if (guerreiros == null || recuperacao == null) continue;
            if (guerreiros <= 0 || recuperacao <= 0) continue;

            long totalRecuperacao = guerreiros * recuperacao;

            // 🔒 trava no máximo
            long novaEnergia = energiaAtual + totalRecuperacao;
            if (novaEnergia > energiaMaxima) {
                novaEnergia = energiaMaxima;
            }

            usuario.setEnergiaGuerreiros(novaEnergia);
        }

        repo.saveAll(usuarios);
    }
*/
    
 // ===============================
    // CONSULTAR REPARO (RETORNO)
    // ===============================
    @Transactional
    public Map<String, Number> consultarReparo(Long usuarioId) {
        UsuarioBossBattle usuario = repo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        long energiaAtual = usuario.getEnergiaGuerreiros();
        long energiaMaxima = usuario.getEnergiaGuerreirosPadrao();

        long guerreiros = usuario.getGuerreirosRetaguarda();
        long recuperacao = usuario.getRecuperacaoRetaguarda();

        if (guerreiros <= 0 || recuperacao <= 0) {
            return Map.of("reparoEfetivo", 0);
        }

        long totalReparo = guerreiros * recuperacao;

        // Valor que realmente vai ser aplicado sem passar do máximo
        long reparoEfetivo = Math.min(totalReparo, energiaMaxima - energiaAtual);

        return Map.of(
            "reparoEfetivo", reparoEfetivo,
            "energiaAtual", energiaAtual,
            "energiaMaxima",energiaMaxima
          
        );
    }
    
    @Transactional
    public long processarAtaqueRetaguarda(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        long guerreiros = usuario.getGuerreirosRetaguarda();
        long recuperacao = usuario.getRecuperacaoRetaguarda(); // taxa base
        long vigor = usuario.getEnergiaGuerreiros();
       

        if (guerreiros <= 0 || recuperacao <= 0 || vigor <= 0) {
            return 0;
        }

        // Retorno decrescente simples
        double eficiencia;
        if (guerreiros == 1) eficiencia = 1.0;
        else if (guerreiros == 2) eficiencia = 0.7;
        else eficiencia = 0.4;

        long ataqueEfetivo = Math.round(guerreiros * recuperacao * eficiencia);

        /*
        // Consumo de vigor proporcional
        long custoVigor = guerreiros * 2;

        if (vigor < custoVigor) {
            ataqueEfetivo = Math.round(ataqueEfetivo * ((double) vigor / custoVigor));
            custoVigor = vigor;
        }

        usuario.setEnergiaGuerreiros(vigor - custoVigor);
       */


        repo.save(usuario);

        return ataqueEfetivo;
    }


}

