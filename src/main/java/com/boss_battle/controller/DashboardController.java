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
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Pega o usuário logado da sessão
        UsuarioBossBattle usuario = (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            // Se não estiver logado, redireciona para login
            return "redirect:/login";
        }

       DecimalFormat df = new DecimalFormat("#,##0");
        
        // Adiciona o usuário ao Model do Thymeleaf
        model.addAttribute("usuario", usuario);
        
        model.addAttribute("idUsuario", usuario.getId());
        
        
     
        model.addAttribute("ataque_base", df.format(usuario.getAtaqueBase()) );
        
        model.addAttribute("xpUsuario", usuario.getExp());
        
        model.addAttribute("nivelUsuario", usuario.getNivel());
        
        model.addAttribute("guerreirosUsuario", usuario.getGuerreiros());
        
        model.addAttribute("guerreirosUsuario", df.format(usuario.getGuerreiros()));
        
        model.addAttribute("guerreirosRetaguarda", df.format(usuario.getGuerreirosRetaguarda()));
        
        long guerreiroAtaque = usuario.getGuerreiros();
        long guerreiroRetaguarda = usuario.getGuerreirosRetaguarda();
        long guerreiroInventario= usuario.getGuerreirosInventario();
        long quantidadeTotalGuerriro = guerreiroAtaque + guerreiroRetaguarda + guerreiroInventario;
        
        model.addAttribute("quantidadeTotalGuerriro", df.format(quantidadeTotalGuerriro));
        
        //aqtaque por minuto
        
        long quantGuerreiros = usuario.getGuerreiros();
        long quantAtaqueBaseGuerreiros = usuario.getAtaqueBaseGuerreiros();
        
        long totalAtaquePorMinuto =(quantGuerreiros)* (quantAtaqueBaseGuerreiros);
        model.addAttribute("ataquePorMinutoUsuario", totalAtaquePorMinuto);
        

        Long energia = usuario.getEnergiaGuerreiros();
        if (energia == null) energia = 0L;
        
        model.addAttribute("energiaGuerreiros", energia);
        
        
        model.addAttribute("energiaGuerreirosPadrao", usuario.getEnergiaGuerreirosPadrao());
        
        
        Long pocaoVigor = Optional
                .ofNullable(usuario.getPocaoVigor())
                .orElse(0L);

        model.addAttribute("quantidadePocaoVigor", pocaoVigor);

        
        // Constrói link de convite
        //String linkReferencia = "https://bossbattle.com/aliados?ref=" + Base64.getUrlEncoder().encodeToString(usuario.getId().toString().getBytes());

         String linkReferencia = "https://boss-battle.up.railway.app//aliados?ref=" + Base64.getUrlEncoder().encodeToString(usuario.getId().toString().getBytes());
     //--------------------------------------------------------------------------------------------------------------------------------------------------------------
      // String linkReferencia = "http://localhost:8080/aliados?ref=" + Base64.getUrlEncoder().encodeToString(usuario.getId().toString().getBytes());
       model.addAttribute("linkReferencia", linkReferencia);
        
          List<UsuarioBossBattle> indicados = repo.findByReferredBy(usuario.getId());
          model.addAttribute("indicados", indicados);
          model.addAttribute("qtdIndicados", indicados.size());
          
          
       BigDecimal ganhos = usuario.getGanhosPendentesReferral() != null ? usuario.getGanhosPendentesReferral() : BigDecimal.ZERO;
       ganhos = ganhos.setScale(1, RoundingMode.HALF_UP);
       model.addAttribute("ganhosPendentesReferral", ganhos);
        
      

        BigDecimal coins = usuario.getBossCoins();
        if (coins == null) coins = BigDecimal.ZERO;

        model.addAttribute("boss_coins", df.format(coins));

        
        
        
        

        return "dashboard"; // Nome do template HTML: dashboard.html
    }
}
