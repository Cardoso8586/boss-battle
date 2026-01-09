package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBoss;

public interface GlobalBossRepository extends JpaRepository<GlobalBoss, Long> {

	 Optional<GlobalBoss> findByName(String name);

	  
    List<GlobalBoss> findByAliveTrue(); 
    
   
    Optional<GlobalBoss> findFirstByAliveTrue();

  
    
}


