package com.boss_battle.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.aprimoramentos_loja.LojaAprimoramentosService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

	@Autowired
	LojaAprimoramentosService lojaAprimoramentosService;
    private final UsuarioBossBattleRepository usuarioRepository;
    

    public ViewController(UsuarioBossBattleRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


	
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // corresponde a templates/login.html
    }

    @GetMapping("/cadastro")
    public String registerPage() {
        return "cadastro"; // templates/cadastro.html
    }
    
    /*
    @GetMapping("/anuncio-recompensa")
    public String anuncioRecompensa() {
        return "anuncio-recompensa"; // templates/anuncio-recompensa.html
    }
    */
    
    @GetMapping("/anuncio-recompensa")
    public String anuncioRecompensa(@RequestParam("usuarioId") Long usuarioId,
                                    Model model) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        model.addAttribute("usuario", usuario);

        return "anuncio-recompensa";
    }
    

    @GetMapping("/desafios")
    public String desafiosArena(HttpSession session, Model model) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/arena";
        }

        DecimalFormat df = new DecimalFormat("#,##0");

        model.addAttribute("usuario", usuario);

        model.addAttribute("idUsuario", usuario.getId());

        model.addAttribute("xpUsuario",
                df.format(usuario.getExp()));

        model.addAttribute("nivelUsuario",
                df.format(usuario.getNivel()));

        BigDecimal coins = usuario.getBossCoins();

        if (coins == null) {
            coins = BigDecimal.ZERO;
        }

        model.addAttribute("boss_coins",
                df.format(coins));

        return "desafios";
    }
    
    //recarregar-vigor
 // recarregar-vigor
    @GetMapping("/recarregar-vigor")
    public String recarregarVigor(
            HttpSession session,
            Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
                usuarioRepository
                        .findById(usuarioSessao.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não encontrado")
                        );

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(new Locale("pt", "BR"));

        DecimalFormat df =
                new DecimalFormat("#,##0", symbols);
        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        model.addAttribute("xpUsuario", df.format(usuario.getExp()));
        model.addAttribute("nivelUsuario", df.format(usuario.getNivel()));

        BigDecimal coins = usuario.getBossCoins();

        if (coins == null) {
            coins = BigDecimal.ZERO;
        }

        model.addAttribute("boss_coins", df.format(coins));

        long energiaGuerreiros =
                usuario.getEnergiaGuerreiros();

        long energiaMaxima =
                usuario.getEnergiaGuerreirosPadrao();

        if (energiaMaxima <= 0) {
            energiaMaxima = 50L;
        }

        // valores numéricos para lógica
        model.addAttribute("energiaGuerreiros", energiaGuerreiros);
        model.addAttribute("energiaMaxima", energiaMaxima);
        model.addAttribute("energiaGuerreirosPadrao", energiaMaxima);

        // valores formatados para visual
        model.addAttribute("energiaGuerreirosFormatado", df.format(energiaGuerreiros));
        model.addAttribute("energiaMaximaFormatado", df.format(energiaMaxima));
        model.addAttribute("energiaGuerreirosPadraoFormatado", df.format(energiaMaxima));

        return "recarregar-vigor";
    }
    /*
    @GetMapping("/recarregar-vigor")
    public String recarregarVigor(
            HttpSession session,
            Model model) {

        UsuarioBossBattle usuarioSessao =
            (UsuarioBossBattle)
                session.getAttribute(
                    "usuarioLogado"
                );

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
            usuarioRepository
                .findById(
                    usuarioSessao.getId()
                )
                .orElseThrow(() ->
                    new RuntimeException(
                        "Usuário não encontrado"
                    )
                );

        DecimalFormat df =
            new DecimalFormat("#,##0");

        model.addAttribute(
            "usuario",
            usuario
        );

        model.addAttribute(
            "idUsuario",
            usuario.getId()
        );

        long energiaGuerreiros =
            usuario.getEnergiaGuerreiros();

        long energiaMaxima =
            usuario.getEnergiaGuerreirosPadrao();

        if (energiaMaxima <= 0) {
            energiaMaxima = 50L;
        }

        model.addAttribute(
            "energiaGuerreiros",
            energiaGuerreiros
        );

        model.addAttribute(
            "energiaMaxima",
            energiaMaxima
        );

        model.addAttribute(
            "energiaGuerreirosPadrao",
            energiaMaxima
        );

        return "recarregar-vigor";
    }
    */
  
    @GetMapping("/aprimoramentos")
    public String aprimoramentos(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
                usuarioRepository
                        .findById(usuarioSessao.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não encontrado"));

        session.setAttribute("usuarioLogado", usuario);

        DecimalFormat df = new DecimalFormat("#,##0");

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        model.addAttribute("xpUsuario", df.format(usuario.getExp()));
        model.addAttribute("nivelUsuario", df.format(usuario.getNivel()));

        BigDecimal coins = usuario.getBossCoins();

        if (coins == null) {
            coins = BigDecimal.ZERO;
        }
        
        //  long precoPocaoVigor = usuario.getPrecoPocaoVigor();
        long precoPocaoVigor =  lojaAprimoramentosService.getPOCAO_VIGOR();
        // long precoEspadaFlanejante = usuario.getPrecoEspadaFlanejante();
        long precoEspadaFlanejante = lojaAprimoramentosService.getPRECO_ESPADA_FLANEJANTE();
        // long precoMachadoDilacerador = usuario.getPrecoMachadoDilacerador();
        long precoMachadoDilacerador = lojaAprimoramentosService.getPRECO_MACHADO_DILACERADOR();
        Long precoArcoCelestial = lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();
        // long precoEscudo = usuario.getPrecoEscudoPrimordial();
        Long precoEscudo = lojaAprimoramentosService.getPRECO_ESCUDO_PRIMORDIAL();
        
        
        model.addAttribute("boss_coins", df.format(coins));

        model.addAttribute("precoGuerreiros", df.format(usuario.getPrecoGuerreiros()));
        model.addAttribute("precoEnergia", df.format(usuario.getPrecoEnergia()));
        model.addAttribute("precoAtaqueEspecial", df.format(usuario.getPrecoAtaqueEspecial()));
        model.addAttribute("precoPocaoVigor", df.format(precoPocaoVigor));
        model.addAttribute("precoEspadaFlanejante", df.format(precoEspadaFlanejante));
        model.addAttribute("precoMachadoDilacerador", df.format(precoMachadoDilacerador));
        model.addAttribute("precoArcoCelestial", df.format(precoArcoCelestial));
        model.addAttribute("precoEscudoPrimordial", df.format(precoEscudo));

        return "aprimoramentos";
    }
    
    
    @GetMapping("/central-comando")
    public String centralComando(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
        		usuarioRepository.findById(usuarioSessao.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não encontrado"));

        session.setAttribute("usuarioLogado", usuario);

        DecimalFormat df = new DecimalFormat("#,##0");

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        model.addAttribute("ataque_base",
                df.format(usuario.getAtaqueBase()));

        model.addAttribute("xpUsuario",
                df.format(usuario.getExp()));

        model.addAttribute("nivelUsuario",
                df.format(usuario.getNivel()));

        model.addAttribute("guerreirosUsuario",
                df.format(usuario.getGuerreiros()));

        model.addAttribute("guerreirosRetaguarda",
                df.format(usuario.getGuerreirosRetaguarda()));

        long guerreiroAtaque = usuario.getGuerreiros();
        long guerreiroRetaguarda = usuario.getGuerreirosRetaguarda();
        long guerreiroInventario = usuario.getGuerreirosInventario();

        long quantidadeTotalGuerriro =
                guerreiroAtaque
                + guerreiroRetaguarda
                + guerreiroInventario;

        model.addAttribute(
                "quantidadeTotalGuerriro",
                df.format(quantidadeTotalGuerriro)
        );

        long quantGuerreiros = usuario.getGuerreiros();

        long quantAtaqueBaseGuerreiros =
                usuario.getAtaqueBaseGuerreiros();

        long totalAtaquePorMinuto =
                quantGuerreiros
                * quantAtaqueBaseGuerreiros;

        model.addAttribute(
                "ataquePorMinutoUsuario",
                df.format(totalAtaquePorMinuto)
        );

        Long energia = usuario.getEnergiaGuerreiros();

        if (energia == null) {
            energia = 0L;
        }

        Long energiaMaxima =
                usuario.getEnergiaGuerreirosPadrao();

        if (energiaMaxima == null || energiaMaxima <= 0) {
            energiaMaxima = 50L;
        }

        model.addAttribute("energiaGuerreiros", energia);

        model.addAttribute("energiaMaxima",
                energiaMaxima);

        model.addAttribute(
                "energiaGuerreirosPadrao",
                energiaMaxima
        );

        Long pocaoVigor =
                Optional.ofNullable(usuario.getPocaoVigor())
                        .orElse(0L);

        model.addAttribute(
                "quantidadePocaoVigor",
                pocaoVigor
        );

        model.addAttribute(
                "quantidadeEspadaFlanejante",
                df.format(usuario.getEspadaFlanejante())
        );

        model.addAttribute(
                "quantidadeMachadoDilacerador",
                df.format(usuario.getMachadoDilacerador())
        );

        model.addAttribute(
                "quantidadeArcoCelestial",
                df.format(usuario.getInventarioArco())
        );

        model.addAttribute(
                "quantidadeFlechaDiamante",
                df.format(usuario.getFlechaDiamante())
        );

        model.addAttribute(
                "quantidadeFlechaFogo",
                df.format(usuario.getFlechaFogo())
        );

        model.addAttribute(
                "quantidadeFlechaVeneno",
                df.format(usuario.getFlechaVeneno())
        );

        model.addAttribute(
                "quantidadeFlechaFerro",
                df.format(usuario.getFlechaFerro())
        );
        
        //get escudos
	     model.addAttribute(""
	     		+ "quantidadeEscudoPrimordial",
	     		usuario.getEscudoPrimordial()
	     );

        BigDecimal coins = usuario.getBossCoins();

        if (coins == null) {
            coins = BigDecimal.ZERO;
        }

        model.addAttribute(
                "boss_coins",
                df.format(coins)
        );

        return "central-comando";
    }
    
    //resgatar-recompensas
    @GetMapping("/resgatar-recompensas")
    public String resgatarRecompensas(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
        		usuarioRepository.findById(usuarioSessao.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não encontrado"));

        session.setAttribute("usuarioLogado", usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());

        return "resgatar-recompensas";
    }//resgatar-recompensas
    
    
    
    @GetMapping("/referidos")
    public String aliados(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle)
                        session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario =
        		usuarioRepository.findById(usuarioSessao.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuário não encontrado"
                                ));

        session.setAttribute(
                "usuarioLogado",
                usuario
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "idUsuario",
                usuario.getId()
        );

        // =====================================
        // LINK REFERÊNCIA
        // =====================================

        String linkReferencia =
                "https://boss-battle-arena.com/aliados?ref="
                        + Base64.getUrlEncoder()
                        .encodeToString(
                                usuario.getId()
                                        .toString()
                                        .getBytes()
                        );

        model.addAttribute(
                "linkReferencia",
                linkReferencia
        );

        // =====================================
        // INDICADOS
        // =====================================

        List<UsuarioBossBattle> indicados =
        		usuarioRepository.findByReferredBy(
                        usuario.getId()
                );

        model.addAttribute(
                "indicados",
                indicados
        );

        model.addAttribute(
                "qtdIndicados",
                indicados.size()
        );

        // =====================================
        // GANHOS
        // =====================================

        BigDecimal ganhos =
                usuario.getGanhosPendentesReferral();

        if (ganhos == null) {
            ganhos = BigDecimal.ZERO;
        }

        ganhos = ganhos.setScale(
                1,
                RoundingMode.HALF_UP
        );

        model.addAttribute(
                "ganhosPendentesReferral",
                ganhos
        );

        return "referidos";
    }
    
 // =========================
 // GANHAR BOSS COINS
 // =========================
 @GetMapping("/ganhar-boss-coins")
 public String ganharBossCoins(HttpSession session, Model model) {

     UsuarioBossBattle usuarioSessao =
             (UsuarioBossBattle) session.getAttribute("usuarioLogado");

     if (usuarioSessao == null) {
         return "redirect:/arena";
     }

     UsuarioBossBattle usuario = usuarioRepository
             .findById(usuarioSessao.getId())
             .orElseThrow(() ->
                     new RuntimeException("Usuário não encontrado"));

     // atualiza sessão
     session.setAttribute("usuarioLogado", usuario);

     // dados da tela
     model.addAttribute("usuario", usuario);
     model.addAttribute("idUsuario", usuario.getId());

     // link zerads
     String zeradsLink =
             "https://zerads.com/ptc.php?ref=10783&user="
                     + usuario.getUsername();

     model.addAttribute("zeradsLink", zeradsLink);

     return "ganhar-boss-coins";
 }
    
    @GetMapping("/aliados")
    public String mostrarCadastro(@RequestParam(required = false) String ref, Model model) {
        // Cria um objeto usuário vazio para o formulário
        model.addAttribute("usuario", new UsuarioBossBattle());
        // Passa a referência (ou string vazia) para o formulário
        model.addAttribute("ref", ref != null ? ref : "");
        return "cadastro"; // nome do template Thymeleaf
    }



    
}
//