package com.boss_battle.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.boss_battle.model.UsuarioBossBattle;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // ✅ import correto

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
  
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UsuarioBossBattle u WHERE u.id = :id")
    Optional<UsuarioBossBattle> findByIdForUpdate(@Param("id") Long id);
    
    
    @Query("""
    		SELECT u FROM UsuarioBossBattle u
    		WHERE u.guerreiros > 0
    		AND u.energiaGuerreiros > 0
    		""")
    		List<UsuarioBossBattle> buscarUsuariosAtivos();

    
 
    // ================= RANKING ATAQUES SEMANAL =================

    // 1) Método com @Query retornando Pageable
    @Query("SELECT u FROM UsuarioBossBattle u ORDER BY u.quantidadeAtaquesSemanal DESC")
    List<UsuarioBossBattle> findTopNByOrderByQuantidadeAtaquesSemanalDesc(Pageable pageable);

    // 2) Método default que aceita o int n
    default List<UsuarioBossBattle> findTopNByOrderByQuantidadeAtaquesSemanalDesc(int n) {
        return findTopNByOrderByQuantidadeAtaquesSemanalDesc(PageRequest.of(0, n));
    }

    // 3) Método para top 5 (se quiser)
    List<UsuarioBossBattle> findTop5ByOrderByQuantidadeAtaquesSemanalDesc();

    // 4) Para listar todos por ordem
    List<UsuarioBossBattle> findAllByOrderByQuantidadeAtaquesSemanalDesc();
    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UsuarioBossBattle u WHERE u.id = :id")
    Optional<UsuarioBossBattle> buscarPorIdComLock(Long id);
    
    @Modifying
    @Transactional
    @Query("""
        UPDATE UsuarioBossBattle u
        SET u.quantidadeSaquesDiario = 0,
            u.dataControleSaque = :hoje
        WHERE u.dataControleSaque IS NULL
           OR u.dataControleSaque < :hoje
    """)
    int resetarSaquesDiarios(@Param("hoje") LocalDate hoje);
    
    List<UsuarioBossBattle> findByEnergiaGuerreirosGreaterThan(Long energia);
    
    
}//---> fim repo
