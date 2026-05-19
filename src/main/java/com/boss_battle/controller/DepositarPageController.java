package com.boss_battle.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.now_payments.NowPaymentsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/depositar")
public class DepositarPageController {

    @Autowired
    private DepositoBossCoinsRepository depositoRepository;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Autowired
    private NowPaymentsService nowPaymentsService;

    @GetMapping
    public String abrirDepositar(HttpSession session,
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

        List<DepositoBossCoins> historico =
                depositoRepository
                        .findByUsuarioIdOrderByCriadoEmDesc(
                                usuario.getId()
                        );

        model.addAttribute("historico", historico);

        return "depositar";
    }

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

        List<DepositoBossCoins> historico =
                depositoRepository
                        .findByUsuarioIdOrderByCriadoEmDesc(
                                usuario.getId()
                        );

        model.addAttribute("historico", historico);

        return "depositar";
    }
}