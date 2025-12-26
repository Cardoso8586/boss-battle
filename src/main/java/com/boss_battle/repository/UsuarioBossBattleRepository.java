package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boss_battle.model.UsuarioBossBattle;

public interface UsuarioBossBattleRepository extends JpaRepository<UsuarioBossBattle, Long> {
    Optional<UsuarioBossBattle> findByEmail(String email);
    Optional<UsuarioBossBattle> findByUsername(String username);
    List<UsuarioBossBattle> findByReferredBy(Long referredBy);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    UsuarioBossBattle findByEmailOrUsername(String email, String username);
    
  
    @Query("""
    	    select u.id
    	    from UsuarioBossBattle u
    	    where u.guerreirosRetaguarda > 0
    	      and u.recuperacaoRetaguarda > 0
    	      and u.energiaGuerreiros < u.energiaGuerreirosPadrao
    	""")
    	List<Long> findIdsAtivos();
  


}
