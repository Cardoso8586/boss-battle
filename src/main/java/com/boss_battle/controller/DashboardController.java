package com.boss_battle.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;


import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
	 @Autowired
	    private UsuarioBossBattleRepository repo;
	 @GetMapping("/arena")
	 public String dashboard(HttpSession session, Model model) {

	     UsuarioBossBattle usuarioSessao =
	             (UsuarioBossBattle) session.getAttribute("usuarioLogado");

	     if (usuarioSessao == null) {
	         return "redirect:/login";
	     }

	     UsuarioBossBattle usuario =
	             repo.findById(usuarioSessao.getId())
	                     .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	     session.setAttribute("usuarioLogado", usuario);

	     DecimalFormat df = new DecimalFormat("#,##0");

	     model.addAttribute("usuario", usuario);
	     model.addAttribute("idUsuario", usuario.getId());

	     model.addAttribute("ataque_base", df.format(usuario.getAtaqueBase()));
	     model.addAttribute("xpUsuario", df.format(usuario.getExp()));
	     model.addAttribute("nivelUsuario", df.format(usuario.getNivel()));

	     model.addAttribute("guerreirosUsuario", df.format(usuario.getGuerreiros()));
	     model.addAttribute("guerreirosRetaguarda", df.format(usuario.getGuerreirosRetaguarda()));

	     BigDecimal ultimoGanho = usuario.getUltimoValorRecebido();
	     if (ultimoGanho == null) {
	         ultimoGanho = BigDecimal.ZERO;
	     }
	     model.addAttribute("ultimoGanho", df.format(ultimoGanho));

	     long guerreiroAtaque = usuario.getGuerreiros();
	     long guerreiroRetaguarda = usuario.getGuerreirosRetaguarda();
	     long guerreiroInventario = usuario.getGuerreirosInventario();

	     long quantidadeTotalGuerriro =
	             guerreiroAtaque + guerreiroRetaguarda + guerreiroInventario;

	     model.addAttribute("quantidadeTotalGuerriro", df.format(quantidadeTotalGuerriro));

	     long quantGuerreiros = usuario.getGuerreiros();
	     long quantAtaqueBaseGuerreiros = usuario.getAtaqueBaseGuerreiros();

	     long totalAtaquePorMinuto =
	             quantGuerreiros * quantAtaqueBaseGuerreiros;

	     model.addAttribute("ataquePorMinutoUsuario", df.format(totalAtaquePorMinuto));

	     Long energia = usuario.getEnergiaGuerreiros();
	     if (energia == null) {
	         energia = 0L;
	     }

	     Long energiaMaxima = usuario.getEnergiaGuerreirosPadrao();
	     if (energiaMaxima == null || energiaMaxima <= 0) {
	         energiaMaxima = 50L;
	     }

	     model.addAttribute("energiaGuerreiros", energia);
	     model.addAttribute("energiaMaxima", energiaMaxima);
	     model.addAttribute("energiaGuerreirosPadrao", energiaMaxima);

	     Long pocaoVigor = Optional
	             .ofNullable(usuario.getPocaoVigor())
	             .orElse(0L);

	     model.addAttribute("quantidadePocaoVigor", pocaoVigor);

	     model.addAttribute("quantidadeEspadaFlanejante",
	             df.format(usuario.getEspadaFlanejante()));

	     model.addAttribute("quantidadeMachadoDilacerador",
	             df.format(usuario.getMachadoDilacerador()));

	     model.addAttribute("quantidadeArcoCelestial",
	             df.format(usuario.getInventarioArco()));

	     model.addAttribute("quantidadeFlechaDiamante",
	             df.format(usuario.getFlechaDiamante()));

	     model.addAttribute("quantidadeFlechaFogo",
	             df.format(usuario.getFlechaFogo()));

	     model.addAttribute("quantidadeFlechaVeneno",
	             df.format(usuario.getFlechaVeneno()));

	     model.addAttribute("quantidadeFlechaFerro",
	             df.format(usuario.getFlechaFerro()));

	     String linkReferencia =
	             "https://boss-battle-arena.com/aliados?ref="
	                     + Base64.getUrlEncoder()
	                     .encodeToString(usuario.getId().toString().getBytes());

	     model.addAttribute("linkReferencia", linkReferencia);

	     List<UsuarioBossBattle> indicados = repo.findByReferredBy(usuario.getId());

	     model.addAttribute("indicados", indicados);
	     model.addAttribute("qtdIndicados", indicados.size());

	     BigDecimal ganhos = usuario.getGanhosPendentesReferral();

	     if (ganhos == null) {
	         ganhos = BigDecimal.ZERO;
	     }

	     ganhos = ganhos.setScale(1, RoundingMode.HALF_UP);

	     model.addAttribute("ganhosPendentesReferral", ganhos);

	     BigDecimal coins = usuario.getBossCoins();

	     if (coins == null) {
	         coins = BigDecimal.ZERO;
	     }

	     model.addAttribute("boss_coins", df.format(coins));

	     
	     
	    
	     
	     
	     return "arena";
	 }
}
