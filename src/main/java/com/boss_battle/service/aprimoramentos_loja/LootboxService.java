package com.boss_battle.service.aprimoramentos_loja;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LootboxService {

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    private final Random random = new Random();

    private final long precoBasica = 500L;
    private final long precoAvancada = 800L;
    private final long precoEspecial = 1_000L;
    private final long precoLendaria = 1_500L;

    public String abrirLootboxPorNivel(UsuarioBossBattle usuario, String tipoLootbox) {

        if (usuario == null || usuario.getId() == null) {
            throw new RuntimeException("Usuário inválido");
        }

        UsuarioBossBattle usuarioLock = usuarioRepository.findByIdForUpdate(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuarioLock.getBossCoins() == null) {
            usuarioLock.setBossCoins(BigDecimal.ZERO);
        }

        int quantidade;
        TipoFlecha tipoFlecha;
        long preco;

        switch (tipoLootbox.toLowerCase()) {
            case "basica" -> {
                quantidade = 1 + random.nextInt(10);
                tipoFlecha = TipoFlecha.FERRO;
                preco = precoBasica;
            }
            case "avancada" -> {
                quantidade = 1 + random.nextInt(15);
                tipoFlecha = TipoFlecha.FOGO;
                preco = precoAvancada;
            }
            case "especial" -> {
                quantidade = 1 + random.nextInt(20);
                tipoFlecha = TipoFlecha.VENENO;
                preco = precoEspecial;
            }
            case "lendaria" -> {
                quantidade = 1 + random.nextInt(25);
                tipoFlecha = TipoFlecha.DIAMANTE;
                preco = precoLendaria;
            }
            default -> throw new RuntimeException("Tipo de lootbox inválido");
        }

        BigDecimal valorTotal = BigDecimal.valueOf(preco);

        if (usuarioLock.getBossCoins().compareTo(valorTotal) < 0) {
            throw new RuntimeException("Saldo insuficiente para abrir a lootbox");
        }

        usuarioLock.setBossCoins(usuarioLock.getBossCoins().subtract(valorTotal));

        switch (tipoFlecha) {
            case FERRO -> usuarioLock.setFlechaFerro(usuarioLock.getFlechaFerro() + quantidade);
            case FOGO -> usuarioLock.setFlechaFogo(usuarioLock.getFlechaFogo() + quantidade);
            case VENENO -> usuarioLock.setFlechaVeneno(usuarioLock.getFlechaVeneno() + quantidade);
            case DIAMANTE -> usuarioLock.setFlechaDiamante(usuarioLock.getFlechaDiamante() + quantidade);
        }

        usuarioRepository.save(usuarioLock);

        String flecha = quantidade == 1 ? "Flecha" : "Flechas";

        return "Você recebeu " + quantidade + " " + flecha + " de " +
                tipoFlecha.name().toLowerCase();
    }

    public long getPrecoBasica() {
        return precoBasica;
    }

    public long getPrecoAvancada() {
        return precoAvancada;
    }

    public long getPrecoEspecial() {
        return precoEspecial;
    }

    public long getPrecoLendaria() {
        return precoLendaria;
    }
}