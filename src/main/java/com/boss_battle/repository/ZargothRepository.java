package com.boss_battle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boss_battle.model.GlobalBossZargoth;

public interface ZargothRepository extends JpaRepository<GlobalBossZargoth, Long> {
	
	 Optional<GlobalBossZargoth> findByName(String name);

	  
	    List<GlobalBossZargoth> findByAliveTrue(); 
	    
	   
	    Optional<GlobalBossZargoth> findFirstByAliveTrue();


}