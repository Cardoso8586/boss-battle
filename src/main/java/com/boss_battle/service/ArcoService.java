package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.ResultadoAcao;
import com.boss_battle.enums.ResultadoEquipamento;
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
    public ResultadoEquipamento equiparArco(UsuarioBossBattle usuario) {

        if (usuario.getInventarioArco() <= 0) {
            return ResultadoEquipamento.SEM_ARCO_INVENTARIO;
        }

        if (usuario.getMachadoDilaceradorAtivo() > 0 ||
            usuario.getEspadaFlanejanteAtiva() > 0) {
            return ResultadoEquipamento.OUTRA_ARMA_ATIVA;
        }

        if (usuario.getDurabilidadeArco() > 0) {
            return ResultadoEquipamento.ARCO_JA_EQUIPADO;
        }

        if (usuario.getAljava() <= 0) {
            return ResultadoEquipamento.SEM_FLECHAS;
        }

        usuario.setInventarioArco(usuario.getInventarioArco() - 1);
        usuario.setArcoAtivo(1);
        usuario.setDurabilidadeArco(2);

        usuarioRepository.save(usuario);

        return ResultadoEquipamento.SUCESSO;
    }

    /*
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
*/
   
    public ResultadoAcao reativarArco(UsuarioBossBattle usuario) {

        if (usuario.getArcoAtivo() > 0) {
            return ResultadoAcao.ARCO_JA_ATIVO;
        }

        if (usuario.getAljava() <= 0) {
            return ResultadoAcao.SEM_FLECHAS;
        }

        if (usuario.getDurabilidadeArco() <= 0) {
            return ResultadoAcao.ARCO_QUEBRADO;
        }

        if (usuario.getEspadaFlanejanteAtiva() > 0 ||
            usuario.getMachadoDilaceradorAtivo() > 0) {
            return ResultadoAcao.CONFLITO_ARMA;
        }

        usuario.setArcoAtivo(1);
        usuarioRepository.save(usuario);

        return ResultadoAcao.SUCESSO;
    }

    
    //======================================================================

    public int usarArco(UsuarioBossBattle usuario) {

        // 🚫 Arco inativo
      
        if (usuario.getArcoAtivo() <= 0 || usuario.getDurabilidadeArco() <= 0) {
            usuario.setArcoAtivo(0);
            usuario.setDurabilidadeArco(0);
            usuario.setAljavaFlechaAtiva(0);
            return 0;
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
