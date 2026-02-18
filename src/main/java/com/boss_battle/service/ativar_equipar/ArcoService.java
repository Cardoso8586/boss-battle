package com.boss_battle.service.ativar_equipar;

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
        usuario.setDurabilidadeArco(200);

        usuarioRepository.save(usuario);

        return ResultadoEquipamento.SUCESSO;
    }

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

   
}
