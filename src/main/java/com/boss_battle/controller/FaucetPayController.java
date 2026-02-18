package com.boss_battle.controller;


import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.model.BossBattleTransactionHistory;
import com.boss_battle.repository.BossBattleTransactionHistoryRepository;
import com.boss_battle.service.reconpensas_faucet_pay.FaucetPayService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faucetpay")
public class FaucetPayController {

    private final FaucetPayService faucetPayService;
    private final BossBattleTransactionHistoryRepository historyRepository;

    public FaucetPayController(
            FaucetPayService faucetPayService,
            BossBattleTransactionHistoryRepository historyRepository
    ) {
        this.faucetPayService = faucetPayService;
        this.historyRepository = historyRepository;
    }

    // ---------------- SALDO ----------------
    @GetMapping("/saldo")
    public ResponseEntity<?> getSaldo(@RequestParam String moeda) {
        try {
            return ResponseEntity.ok(faucetPayService.getBalance(moeda));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("success", false, "message", e.getMessage())
            );
        }
    }

    // ---------------- RETIRADA ----------------
    @PostMapping("/retirada")
    public ResponseEntity<Map<String, Object>> realizarRetirada(
            @RequestBody RetiradaRequest request
    ) {

        if (request.getUserId() == null ||
            request.getMoeda() == null ||
            request.geBossCoin() == null ||
            request.getEmail() == null) {

            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Dados incompletos")
            );
        }

        try {
            String respostaApi = faucetPayService.sendFunds(
                    request.getUserId(),
                    request.getMoeda(),
                    request.geBossCoin(), // ✅ BossCoin real
                    request.getEmail(),
                    request.getNota()      // ✅ pode ser null
            );

            boolean sucesso = respostaApi.contains("\"status\":200");

            String mensagem;

            if (sucesso) {
                mensagem = "✅ Retirada enviada com sucesso!";
            } else if (respostaApi.contains("\"status\":402")) {
                mensagem = "❌ Saldo insuficiente no momento. Tente novamente mais tarde.";
            } else if (respostaApi.contains("\"status\":401")) {
                mensagem = "❌ Falha de autenticação. Verifique suas credenciais.";
            } else {
                mensagem = "❌ Ocorreu um erro ao processar a retirada. Tente novamente.";
            }

            return ResponseEntity.ok(
                    Map.of(
                            "success", sucesso,
                            "message", mensagem
                    )
            );


        } catch (IOException | ParseException e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("success", false, "message", "Erro de comunicação com FaucetPay")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", e.getMessage())
            );
        }
    }


    // ---------------- HISTÓRICO POR USUÁRIO ----------------
 // ---------------- HISTÓRICO POR USUÁRIO ----------------
    @GetMapping("/historico")
    public ResponseEntity<List<BossBattleTransactionHistory>> historico(@RequestParam Long userId) {
        return ResponseEntity.ok(historyRepository.findByUserId(userId));
    }


    // ---------------- HISTÓRICO GLOBAL ----------------
    @GetMapping("/todos")
    public ResponseEntity<List<BossBattleTransactionHistory>> historicoTodos() {
        List<BossBattleTransactionHistory> historico = historyRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(historico);
    }

    // ---------------- DTO ----------------
    public static class RetiradaRequest {

        private Long userId;
        private String moeda;
        // 👇 ESTE É O VALOR REAL DO SAQUE
        private BigDecimal bossCoin;
        private String valor;
        private String email;
        private String nota;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public BigDecimal geBossCoin() { return bossCoin; }
        public void setBossCoin(BigDecimal bossCoin) { this.bossCoin = bossCoin; }
        
        public String getMoeda() { return moeda; }
        public void setMoeda(String moeda) { this.moeda = moeda; }

        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNota() { return nota; }
        public void setNota(String nota) { this.nota = nota; }
    }
}
