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

    private static final long LIMITE_GUERREIRO = 1_000L;

    DecimalFormat df = new DecimalFormat("0");
    DecimalFormat moeda = new DecimalFormat("#,##0");

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Autowired
    private LojaAprimoramentosService lojaAprimoramentosService;

    public String comprarPromo(UsuarioBossBattle usuario, String tipoPromo) {

        if (usuario == null || usuario.getId() == null) {
            throw new RuntimeException("Usuário inválido.");
        }

        UsuarioBossBattle usuarioLock = usuarioRepository.findByIdForUpdate(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (usuarioLock.getBossCoins() == null) {
            usuarioLock.setBossCoins(BigDecimal.ZERO);
        }

        String tipo = tipoPromo == null ? "" : tipoPromo.toUpperCase();

        long precoGuerreiro = usuarioLock.getPrecoGuerreiros();
        long precoPocaoVigor = usuarioLock.getPrecoPocaoVigor();
        long precoEspada = usuarioLock.getPrecoEspadaFlanejante();
        long precoMachado = usuarioLock.getPrecoMachadoDilacerador();
        long precoArcoCelestial = lojaAprimoramentosService.getPRECO_ARCO_CELESTIAL();

        long valorBruto;
        double desconto;
        int guerreirosRecebidos;

        switch (tipo) {
            case "NORMAL" -> {
                valorBruto = (2 * precoGuerreiro) + (2 * precoPocaoVigor) + precoEspada;
                desconto = 0.15;
                guerreirosRecebidos = 2;
            }
            case "AVANCADA" -> {
                valorBruto = (4 * precoGuerreiro) + (3 * precoPocaoVigor) + (2 * precoEspada) + precoMachado;
                desconto = 0.25;
                guerreirosRecebidos = 4;
            }
            case "ESPECIAL" -> {
                valorBruto = (6 * precoGuerreiro) + (5 * precoPocaoVigor) + (3 * precoEspada) + (2 * precoMachado);
                desconto = 0.35;
                guerreirosRecebidos = 6;
            }
            case "LENDARIA" -> {
                valorBruto = (10 * precoGuerreiro) + (8 * precoPocaoVigor) + (5 * precoEspada)
                        + (3 * precoMachado) + (3 * precoArcoCelestial);
                desconto = 0.50;
                guerreirosRecebidos = 10;
            }
            default -> throw new RuntimeException("Tipo de promoção inválido.");
        }

        long totalGuerreiros =
                usuarioLock.getGuerreiros()
                        + usuarioLock.getGuerreirosInventario()
                        + usuarioLock.getGuerreirosRetaguarda();

        if (totalGuerreiros + guerreirosRecebidos > LIMITE_GUERREIRO) {
            throw new RuntimeException("Limite máximo de guerreiros atingido.");
        }

        BigDecimal precoFinal = BigDecimal.valueOf(valorBruto)
                .multiply(BigDecimal.valueOf(1 - desconto))
                .setScale(0, RoundingMode.DOWN);

        if (usuarioLock.getBossCoins().compareTo(precoFinal) < 0) {
            throw new RuntimeException(
                    "Saldo insuficiente. Valor com desconto: "
                            + moeda.format(precoFinal.doubleValue())
            );
        }

        usuarioLock.setBossCoins(usuarioLock.getBossCoins().subtract(precoFinal));

        switch (tipo) {
            case "NORMAL" -> {
                usuarioLock.setGuerreirosInventario(usuarioLock.getGuerreirosInventario() + 2);
                usuarioLock.setPocaoVigor(usuarioLock.getPocaoVigor() + 2);
                usuarioLock.setEspadaFlanejante(usuarioLock.getEspadaFlanejante() + 1);
            }
            case "AVANCADA" -> {
                usuarioLock.setGuerreirosInventario(usuarioLock.getGuerreirosInventario() + 4);
                usuarioLock.setPocaoVigor(usuarioLock.getPocaoVigor() + 3);
                usuarioLock.setEspadaFlanejante(usuarioLock.getEspadaFlanejante() + 2);
                usuarioLock.setMachadoDilacerador(usuarioLock.getMachadoDilacerador() + 1);
            }
            case "ESPECIAL" -> {
                usuarioLock.setGuerreirosInventario(usuarioLock.getGuerreirosInventario() + 6);
                usuarioLock.setPocaoVigor(usuarioLock.getPocaoVigor() + 5);
                usuarioLock.setEspadaFlanejante(usuarioLock.getEspadaFlanejante() + 3);
                usuarioLock.setMachadoDilacerador(usuarioLock.getMachadoDilacerador() + 2);
            }
            case "LENDARIA" -> {
                usuarioLock.setGuerreirosInventario(usuarioLock.getGuerreirosInventario() + 10);
                usuarioLock.setPocaoVigor(usuarioLock.getPocaoVigor() + 8);
                usuarioLock.setEspadaFlanejante(usuarioLock.getEspadaFlanejante() + 5);
                usuarioLock.setMachadoDilacerador(usuarioLock.getMachadoDilacerador() + 3);
                usuarioLock.setInventarioArco(usuarioLock.getInventarioArco() + 3);
            }
        }

        lojaAprimoramentosService.atualizarPrecoGuerreiro(usuarioLock, guerreirosRecebidos);

        usuarioRepository.save(usuarioLock);

        return "Promoção " + tipo
                + " comprada! Você economizou "
                + df.format(desconto * 100) + "%";
    }

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
                valorBruto = (2 * precoGuerreiro) + (2 * precoPocao) + precoEspada;
                descontoPercentual = 15;
            }
            case "AVANCADA" -> {
                valorBruto = (4 * precoGuerreiro) + (3 * precoPocao) + (2 * precoEspada) + precoMachado;
                descontoPercentual = 25;
            }
            case "ESPECIAL" -> {
                valorBruto = (6 * precoGuerreiro) + (5 * precoPocao) + (3 * precoEspada) + (2 * precoMachado);
                descontoPercentual = 35;
            }
            case "LENDARIA" -> {
                valorBruto = (10 * precoGuerreiro) + (8 * precoPocao) + (5 * precoEspada)
                        + (3 * precoMachado) + (3 * precoArco);
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