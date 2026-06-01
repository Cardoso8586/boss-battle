package com.boss_battle.controller;

import com.boss_battle.service.ZerAdsBossService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class ZerAdsBossController {

    private static final String PASSWORD = "BossArena2026_ZER_Secure";
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
        String ip = getClientIp(request);

        System.out.println("===== CALLBACK ZERADS =====");
        System.out.println("USER: " + user);
        System.out.println("IP: " + ip);

        if (!PASSWORD.equals(pwd)) {
            return ResponseEntity.status(403).body("Senha inválida");
        }

        if (!ZERADS_IP.equals(ip)) {
            return ResponseEntity.status(403).body("IP inválido");
        }

        BigDecimal recompensa = zerAdsBossService.creditarRecompensa(user, amount, clicks);

        return ResponseEntity.ok("OK +" + recompensa + " Boss Coins");
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}