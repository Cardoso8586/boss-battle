

package com.boss_battle.service.loja_guerreiros_elite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.CompraGuerreiroEliteResponse;
import com.boss_battle.dto.GuerreiroEliteResponse;
import com.boss_battle.dto.GuerreiroUsuarioCardResponse;
import com.boss_battle.model.Guerreiro;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.model.UsuarioGuerreiro;
import com.boss_battle.repository.GuerreiroRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.repository.UsuarioGuerreiroRepository;

@Service
@Transactional
public class LojaGuerreiroEliteService {

    private final UsuarioBossBattleRepository repo;
    private final GuerreiroRepository guerreiroRepository;
    private final UsuarioGuerreiroRepository usuarioGuerreiroRepository;

    private static final BigDecimal AUMENTO = new BigDecimal("0.2");
    private static final long QUANTIDADE_MAXIMA_POR_COMPRA = 5L;

    public LojaGuerreiroEliteService(
            UsuarioBossBattleRepository repo,
            GuerreiroRepository guerreiroRepository,
            UsuarioGuerreiroRepository usuarioGuerreiroRepository
    ) {
        this.repo = repo;
        this.guerreiroRepository = guerreiroRepository;
        this.usuarioGuerreiroRepository = usuarioGuerreiroRepository;
    }

