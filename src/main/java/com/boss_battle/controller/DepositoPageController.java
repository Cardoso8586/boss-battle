package com.boss_battle.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.NowPaymentsService;
import com.boss_battle.service.UltimoValorRecebidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/depositos")
public class DepositoPageController {
	
	@Autowired
	private UltimoValorRecebidoService ultimoValorRecebidoService;
	
	
	@Autowired
	private DepositoBossCoinsRepository depositoRepository;
	
	@Autowired
	private NowPaymentsService nowPaymentsService;
	   
    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @GetMapping
    public String abrirDepositos(HttpSession session,
                                 Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioSessao.getId())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        session.setAttribute("usuarioLogado", usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        // =========================================
        // HISTÓRICO
        // =========================================

        List<DepositoBossCoins> historico =
                depositoRepository
                        .findByUsuarioIdOrderByCriadoEmDesc(
                                usuario.getId()
                        );

        model.addAttribute("historico", historico);

        return "depositos";
    }
    
    //=========
 

    @PostMapping("/criar")
    public String criarDeposito(@RequestParam Long usuarioId,
                                @RequestParam BigDecimal valorUsd,
                                @RequestParam String moeda,
                                HttpSession session,
                                Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioSessao.getId())
                .orElseThrow();

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        var pagamento = nowPaymentsService.criarPagamento(
                usuarioId,
                valorUsd,
                moeda
        );

        model.addAttribute("pagamento", pagamento);

        return "depositos";
    }
    //================================
 /*
    @PostMapping("/nowpayments/ipn")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> receberIpn(
            @RequestBody String rawBody,
            @RequestHeader Map<String, String> headers) {

        try {

            System.out.println("=================================");
            System.out.println("IPN NOWPAYMENTS RECEBIDO");
            System.out.println("HEADERS:");
            headers.forEach((k,v) -> System.out.println(k + " = " + v));

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

                return ResponseEntity.ok("IGNORED");
            }

            Optional<DepositoBossCoins> depositoOpt =
                    depositoRepository.findByPaymentId(paymentId);

            if (depositoOpt.isEmpty()) {

                System.out.println("DEPÓSITO NÃO ENCONTRADO");

                return ResponseEntity.ok("NOT_FOUND");
            }

            DepositoBossCoins deposito = depositoOpt.get();

            deposito.setStatus(status);
            deposito.setAtualizadoEm(LocalDateTime.now());

            boolean statusCreditavel =
                    "finished".equalsIgnoreCase(status)
                 || "partially_paid".equalsIgnoreCase(status);

            if (statusCreditavel && !deposito.isCreditado()) {

                UsuarioBossBattle usuario =
                        usuarioRepository
                                .findById(deposito.getUsuarioId())
                                .orElseThrow();

                BigDecimal saldoAtual =
                        usuario.getBossCoins() == null
                        ? BigDecimal.ZERO
                        : usuario.getBossCoins();

                BigDecimal valorUsdParaCreditar =
                        deposito.getValorUsd();

                if ("partially_paid".equalsIgnoreCase(status)) {

                    Object valorPagoFiat =
                            body.get("actually_paid_fiat");

                    if (valorPagoFiat != null) {

                        valorUsdParaCreditar =
                                new BigDecimal(
                                        String.valueOf(valorPagoFiat)
                                );
                    }
                }

                BigDecimal bossCoinsRecebidas =
                        valorUsdParaCreditar.multiply(
                                BigDecimal.valueOf(10_000_000)
                        );

                usuario.setBossCoins(
                        saldoAtual.add(bossCoinsRecebidas)
                );

                deposito.setCreditado(true);

                ultimoValorRecebidoService
                        .setUltimoValorRecebido(
                                usuario,
                                bossCoinsRecebidas
                        );

                usuarioRepository.save(usuario);

                System.out.println("USUÁRIO CREDITADO");
            }

            depositoRepository.save(deposito);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.ok("ERROR");
        }
    }
  */
}