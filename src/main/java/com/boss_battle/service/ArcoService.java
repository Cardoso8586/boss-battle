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

	 
    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;
    
    @Transactional
    public void equiparArco(UsuarioBossBattle usuario) {

        long arcoInventario = usuario.getInventarioArco();
        long machadoDilaceradorAtivo = usuario.getMachadoDilaceradorAtivo();
        long espadaFlanejanteAtiva = usuario.getEspadaFlanejanteAtiva();
        long durabilidadeArco = usuario.getDurabilidadeArco();
        long flechasNaAljava = usuario.getAljava();

        // =========================
        // VALIDAÇÕES
        // =========================
        if (arcoInventario <= 0) {
            throw new RuntimeException("Usuário não possui arco no inventário");
        }

        if (machadoDilaceradorAtivo > 0) {
            throw new RuntimeException("Machado equipado");
        }

        if (espadaFlanejanteAtiva > 0) {
            throw new RuntimeException("Espada equipada");
        }

        if (durabilidadeArco != 0) {
            throw new RuntimeException("Já existe um arco equipado");
        }

        if (flechasNaAljava <= 0) {
            throw new RuntimeException(
                "Não é possível equipar o arco sem flechas na aljava"
            );
        }

        // =========================
        // AÇÃO (SÓ SE PASSAR EM TUDO)
        // =========================
        usuario.setInventarioArco(arcoInventario - 1);
        usuario.setArcoAtivo(1);
        usuario.setDurabilidadeArco(2);

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
            throw new IllegalStateException("Nenhum arco ativo");
        }

        TipoFlecha flechaAtiva = TipoFlecha.fromOrdinal(usuario.getAljavaFlechaAtiva());

        // 🚫 Sem flechas OU flecha inválida
        if (usuario.getAljava() <= 0 || flechaAtiva == null) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
            return 0;
        }

        // 🔻 Consome flecha
        usuario.setAljava(usuario.getAljava() - 1);

        // 🔻 Desgasta arco
        usuario.setDurabilidadeArco(usuario.getDurabilidadeArco() - 1);

        int poder = flechaAtiva.getPoder();

        // 💥 Arco quebrou
        if (usuario.getDurabilidadeArco() <= 0) {
            usuario.setDurabilidadeArco(0);
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
            return poder;
        }

        // 📴 Acabaram as flechas
        if (usuario.getAljava() == 0) {
            usuario.setArcoAtivo(0);
            usuario.setAljavaFlechaAtiva(0);
        }

        return poder;
    }

    /*
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
*/
   
}