    public CompraGuerreiroEliteResponse comprarGuerreiroElite(
            Long usuarioId,
            Long guerreiroId,
            int quantidade
    ) {

        if (quantidade <= 0) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Quantidade inválida.",
                    null,
                    null,
                    null
            );
        }

        if (quantidade > QUANTIDADE_MAXIMA_POR_COMPRA) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Quantidade máxima por compra é 5.",
                    null,
                    null,
                    null
            );
        }

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Guerreiro guerreiro = guerreiroRepository.findById(guerreiroId)
                .orElseThrow(() -> new RuntimeException("Guerreiro não encontrado"));

        if (!guerreiro.isAtivo()) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Este guerreiro não está disponível.",
                    usuario.getBossCoins(),
                    null,
                    null
            );
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        long limiteGuerreiro = guerreiro.getQuantidadeMaxima() != null
                ? guerreiro.getQuantidadeMaxima()
                : 100L;

        UsuarioGuerreiro usuarioGuerreiro = usuarioGuerreiroRepository
                .findByUsuarioAndGuerreiro(usuario, guerreiro)
                .orElseGet(() -> {
                    UsuarioGuerreiro novo = new UsuarioGuerreiro();
                    novo.setUsuario(usuario);
                    novo.setGuerreiro(guerreiro);
                    novo.setQuantidade(0L);
                    novo.setNivel(1L);
                    return novo;
                });

        long quantidadeAtual = usuarioGuerreiro.getQuantidade() != null
                ? usuarioGuerreiro.getQuantidade()
                : 0L;

        if (quantidadeAtual >= limiteGuerreiro) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Você já possui o limite máximo deste guerreiro.",
                    usuario.getBossCoins(),
                    null,
                    quantidadeAtual
            );
        }

        if (quantidadeAtual + quantidade > limiteGuerreiro) {
            long podeComprar = limiteGuerreiro - quantidadeAtual;

            return new CompraGuerreiroEliteResponse(
                    false,
                    "Limite máximo deste guerreiro é " + limiteGuerreiro
                            + " unidades. Você ainda pode comprar " + podeComprar + ".",
                    usuario.getBossCoins(),
                    null,
                    quantidadeAtual
            );
        }

        BigDecimal precoBase = BigDecimal.valueOf(guerreiro.getCustoCompra());

        BigDecimal valorTotal = calcularPrecoCompra(
                precoBase,
                quantidadeAtual,
                quantidade
        );

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Boss Coins insuficientes.",
                    usuario.getBossCoins(),
                    calcularPreco(precoBase, quantidadeAtual),
                    quantidadeAtual
            );
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        long novaQuantidade = quantidadeAtual + quantidade;
        usuarioGuerreiro.setQuantidade(novaQuantidade);

        usuarioGuerreiroRepository.save(usuarioGuerreiro);
        repo.save(usuario);

        BigDecimal precoAtualDepoisCompra = calcularPreco(
                precoBase,
                novaQuantidade
        );

        return new CompraGuerreiroEliteResponse(
                true,
                "Guerreiro comprado com sucesso.",
                usuario.getBossCoins(),
                precoAtualDepoisCompra,
                novaQuantidade
        );
    }

    public BigDecimal calcularPreco(BigDecimal precoBase, long quantidadeAtual) {
        return precoBase
                .multiply(BigDecimal.ONE.add(AUMENTO.multiply(BigDecimal.valueOf(quantidadeAtual))))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPrecoCompra(
            BigDecimal precoBase,
            long quantidadeAtual,
            long quantidadeCompra
    ) {
        if (quantidadeCompra <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        if (quantidadeCompra > QUANTIDADE_MAXIMA_POR_COMPRA) {
            throw new IllegalArgumentException("Quantidade máxima por compra é 5.");
        }

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (long i = 0; i < quantidadeCompra; i++) {
            custoTotal = custoTotal.add(
                    calcularPreco(precoBase, quantidadeAtual + i)
            );
        }

        return custoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public List<GuerreiroEliteResponse> listarGuerreirosLoja(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return guerreiroRepository.findByAtivoTrue()
                .stream()
                .map(guerreiro -> {

                    UsuarioGuerreiro usuarioGuerreiro =
                            usuarioGuerreiroRepository
                                    .findByUsuarioAndGuerreiro(usuario, guerreiro)
                                    .orElse(null);

                    long quantidadeAtual = usuarioGuerreiro != null &&
                            usuarioGuerreiro.getQuantidade() != null
                            ? usuarioGuerreiro.getQuantidade()
                            : 0L;

                    BigDecimal precoAtual = calcularPreco(
                            BigDecimal.valueOf(guerreiro.getCustoCompra()),
                            quantidadeAtual
                    );

                    return new GuerreiroEliteResponse(
                            guerreiro.getId(),
                            guerreiro.getNome(),
                            guerreiro.getTipo(),
                            guerreiro.getDanoBase(),
                            guerreiro.getImagem(),
                            precoAtual,
                            quantidadeAtual,
                            guerreiro.isAtivo(),
                            guerreiro.getQuantidadeMaxima()
                    );
                })
                .toList();
    }

    public List<GuerreiroUsuarioCardResponse> listarGuerreirosUsuario(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<GuerreiroUsuarioCardResponse> lista = new ArrayList<>();

        long antigosAtaque = usuario.getGuerreiros();
        long antigosRetaguarda = usuario.getGuerreirosRetaguarda();
        long antigosInventario = usuario.getGuerreirosInventario();

        long totalTradicional =
                antigosAtaque
                        + antigosRetaguarda
                        + antigosInventario;

        long ataqueBaseTradicional = usuario.getAtaqueBaseGuerreiros();

        if (totalTradicional > 0) {
            lista.add(new GuerreiroUsuarioCardResponse(
                    0L,
                    "Guerreiro Tradicional",
                    "COMUM",
                    totalTradicional,
                    ataqueBaseTradicional,
                    antigosAtaque * ataqueBaseTradicional,
                    "guerreiro-tradicional-card.webp",
                    false
            ));
        }

        List<UsuarioGuerreiro> guerreirosElite =
                usuarioGuerreiroRepository.findByUsuario(usuario);

        for (UsuarioGuerreiro ug : guerreirosElite) {

            if (ug.getGuerreiro() == null) {
                continue;
            }

            long quantidadeDesteGuerreiro =
                    ug.getQuantidade() == null ? 0L : ug.getQuantidade();

            if (quantidadeDesteGuerreiro <= 0) {
                continue;
            }

            Long danoBaseObj = ug.getGuerreiro().getDanoBase();

            long danoBase = danoBaseObj == null ? 0L : danoBaseObj;

            lista.add(new GuerreiroUsuarioCardResponse(
                    ug.getGuerreiro().getId(),
                    ug.getGuerreiro().getNome(),
                    ug.getGuerreiro().getTipo(),
                    quantidadeDesteGuerreiro,
                    danoBase,
                    quantidadeDesteGuerreiro * danoBase,
                    ug.getGuerreiro().getImagem(),
                    true
            ));
        }

        return lista;
    }
}






/**

package com.boss_battle.service.loja_guerreiros_elite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.CompraGuerreiroEliteResponse;
import com.boss_battle.dto.GuerreiroEliteResponse;
import com.boss_battle.dto.GuerreiroUsuarioCardResponse;
import com.boss_battle.model.Guerreiro;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.model.UsuarioGuerreiro;
import com.boss_battle.repository.GuerreiroRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.repository.UsuarioGuerreiroRepository;

@Service
@Transactional
public class LojaGuerreiroEliteService {

    private final UsuarioBossBattleRepository repo;
    private final GuerreiroRepository guerreiroRepository;
    private final UsuarioGuerreiroRepository usuarioGuerreiroRepository;

    private static final BigDecimal AUMENTO = new BigDecimal("0.2");
    private static final long QUANTIDADE_MAXIMA = 5L;
    private static final long LIMITE_GUERREIRO = 100L;

    public LojaGuerreiroEliteService(
            UsuarioBossBattleRepository repo,
            GuerreiroRepository guerreiroRepository,
            UsuarioGuerreiroRepository usuarioGuerreiroRepository
    ) {
        this.repo = repo;
        this.guerreiroRepository = guerreiroRepository;
        this.usuarioGuerreiroRepository = usuarioGuerreiroRepository;
    }

    public CompraGuerreiroEliteResponse comprarGuerreiroElite(
            Long usuarioId,
            Long guerreiroId,
            int quantidade
    ) {

        if (quantidade <= 0) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Quantidade inválida.",
                    null,
                    null,
                    null
            );
        }

        if (quantidade > QUANTIDADE_MAXIMA) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Quantidade máxima por compra é 5.",
                    null,
                    null,
                    null
            );
        }

        UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Guerreiro guerreiro = guerreiroRepository.findById(guerreiroId)
                .orElseThrow(() -> new RuntimeException("Guerreiro não encontrado"));

        if (!guerreiro.isAtivo()) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Este guerreiro não está disponível.",
                    usuario.getBossCoins(),
                    null,
                    null
            );
        }

        if (usuario.getBossCoins() == null) {
            usuario.setBossCoins(BigDecimal.ZERO);
        }

        UsuarioGuerreiro usuarioGuerreiro = usuarioGuerreiroRepository
                .findByUsuarioAndGuerreiro(usuario, guerreiro)
                .orElseGet(() -> {
                    UsuarioGuerreiro novo = new UsuarioGuerreiro();
                    novo.setUsuario(usuario);
                    novo.setGuerreiro(guerreiro);
                    novo.setQuantidade(0L);
                    novo.setNivel(1L);
                    return novo;
                });

        long quantidadeAtual = usuarioGuerreiro.getQuantidade() != null
                ? usuarioGuerreiro.getQuantidade()
                : 0L;

        if (quantidadeAtual >= LIMITE_GUERREIRO) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Você já possui o limite máximo deste guerreiro.",
                    usuario.getBossCoins(),
                    null,
                    quantidadeAtual
            );
        }

        if (quantidadeAtual + quantidade > LIMITE_GUERREIRO) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Limite máximo deste guerreiro é 100 unidades.",
                    usuario.getBossCoins(),
                    null,
                    quantidadeAtual
            );
        }

        BigDecimal precoBase = BigDecimal.valueOf(guerreiro.getCustoCompra());

        BigDecimal valorTotal = calcularPrecoCompra(
                precoBase,
                quantidadeAtual,
                quantidade
        );

        if (usuario.getBossCoins().compareTo(valorTotal) < 0) {
            return new CompraGuerreiroEliteResponse(
                    false,
                    "Boss Coins insuficientes.",
                    usuario.getBossCoins(),
                    calcularPreco(precoBase, quantidadeAtual),
                    quantidadeAtual
            );
        }

        usuario.setBossCoins(usuario.getBossCoins().subtract(valorTotal));

        long novaQuantidade = quantidadeAtual + quantidade;
        usuarioGuerreiro.setQuantidade(novaQuantidade);

        usuarioGuerreiroRepository.save(usuarioGuerreiro);
        repo.save(usuario);

        BigDecimal precoAtualDepoisCompra = calcularPreco(
                precoBase,
                novaQuantidade
        );

        return new CompraGuerreiroEliteResponse(
                true,
                "Guerreiro comprado com sucesso.",
                usuario.getBossCoins(),
                precoAtualDepoisCompra,
                novaQuantidade
        );
    }

    public BigDecimal calcularPreco(BigDecimal precoBase, long quantidadeAtual) {
        return precoBase
                .multiply(BigDecimal.ONE.add(AUMENTO.multiply(BigDecimal.valueOf(quantidadeAtual))))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPrecoCompra(
            BigDecimal precoBase,
            long quantidadeAtual,
            long quantidadeCompra
    ) {
        if (quantidadeCompra <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        if (quantidadeCompra > QUANTIDADE_MAXIMA) {
            throw new IllegalArgumentException("Quantidade máxima por compra é 5.");
        }

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (long i = 0; i < quantidadeCompra; i++) {
            custoTotal = custoTotal.add(
                    calcularPreco(precoBase, quantidadeAtual + i)
            );
        }

        return custoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public List<GuerreiroEliteResponse> listarGuerreirosLoja(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return guerreiroRepository.findByAtivoTrue()
                .stream()
                .map(guerreiro -> {

                    UsuarioGuerreiro usuarioGuerreiro =
                            usuarioGuerreiroRepository
                                    .findByUsuarioAndGuerreiro(usuario, guerreiro)
                                    .orElse(null);

                    long quantidadeAtual = usuarioGuerreiro != null &&
                            usuarioGuerreiro.getQuantidade() != null
                            ? usuarioGuerreiro.getQuantidade()
                            : 0L;

                    BigDecimal precoAtual = calcularPreco(
                            BigDecimal.valueOf(guerreiro.getCustoCompra()),
                            quantidadeAtual
                    );

                    return new GuerreiroEliteResponse(
                            guerreiro.getId(),
                            guerreiro.getNome(),
                            guerreiro.getTipo(),
                            guerreiro.getDanoBase(),
                            guerreiro.getImagem(),
                            precoAtual,
                            quantidadeAtual,
                            guerreiro.isAtivo()
                    );
                })
                .toList();
    }
    
    public List<GuerreiroUsuarioCardResponse> listarGuerreirosUsuario(Long usuarioId) {

        UsuarioBossBattle usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<GuerreiroUsuarioCardResponse> lista = new ArrayList<>();

        // ============================
        // GUERREIRO TRADICIONAL
        // ============================
        long antigosAtaque = usuario.getGuerreiros();
        long antigosRetaguarda = usuario.getGuerreirosRetaguarda();
        long antigosInventario = usuario.getGuerreirosInventario();

        long totalTradicional =
                antigosAtaque
              + antigosRetaguarda
              + antigosInventario;

        long ataqueBaseTradicional = usuario.getAtaqueBaseGuerreiros();

        if (totalTradicional > 0) {
            lista.add(new GuerreiroUsuarioCardResponse(
                    0L,
                    "Guerreiro Tradicional",
                    "COMUM",
                    totalTradicional,
                    ataqueBaseTradicional,
                    antigosAtaque * ataqueBaseTradicional,
                    "guerreiro-tradicional-card.webp",
                    false
            ));
        }

        // ============================
        // GUERREIROS ELITE
        // ============================
        List<UsuarioGuerreiro> guerreirosElite =
                usuarioGuerreiroRepository.findByUsuario(usuario);

        for (UsuarioGuerreiro ug : guerreirosElite) {

            if (ug.getGuerreiro() == null) continue;

            long quantidadeDesteGuerreiro =
                    ug.getQuantidade() == null ? 0L : ug.getQuantidade();

            if (quantidadeDesteGuerreiro <= 0) continue;

            Long danoBaseObj = ug.getGuerreiro().getDanoBase();

            long danoBase =
                    danoBaseObj == null ? 0L : danoBaseObj;

            lista.add(new GuerreiroUsuarioCardResponse(
                    ug.getGuerreiro().getId(),
                    ug.getGuerreiro().getNome(),
                    ug.getGuerreiro().getTipo(),
                    quantidadeDesteGuerreiro,
                    danoBase,
                    quantidadeDesteGuerreiro * danoBase,
                    ug.getGuerreiro().getImagem(),
                    true
            ));
        }

        return lista;
    }
}

*/