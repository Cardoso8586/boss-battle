package com.boss_battle.service.ativar_equipar;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EquiparGuerreiroService {

    @Autowired
    private UsuarioBossBattleRepository repo;
    
    private UsuarioBossBattle buscarUsuarioComLock(Long usuarioId) {
        return repo.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    public Map<String, Number> equiparGuerreiro(Long usuarioId) {

        UsuarioBossBattle usuario = buscarUsuarioComLock(usuarioId);

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreiros(usuario.getGuerreiros() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);

        repo.saveAndFlush(usuario); // 🔥 FORÇA COMMIT

        return Map.of(
                "sucesso", 1,
                "estoqueGuerreiro", usuario.getGuerreirosInventario(),
                "ativoGuerreiro", usuario.getGuerreiros()
        );
    }

    public Map<String, Number> adicionarGuerreirosRetaguarda(Long usuarioId) {

        UsuarioBossBattle usuario = buscarUsuarioComLock(usuarioId);

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreirosRetaguarda(usuario.getGuerreirosRetaguarda() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);

        repo.saveAndFlush(usuario);

        return Map.of(
                "sucesso", 1,
                "estoqueGuerreiro", usuario.getGuerreirosInventario(),
                "retaguardaGuerreiro", usuario.getGuerreirosRetaguarda()
        );
    }

    public Map<String, Number> retirarGuerreiroAtaque(Long usuarioId) {

        UsuarioBossBattle usuario = buscarUsuarioComLock(usuarioId);

        if (usuario.getGuerreiros() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreiros(usuario.getGuerreiros() - 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 1);

        repo.saveAndFlush(usuario);

        return Map.of(
                "sucesso", 1,
                "estoqueGuerreiro", usuario.getGuerreirosInventario(),
                "ativoGuerreiro", usuario.getGuerreiros()
        );
    }

    public Map<String, Number> retirarGuerreiroRetaguarda(Long usuarioId) {

        UsuarioBossBattle usuario = buscarUsuarioComLock(usuarioId);

        if (usuario.getGuerreirosRetaguarda() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreirosRetaguarda(usuario.getGuerreirosRetaguarda() - 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + 1);

        repo.saveAndFlush(usuario);

        return Map.of(
                "sucesso", 1,
                "estoqueGuerreiro", usuario.getGuerreirosInventario(),
                "retaguardaGuerreiro", usuario.getGuerreirosRetaguarda()
        );
    }

}


    /**

    public Map<String, Number> equiparGuerreiro(Long usuarioId) {
    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreiros(usuario.getGuerreiros() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);
        repo.save(usuario);

        return Map.of(
            "sucesso", 1,
            "estoqueGuerreiro", usuario.getGuerreirosInventario(),
            "ativaGuerreiro", usuario.getGuerreiros()
        );
    }
///
    
    
    //============================================== ADICIONAR GERREIRO NA RETAGUSRADA =======================================
  
    public Map<String, Number> adicionarGuerreirosRetaguarda(Long usuarioId) {
    	UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
    	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getGuerreirosInventario() <= 0) {
            return Map.of("sucesso", 0);
        }

        usuario.setGuerreirosRetaguarda(usuario.getGuerreirosRetaguarda() + 1);
        usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() - 1);

        repo.save(usuario);

        return Map.of(
            "sucesso", 1,
            "estoqueGuerreiro", usuario.getGuerreirosInventario(),
            "ativoGuerreiro", usuario.getGuerreiros()
        );
    }
//=================================================================
    
 // ==============================================
 // RETIRAR GUERREIRO DO ATAQUE
 // ==============================================

 public Map<String, Number> retirarGuerreiroAtaque(Long usuarioId) {
	 UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
		        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	 
     if (usuario.getGuerreiros() <= 0) {
         return Map.of("sucesso", 0);
     }

     usuario.setGuerreiros(usuario.getGuerreiros() - 1);
     usuario.setGuerreirosInventario(
             usuario.getGuerreirosInventario() + 1
     );

     repo.save(usuario);

     return Map.of(
         "sucesso", 1,
         "estoqueGuerreiro", usuario.getGuerreirosInventario(),
         "guerreirosRetaguarda", usuario.getGuerreirosRetaguarda()
     );
 }
//==============================================
//RETIRAR GUERREIRO DO ATAQUE E DA RETAGUARDA
//==============================================

 public Map<String, Number> retirarGuerreiroRetaguarda(Long usuarioId) {
	 UsuarioBossBattle usuario = repo.findByIdForUpdate(usuarioId)
		        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

     if (usuario.getGuerreirosRetaguarda() <= 0) {
         return Map.of("sucesso", 0);
     }

     usuario.setGuerreirosRetaguarda(
             usuario.getGuerreirosRetaguarda() - 1
     );

     usuario.setGuerreirosInventario(
             usuario.getGuerreirosInventario() + 1
     );

     repo.save(usuario);

     return Map.of(
         "sucesso", 1,
         "estoqueGuerreiro", usuario.getGuerreirosInventario(),
         "retaguardaGuerreiro", usuario.getGuerreirosRetaguarda()
     );
 }

*/

