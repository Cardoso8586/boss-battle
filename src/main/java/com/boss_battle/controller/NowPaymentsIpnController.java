package com.boss_battle.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/depositos")
public class NowPaymentsIpnController {
	
	@Autowired
	private DepositoBossCoinsRepository depositoRepository;
	

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Value("${nowpayments.ipn-secret}")
    private String ipnSecret;

    @PostMapping("/nowpayments/ipn")
    @Transactional
    public ResponseEntity<String> receberIpn(
            @RequestBody String rawBody,
            @RequestHeader Map<String, String> headers) {

        try {
            String assinatura = headers.get("x-nowpayments-sig");

            if (assinatura == null || assinatura.isBlank()) {
                return ResponseEntity.status(401).body("NO_SIGNATURE");
            }

            if (!assinaturaValida(rawBody, assinatura)) {
                return ResponseEntity.status(401).body("INVALID_SIGNATURE");
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> body = mapper.readValue(rawBody, Map.class);

            String paymentId = String.valueOf(body.get("payment_id"));
            String status = String.valueOf(body.get("payment_status"));

            if (paymentId == null || paymentId.equals("null")) {
                return ResponseEntity.ok("NO_PAYMENT_ID");
            }

            if (!"finished".equalsIgnoreCase(status)) {
                return ResponseEntity.ok("STATUS_IGNORADO");
            }

            DepositoBossCoins deposito = depositoRepository
                    .findByPaymentIdForUpdate(paymentId)
                    .orElse(null);

            if (deposito == null) {
                return ResponseEntity.ok("DEPOSITO_NOT_FOUND");
            }

            if (deposito.isCreditado()) {
                return ResponseEntity.ok("JA_CREDITADO");
            }

            UsuarioBossBattle usuario = usuarioRepository
                    .findByIdForUpdate(deposito.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            BigDecimal saldoAtual = usuario.getBossCoins();

            if (saldoAtual == null) {
                saldoAtual = BigDecimal.ZERO;
            }

            BigDecimal bossCoinsRecebidas =
                    deposito.getValorUsd().multiply(BigDecimal.valueOf(10_000_000));

            usuario.setBossCoins(saldoAtual.add(bossCoinsRecebidas));
            usuario.setUltimoValorRecebido(bossCoinsRecebidas);

            deposito.setStatus(status);
            deposito.setAtualizadoEm(LocalDateTime.now());
            deposito.setCreditado(true);

            usuarioRepository.saveAndFlush(usuario);
            depositoRepository.saveAndFlush(deposito);

            return ResponseEntity.ok("CREDITADO");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("ERROR");
        }
    }
    
    private boolean assinaturaValida(String rawBody, String assinaturaRecebida) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey =
                    new SecretKeySpec(ipnSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            mac.init(secretKey);

            byte[] hash = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));

            String assinaturaCalculada = HexFormat.of().formatHex(hash);

            return MessageDigest.isEqual(
                    assinaturaCalculada.getBytes(StandardCharsets.UTF_8),
                    assinaturaRecebida.getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception e) {
            return false;
        }
    }
    
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<Map<String, Object>> statusPagamento(@PathVariable String paymentId) {

        return depositoRepository.findByPaymentId(paymentId)
                .map(d -> {
                    Map<String, Object> resposta = new HashMap<>();

                    resposta.put("paymentId", d.getPaymentId());
                    resposta.put("status", d.getStatus());
                    resposta.put("creditado", d.isCreditado());

                    return ResponseEntity.ok(resposta);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
  
}