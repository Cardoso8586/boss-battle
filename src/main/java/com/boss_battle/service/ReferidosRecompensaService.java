package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;



@Service
@Transactional
public class ReferidosRecompensaService {
	

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;
	
	

    public void adicionarGanho(UsuarioBossBattle usuario, BigDecimal valor) {
      
    	
       
        usuarioRepository.save(usuario);

        // 2️⃣ Dá 10% ao indicante
        if (usuario.getReferredBy() != null) {
            Long referrerId = usuario.getReferredBy();
            usuarioRepository.findById(referrerId).ifPresent(referrer -> {
                if (referrer.getGanhosPendentesReferral() == null) {
                    referrer.setGanhosPendentesReferral(BigDecimal.ZERO);
                }

                // ✅ Corrigido: calcula apenas 10% do valor da ação
                BigDecimal bonus = valor.multiply(new BigDecimal("0.10"));

                referrer.setGanhosPendentesReferral(
                    referrer.getGanhosPendentesReferral().add(bonus)
                );

           
                
                usuarioRepository.save(referrer);
                System.out.println("[Ganho] Usuário " + referrer.getUsername()
                        + " ganhou 10% de " + usuario.getUsername() + ": " + bonus);
            });
        }
    }
    
	
	  // =========================================  claimGanhos ===============================================================

 
    public void claimGanhos(UsuarioBossBattle usuario) {
        BigDecimal totalClaim = BigDecimal.ZERO;

      

        if (usuario.getGanhosPendentesReferral() != null) {
            totalClaim = totalClaim.add(usuario.getGanhosPendentesReferral());
            usuario.setGanhosPendentesReferral(BigDecimal.ZERO);
        }

        usuario.setBossCoins(usuario.getBossCoins().add(totalClaim));
        usuario.setGanhosPendentesReferral(BigDecimal.ZERO);
        usuarioRepository.save(usuario);
    }

}
