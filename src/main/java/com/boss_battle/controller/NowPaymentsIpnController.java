package com.boss_battle.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.UltimoValorRecebidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/nowpayments")
public class NowPaymentsIpnController {

    @Autowired
    private DepositoBossCoinsRepository depositoRepository;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Autowired
    private UltimoValorRecebidoService ultimoValorRecebidoService;

    @PostMapping("/ipn")
    @Transactional
    public ResponseEntity<String> receberIpn(
            @RequestBody String rawBody,
            @RequestHeader Map<String, String> headers) {

        try {

            System.out.println("=================================");
            System.out.println("IPN NOWPAYMENTS RECEBIDO");
            System.out.println("HEADERS:");

            headers.forEach((k, v) ->
                    System.out.println(k + " = " + v));

            System.out.println("BODY:");
            System.out.println(rawBody);
            System.out.println("=================================");

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> body =
                    mapper.readValue(rawBody, Map.class);

            String paymentId =
                    String.valueOf(body.get("payment_id"));

            String status =
                    String.valueOf(body.get("payment_status"));

            if (paymentId == null || paymentId.equals("null")) {

                System.out.println("PAYMENT ID NULL");

                return ResponseEntity.ok("NO_PAYMENT_ID");
            }

            DepositoBossCoins deposito =
                    depositoRepository
                            .findByPaymentId(paymentId)
                            .orElse(null);

            if (deposito == null) {

                System.out.println("DEPÓSITO NÃO ENCONTRADO");

                return ResponseEntity.ok("DEPOSITO_NOT_FOUND");
            }

            deposito.setStatus(status);
            deposito.setAtualizadoEm(LocalDateTime.now());

            boolean statusCreditavel =
                    "finished".equalsIgnoreCase(status)
                    || "partially_paid".equalsIgnoreCase(status);

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

                BigDecimal valorUsdParaCreditar =
                        deposito.getValorUsd();

                Object valorPagoFiat =
                        body.get("actually_paid_fiat");

                if (valorPagoFiat != null) {

                    valorUsdParaCreditar =
                            new BigDecimal(
                                    String.valueOf(valorPagoFiat)
                            );
                }

                BigDecimal bossCoinsRecebidas =
                        valorUsdParaCreditar.multiply(
                                BigDecimal.valueOf(10_000_000)
                        );

                usuario.setBossCoins(
                        saldoAtual.add(bossCoinsRecebidas)
                );

                deposito.setCreditado(true);

                usuarioRepository.save(usuario);

                ultimoValorRecebidoService
                        .setUltimoValorRecebido(
                                usuario,
                                bossCoinsRecebidas
                        );

                System.out.println("=================================");
                System.out.println("SALDO CREDITADO");
                System.out.println("USUARIO ID: " + usuario.getId());
                System.out.println("STATUS: " + status);
                System.out.println("VALOR USD: " + valorUsdParaCreditar);
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

    @GetMapping("/teste")
    public String teste() {

        System.out.println("TESTE NOWPAYMENTS ONLINE");

        return "NOWPAYMENTS ONLINE";
    }
}