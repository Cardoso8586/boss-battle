package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.ResultadoAljava;
import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AljavaService {

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Transactional
    public ResultadoAljava colocarFlechasNaAljava(
            UsuarioBossBattle usuario,
            TipoFlecha tipo,
            int quantidade
    ) {

        // ❌ Quantidade inválida
        if (quantidade <= 0) {
            return ResultadoAljava.QUANTIDADE_INVALIDA;
        }

        // 🚫 Outra arma ativa
        if (usuario.getMachadoDilaceradorAtivo() > 0 ||
            usuario.getEspadaFlanejanteAtiva() > 0) {
            return ResultadoAljava.OUTRA_ARMA_ATIVA;
        }

        long aljavaAtual = usuario.getAljava();
        long flechaAtiva = usuario.getAljavaFlechaAtiva();

        // 🔄 Tentativa de trocar tipo com aljava cheia
        if (aljavaAtual > 0 && flechaAtiva != tipo.ordinal()) {
            return ResultadoAljava.TROCA_TIPO_NAO_PERMITIDA;
        }

        // 🏹 Verifica estoque por tipo
        boolean temFlechas = switch (tipo) {
            case FERRO -> usuario.getFlechaFerro() >= quantidade;
            case FOGO -> usuario.getFlechaFogo() >= quantidade;
            case VENENO -> usuario.getFlechaVeneno() >= quantidade;
            case DIAMANTE -> usuario.getFlechaDiamante() >= quantidade;
        };

        if (!temFlechas) {
            return ResultadoAljava.FLECHAS_INSUFICIENTES;
        }

        // 🔻 Consome flechas do estoque
        switch (tipo) {
            case FERRO -> usuario.setFlechaFerro(usuario.getFlechaFerro() - quantidade);
            case FOGO -> usuario.setFlechaFogo(usuario.getFlechaFogo() - quantidade);
            case VENENO -> usuario.setFlechaVeneno(usuario.getFlechaVeneno() - quantidade);
            case DIAMANTE -> usuario.setFlechaDiamante(usuario.getFlechaDiamante() - quantidade);
        }

        // 🔺 Atualiza aljava
        usuario.setAljava(aljavaAtual + quantidade);
        usuario.setAljavaFlechaAtiva(tipo.ordinal());

        // ⚡ Ativa arco automaticamente se possível
        if (usuario.getDurabilidadeArco() > 0 && usuario.getAljava() > 0) {
            usuario.setArcoAtivo(1);
        }

        usuarioRepository.save(usuario);
        return ResultadoAljava.SUCESSO;
    }

    /*
    public void colocarFlechasNaAljava(UsuarioBossBattle usuario, TipoFlecha tipo, int quantidade) {

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        // Bloqueia se outra arma estiver equipada
        if (usuario.getMachadoDilaceradorAtivo() > 0) {
            throw new RuntimeException(
                "Não é possível colocar Flechas enquanto um Machado Dilacerador estiver equipado"
            );
        }

        if (usuario.getEspadaFlanejanteAtiva() > 0) {
            throw new RuntimeException(
                "Não é possível colocar Flechas enquanto uma Espada Flanejante estiver equipada"
            );
        }

        // Verifica se já existe flecha na aljava e for outro tipo
        long aljavaAtual = usuario.getAljava();
        long flechaAtiva = usuario.getAljavaFlechaAtiva();
        if (aljavaAtual > 0 && flechaAtiva != tipo.ordinal()) {
            throw new RuntimeException("Esvazie a aljava antes de trocar o tipo de flecha");
        }

        // Verifica se usuário tem flechas suficientes do tipo escolhido
        switch (tipo) {
            case FERRO -> {
                if (usuario.getFlechaFerro() < quantidade) {
                    throw new RuntimeException("Flechas de ferro insuficientes");
                }
                usuario.setFlechaFerro(usuario.getFlechaFerro() - quantidade);
            }
            case FOGO -> {
                if (usuario.getFlechaFogo() < quantidade) {
                    throw new RuntimeException("Flechas de fogo insuficientes");
                }
                usuario.setFlechaFogo(usuario.getFlechaFogo() - quantidade);
            }
            case VENENO -> {
                if (usuario.getFlechaVeneno() < quantidade) {
                    throw new RuntimeException("Flechas de veneno insuficientes");
                }
                usuario.setFlechaVeneno(usuario.getFlechaVeneno() - quantidade);
            }
            case DIAMANTE -> {
                if (usuario.getFlechaDiamante() < quantidade) {
                    throw new RuntimeException("Flechas de diamante insuficientes");
                }
                usuario.setFlechaDiamante(usuario.getFlechaDiamante() - quantidade);
            }
        }

        // Atualiza aljava
        usuario.setAljava(aljavaAtual + quantidade);
        usuario.setAljavaFlechaAtiva(tipo.ordinal());

        // ⚡ Só ativa arco se houver flechas na aljava
        if (usuario.getArcoAtivo() <= 0 && usuario.getAljava() > 0) {
            usuario.setArcoAtivo(1); // ativa arco automaticamente
        }

        usuarioRepository.save(usuario);
    }
    
    */
}
