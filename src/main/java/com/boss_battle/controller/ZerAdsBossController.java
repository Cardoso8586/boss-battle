package com.boss_battle.controller;

import com.boss_battle.service.ZerAdsBossService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ZerAdsBossController {

	private static final String PASSWORD =
	        "BossArena2026_ZER_Secure";
    private static final String ZERADS_IP = "162.0.208.108";

    private final ZerAdsBossService zerAdsBossService;

    public ZerAdsBossController(ZerAdsBossService zerAdsBossService) {
        this.zerAdsBossService = zerAdsBossService;
    }

    @GetMapping("/zeradsptc")
    public ResponseEntity<String> callbackZerAds(
            @RequestParam String pwd,
            @RequestParam String user,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "0") Integer clicks,
            HttpServletRequest request
    ) {
        System.out.println("===== CALLBACK ZERADS =====");
        System.out.println("PWD: " + pwd);
        System.out.println("USER: " + user);
        System.out.println("AMOUNT: " + amount);
        System.out.println("CLICKS: " + clicks);
        System.out.println("IP: " + request.getRemoteAddr());

        if (!PASSWORD.equals(pwd)) {
            return ResponseEntity.status(403).body("Senha inválida");
        }

        // deixe comentado só para teste
        // if (!ZERADS_IP.equals(request.getRemoteAddr())) {
        //     return ResponseEntity.status(403).body("IP inválido: " + request.getRemoteAddr());
        // }

        BigDecimal recompensa = zerAdsBossService.creditarRecompensa(user, amount, clicks);

        return ResponseEntity.ok("OK +" + recompensa + " Boss Coins");
    }
}