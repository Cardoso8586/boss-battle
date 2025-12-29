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

/**
@Modifying
@Query("""
        SELECT b.userId, SUM(b.damage)
        FROM BossDamageLog b
        WHERE b.bossName = :bossName
        GROUP BY b.userId
    """)
    List<Object[]> sumDamageByUser(@Param("bossName") String bossName);
*/
    void deleteByBossName(String bossName);

  //  List<Object[]> sumDamageByUser(@Param("bossName") String bossName);
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
