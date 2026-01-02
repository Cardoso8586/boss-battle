package com.boss_battle.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boss_battle.dto.BossDamageLogDTO;
import com.boss_battle.model.BossDamageLog;
public interface BossDamageLogRepository
extends JpaRepository<BossDamageLog, Long> {

List<BossDamageLog> findByBossName(String bossName);


long countByBossName(String bossName);


    void deleteByBossName(String bossName);
    @Query(value = """
    	    SELECT 
    	        b.user_id,
    	        b.user_name,
    	        COUNT(b.id) AS ataques,
    	        SUM(b.damage) AS danoTotal
    	    FROM boss_damage_log b
    	    GROUP BY b.user_id, b.user_name
    	    ORDER BY SUM(b.damage) DESC
    	""", nativeQuery = true)
    	List<Object[]> top10Geral(Pageable pageable);
    	
//=====================================================================================
    	@Query(value = """
    		    SELECT COUNT(*) + 1
    		    FROM (
    		        SELECT user_id, SUM(damage) total
    		        FROM boss_damage_log
    		        GROUP BY user_id
    		    ) t
    		    WHERE t.total >
    		        (SELECT SUM(damage) FROM boss_damage_log WHERE user_id = :userId)
    		""", nativeQuery = true)
    		Integer buscarPosicaoUsuario(@Param("userId") Long userId);
    	
//=====================================================================================
    	
    	@Query(value = """
    		    select sum(damage)
    		    from boss_damage_log
    		    where user_id = :userId
    		""", nativeQuery = true)
    		Integer buscarResumoUsuario(@Param("userId") Long userId);

    
  //  List<Object[]> sumDamageByUser(@Param("bossName") String bossName);
    	
//===================================================================================    	
    @Query("""
            SELECT new com.boss_battle.dto.BossDamageLogDTO(
                b.userName,
                b.damage
            )
            FROM BossDamageLog b
            ORDER BY b.id DESC
        """)
        List<BossDamageLogDTO> ultimosAtaques(Pageable pageable);
}
