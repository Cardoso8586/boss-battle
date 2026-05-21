package com.boss_battle.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.boss_battle.service.UltimoValorRecebidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/depositos")
public class NowPaymentsIpnController {
	
	@Autowired
	private UltimoValorRecebidoService ultimoValorRecebidoService;
	
	
	@Autowired
	private DepositoBossCoinsRepository depositoRepository;
	

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

 
    @PostMapping("/nowpayments/ipn")
    @Transactional
    public ResponseEntity<String> receberIpn(
            @RequestBody String rawBody,
            @RequestHeader Map<String, String> headers) {

        try {
            System.out.println("=================================");
            System.out.println("IPN NOWPAYMENTS RECEBIDO");

            headers.forEach((k, v) ->
                    System.out.println(k + " = " + v));

            System.out.println("BODY:");
            System.out.println(rawBody);
            System.out.println("=================================");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> body = mapper.readValue(rawBody, Map.class);

            String paymentId = String.valueOf(body.get("payment_id"));
            String status = String.valueOf(body.get("payment_status"));

            Object orderIdObj = body.get("order_id");
            String orderId = orderIdObj == null ? null : String.valueOf(orderIdObj);

            System.out.println("PAYMENT_ID RECEBIDO: [" + paymentId + "]");
            System.out.println("ORDER_ID RECEBIDO: [" + orderId + "]");
            System.out.println("STATUS RECEBIDO: [" + status + "]");

            if (paymentId == null || paymentId.equals("null")) {
                return ResponseEntity.ok("NO_PAYMENT_ID");
            }

            DepositoBossCoins deposito =
                    depositoRepository
                            .findByPaymentId(paymentId)
                            .orElse(null);

            if (deposito == null && orderId != null) {
                deposito =
                        depositoRepository
                                .findByOrderId(orderId)
                                .orElse(null);
            }

            if (deposito == null) {
                System.out.println("DEPÓSITO NÃO ENCONTRADO");
                return ResponseEntity.ok("DEPOSITO_NOT_FOUND");
            }

            deposito.setStatus(status);
            deposito.setAtualizadoEm(LocalDateTime.now());

            boolean statusCreditavel =
                    "finished".equalsIgnoreCase(status)
                    || "partially_paid".equalsIgnoreCase(status)
                    || "sending".equalsIgnoreCase(status);

            if (statusCreditavel && !deposito.isCreditado()) {

                UsuarioBossBattle usuario =
                        usuarioRepository
                                .findById(deposito.getUsuarioId())
                                .orElseThrow(() ->
                                        new RuntimeException("Usuário não encontrado"));

                BigDecimal saldoAtual = usuario.getBossCoins();

                if (saldoAtual == null) {
                    saldoAtual = BigDecimal.ZERO;
                }

                // =========================================
                // VALOR REAL PAGO
                // =========================================

                BigDecimal valorUsdParaCreditar =
                        deposito.getValorUsd();

                Object valorPagoFiat =
                        body.get("actually_paid_fiat");

                if (valorPagoFiat != null
                        && !String.valueOf(valorPagoFiat).equals("null")) {

                    valorUsdParaCreditar =
                            new BigDecimal(
                                    String.valueOf(valorPagoFiat)
                            );
                }

                // =========================================
                // BOSS COINS
                // =========================================

                BigDecimal bossCoinsRecebidas =
                        valorUsdParaCreditar.multiply(
                                BigDecimal.valueOf(10_000_000)
                        );

                
                
                usuario.setBossCoins(
                        saldoAtual.add(bossCoinsRecebidas)
                );

                 ultimoValorRecebidoService
                        .setUltimoValorRecebido(
                                usuario,
                                bossCoinsRecebidas
                        );
                 
                 
                deposito.setCreditado(true);
                
                  

                usuarioRepository.save(usuario);

             

                System.out.println("=================================");
                System.out.println("SALDO CREDITADO");
                System.out.println("USUARIO ID: " + usuario.getId());
                System.out.println("STATUS: " + status);
                System.out.println("VALOR USD CREDITADO: " + valorUsdParaCreditar);
                System.out.println("BOSS COINS: " + bossCoinsRecebidas);
                System.out.println("=================================");
            }
            depositoRepository.save(deposito);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("ERROR");
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