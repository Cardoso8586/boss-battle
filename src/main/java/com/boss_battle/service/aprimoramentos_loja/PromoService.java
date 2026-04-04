package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.dto.PromoPrecoDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PromoService {
	
    DecimalFormat df = new DecimalFormat("0");
    DecimalFormat moeda = new DecimalFormat("#,##0");
    
    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Autowired
    private LojaAprimoramentosService lojaAprimoramentosService;

    public String comprarPromo(UsuarioBossBattle usuario, String tipoPromo) {

    	 // DecimalFormat df = new DecimalFormat("#,#0");
    	
        // 🔒 Preços atuais
        long precoGuerreiro = usuario.getPrecoGuerreiros();
        long precoPocaoVigor = usuario.getPrecoPocaoVigor();
        long precoEspada = usuario.getPrecoEspadaFlanejante();
        long precoMachado = usuario.getPrecoMachadoDilacerador();
        long precoArcoCelestial = lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();

        long valorBruto;
        double desconto;

        // 🧮 Calcula custo REAL do pacote
        switch (tipoPromo.toUpperCase()) {

            case "NORMAL" -> {
                valorBruto =
                        (2 * precoGuerreiro) +
                        (2 * precoPocaoVigor) +
                        (1 * precoEspada);
                desconto = 0.15;
            }

            case "AVANCADA" -> {
                valorBruto =
                        (4 * precoGuerreiro) +
                        (3 * precoPocaoVigor) +
                        (2 * precoEspada) +
                        (1 * precoMachado);
                desconto = 0.25;
            }

            case "ESPECIAL" -> {
                valorBruto =
                        (6 * precoGuerreiro) +
                        (5 * precoPocaoVigor) +
                        (3 * precoEspada) +
                        (2 * precoMachado);
                desconto = 0.35;
            }

            case "LENDARIA" -> {
                valorBruto =
                        (10 * precoGuerreiro) +
                        (8 * precoPocaoVigor) +
                        (5 * precoEspada) +
                        (3 * precoMachado) +
                        (3 * precoArcoCelestial);
                desconto = 0.50;
            }

            default -> {
                return "❌ Tipo de promoção inválido.";
            }
        }

        // 💸 Aplica desconto
        BigDecimal precoFinal = BigDecimal.valueOf(valorBruto)
                .multiply(BigDecimal.valueOf(1 - desconto))
                .setScale(0, RoundingMode.DOWN);
        
      
       //verifica o saldo, e mostra se erro
        if (usuario.getBossCoins().compareTo(precoFinal) < 0) {
            throw new RuntimeException(
                "Saldo insuficiente. Valor com desconto: " 
                + moeda.format(precoFinal.doubleValue())
            );
        }
      
        // 💰 Debita
        usuario.setBossCoins(usuario.getBossCoins().subtract(precoFinal));

        // 🎁 ENTREGA DOS ITENS
        
        
        int quantidade = 0;
       
       
        switch (tipoPromo.toUpperCase()) {

            case "NORMAL" -> {
                usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 2);
                usuario.setPocaoVigor(usuario.getPocaoVigor() + 2);
                usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + 1);
                
                
                // 🔁 Recalcula preço (sem salvar usuário ainda)
                quantidade = 2;
                lojaAprimoramentosService.atualizarPrecoGuerreiro(usuario, quantidade);
                
            }

            case "AVANCADA" -> {
                usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 4);
                usuario.setPocaoVigor(usuario.getPocaoVigor() + 3);
                usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + 2);
                usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + 1);
                
                // 🔁 Recalcula preço (sem salvar usuário ainda)
                quantidade = 4;
                lojaAprimoramentosService.atualizarPrecoGuerreiro(usuario, quantidade);
                
            }

            case "ESPECIAL" -> {
            	usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 6);
                usuario.setPocaoVigor(usuario.getPocaoVigor() + 5);
                usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + 3);
                usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + 2);
                // 🔁 Recalcula preço (sem salvar usuário ainda)
                quantidade = 6;
                lojaAprimoramentosService.atualizarPrecoGuerreiro(usuario, quantidade);
                
            }

            case "LENDARIA" -> {
            	usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 10);
                usuario.setPocaoVigor(usuario.getPocaoVigor() + 8);
                usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + 5);
                usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + 3);
                
                // 🔁 Recalcula preço (sem salvar usuário ainda)
                quantidade = 10;
                lojaAprimoramentosService.atualizarPrecoGuerreiro(usuario, quantidade);
                
            }
        }

        // 💾 Salva
        usuarioRepository.save(usuario);

        return "Promoção " + tipoPromo.toUpperCase()
        + " comprada! Você economizou "
        + df.format(desconto * 100) + "%";
    }
    
    //=============================================================
                   //PREÇOS
    //=============================================================
    
    public PromoPrecoDTO calcularPreviewPromo(
            UsuarioBossBattle usuario,
            String tipoPromo) {

        long precoGuerreiro = usuario.getPrecoGuerreiros();
        long precoPocao = usuario.getPrecoPocaoVigor();
        long precoEspada = usuario.getPrecoEspadaFlanejante();
        long precoMachado = usuario.getPrecoMachadoDilacerador();
        long precoArco = lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();

        long valorBruto;
        int descontoPercentual;

        switch (tipoPromo.toUpperCase()) {

            case "NORMAL" -> {
                valorBruto =
                        (2 * precoGuerreiro) +
                        (2 * precoPocao) +
                        (1 * precoEspada);
                descontoPercentual = 15;
            }

            case "AVANCADA" -> {
                valorBruto =
                        (4 * precoGuerreiro) +
                        (3 * precoPocao) +
                        (2 * precoEspada) +
                        (1 * precoMachado);
                descontoPercentual = 25;
            }

            case "ESPECIAL" -> {
                valorBruto =
                        (6 * precoGuerreiro) +
                        (5 * precoPocao) +
                        (3 * precoEspada) +
                        (2 * precoMachado);
                descontoPercentual = 35;
            }

            case "LENDARIA" -> {
                valorBruto =
                        (10 * precoGuerreiro) +
                        (8 * precoPocao) +
                        (5 * precoEspada) +
                        (3 * precoMachado) +
                        (3 * precoArco);
                descontoPercentual = 50;
            }

            default -> throw new RuntimeException("Promoção inválida");
        }

        BigDecimal precoBruto = BigDecimal.valueOf(valorBruto);

        BigDecimal precoFinal = precoBruto
                .multiply(BigDecimal.valueOf(1 - (descontoPercentual / 100.0)))
                .setScale(0, RoundingMode.DOWN);

        return new PromoPrecoDTO(
                precoBruto,
                precoFinal,
                descontoPercentual
        );
    }

}

