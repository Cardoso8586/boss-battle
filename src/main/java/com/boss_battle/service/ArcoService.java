package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.TipoFlecha;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class ArcoService {

	   private static final long DURABILIDADE_MAX_ARCO = 100;
	   private static final long QUANTIDADE_MAX_ARCO_EQUIPADO = 1;
	   private static final long QUANTIDADE_DESCONTAR_ARCO = 1;
    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;
    public void equiparArco(UsuarioBossBattle usuario) {

        // Verifica se possui arco no inventário
        if (usuario.getInventarioArco() <= 0) {
            throw new RuntimeException("Usuário não possui arco no inventário");
        }

        // Penalidade: tentou equipar sem flecha
        if (usuario.getAljava() <= 0) {
            // Remove arco do inventário ou aplica penalidade
            usuario.setInventarioArco(usuario.getInventarioArco() - QUANTIDADE_DESCONTAR_ARCO);

            // Zera arco ativo e durabilidade
            usuario.setArcoAtivo(0);
            usuario.setDurabilidadeArco(0);

            usuarioRepository.save(usuario);

            throw new RuntimeException(
                "Você tentou equipar o Arco Celestial sem flechas e perdeu 1 arco como penalidade!"
            );
        }

        // Bloqueia se outra arma estiver equipada
        if (usuario.getMachadoDilaceradorAtivo() > 0) {
            throw new RuntimeException(
                "Não é possível equipar o arco enquanto um Machado Dilacerador estiver equipado"
            );
        }

        if (usuario.getEspadaFlanejanteAtiva() > 0) {
            throw new RuntimeException(
                "Não é possível equipar o arco enquanto uma Espada Flanejante estiver equipada"
            );
        }

        // Bloqueia se já existir arco em uso
        if (usuario.getDurabilidadeArco() > 0) {
            throw new RuntimeException(
                "Já existe um arco equipado com durabilidade restante"
            );
        }

        // Equipar normalmente
        usuario.setInventarioArco(usuario.getInventarioArco() - QUANTIDADE_DESCONTAR_ARCO);
        usuario.setArcoAtivo(QUANTIDADE_MAX_ARCO_EQUIPADO);
        usuario.setDurabilidadeArco(DURABILIDADE_MAX_ARCO);

        usuarioRepository.save(usuario);
    }

   
    public void reativarArco(UsuarioBossBattle usuario) {


        // 🚫 Já está ativo
        if (usuario.getArcoAtivo() > 0) {
            throw new RuntimeException("O arco já está ativo");
        }

        // 🚫 Sem flechas
        if (usuario.getAljava() <= 0) {
            throw new RuntimeException("Não é possível reativar o arco sem flechas na aljava");
        }

        // 🚫 Arco inexistente ou quebrado
        if (usuario.getDurabilidadeArco() <= 0) {
            throw new RuntimeException("O arco está quebrado e precisa ser reequipado");
        }

        // 🚫 Conflito de armas
        if (usuario.getEspadaFlanejanteAtiva() > 0 ||
            usuario.getMachadoDilaceradorAtivo() > 0) {
            throw new RuntimeException("Não é possível reativar o arco com outra arma ativa");
        }

        // 🔁 Reativa arco
        usuario.setArcoAtivo(1);

        usuarioRepository.save(usuario);
    }

    
    //======================================================================
   
    public int usarArco(UsuarioBossBattle usuario) {

        // 🚫 Arco inativo
        if (usuario.getArcoAtivo() <= 0) {
            throw new RuntimeException("Nenhum arco ativo");
        }

        // 🚫 Sem flechas
        if (usuario.getAljava() <= 0) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
            usuarioRepository.save(usuario);
            return 0;
        }

        TipoFlecha flechaAtiva = TipoFlecha.fromOrdinal(usuario.getAljavaFlechaAtiva());

        // 🚫 Flecha inválida → apaga arco
        if (flechaAtiva == null) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
            usuarioRepository.save(usuario);
            return 0;
        }

        // 🔻 Consome flecha
        usuario.setAljava(usuario.getAljava() - 1);

        // 🔻 Desgasta arco
        usuario.setDurabilidadeArco(Math.max(usuario.getDurabilidadeArco() - 1, 0));

        // 💥 Arco quebrou
        if (usuario.getDurabilidadeArco() == 0) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
            usuarioRepository.save(usuario);
            return flechaAtiva.getPoder();
        }

        // 📴 Acabaram as flechas
        if (usuario.getAljava() == 0) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
        }

        usuarioRepository.save(usuario);

        // 🔥 Retorna o PODER da flecha ativa
        return flechaAtiva.getPoder();
    }

   
}
