package com.boss_battle.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.UsuarioBossBattle;


@Service
@Transactional
public class UltimoValorRecebidoService {

	// @Autowired
	 //private UsuarioBossBattleRepository usuarioRepository;

	 public void setUltimoValorRecebido(UsuarioBossBattle usuario, BigDecimal valor) {
		    usuario.setUltimoValorRecebido(
		        valor != null ? valor : BigDecimal.ZERO
		    );
		    
		    System.out.println( usuario.getUsername() + " " + "Recebeu + :" + usuario.getUltimoValorRecebido() + "  Boss Coins");

		    //usuarioRepository.saveAndFlush(usuario);
		}
	 
	 
}//---> UltimoValorRecebidoService