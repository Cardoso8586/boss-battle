package com.boss_battle.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;



@Controller
public class SaldoController {

    @Autowired
    private UsuarioBossBattleRepository usuarioBossBattleRepository;

    @GetMapping("/api/saldoBossCoin")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoBossCoin(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // userDetails.username é o email

        UsuarioBossBattle usuario = usuarioBossBattleRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return Map.of("saldoBossCoin", usuario.getBossCoins());
    }

    
  /**
    @GetMapping("/api/saldoUsdt")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoUsdt(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return Map.of("saldoUsdt", usuario.getUsdtSaldo());
    }


    @GetMapping("/api/saldoDgb")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoDgb(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Garantir que nunca retorne null
        BigDecimal saldoDgb = usuario.getDgbSaldo() != null ? usuario.getDgbSaldo() : BigDecimal.ZERO;

        return Map.of("saldoDgb", saldoDgb);
    }

    @GetMapping("/api/saldoDoge")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoDoge(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Garantir que nunca retorne null
        BigDecimal saldoDoge = usuario.getDogeSaldo() != null ? usuario.getDogeSaldo() : BigDecimal.ZERO;

        return Map.of("saldoDoge", saldoDoge);
    }

    @GetMapping("/api/saldoTrx")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoTrx(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Garantir que nunca retorne null
        BigDecimal saldoTrx = usuario.getTrxSaldo() != null ? usuario.getTrxSaldo() : BigDecimal.ZERO;

        return Map.of("saldoTrx", saldoTrx);
    } 
  
    
    @GetMapping("/api/saldoLtc")
    @ResponseBody
    public Map<String, BigDecimal> getSaldoLtc(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Garantir que nunca retorne null
        BigDecimal saldoLtc = usuario.getLtcSaldo() != null ? usuario.getLtcSaldo() : BigDecimal.ZERO;

        return Map.of("saldoLtc", saldoLtc);
    } 
    */
}
