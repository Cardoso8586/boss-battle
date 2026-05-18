package com.boss_battle.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.NowPaymentsService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/depositos")
public class DepositoPageController {
	
	
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
    
    @PostMapping("/nowpayments/ipn")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> receberIpn(@RequestBody Map<String, Object> body) {

    	
    	    System.out.println("=================================");
    	    System.out.println("IPN RECEBIDO NOWPAYMENTS");
    	    System.out.println(body);
    	    System.out.println("=================================");
    	
        String paymentId = String.valueOf(body.get("payment_id"));
        String status = String.valueOf(body.get("payment_status"));

        DepositoBossCoins deposito = depositoRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Depósito não encontrado"));

        deposito.setStatus(status);
        deposito.setAtualizadoEm(LocalDateTime.now());

        if ("finished".equalsIgnoreCase(status) && !deposito.isCreditado()) {

            UsuarioBossBattle usuario = usuarioRepository.findById(deposito.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            BigDecimal saldoAtual = usuario.getBossCoins();

            if (saldoAtual == null) {
                saldoAtual = BigDecimal.ZERO;
            }

            BigDecimal bossCoinsRecebidas = deposito.getValorUsd()
                    .multiply(BigDecimal.valueOf(10_000_000));

            usuario.setBossCoins(saldoAtual.add(bossCoinsRecebidas));

            deposito.setCreditado(true);

            usuarioRepository.save(usuario);
        }

        depositoRepository.save(deposito);

        return ResponseEntity.ok("OK");
    }
    
   
    
}