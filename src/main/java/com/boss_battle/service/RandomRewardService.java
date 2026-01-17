package com.boss_battle.service;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.boss_battle.model.RandomLevelReward;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.RandomLevelRewardRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import  com.boss_battle.enums.RewardItem;
import  com.boss_battle.enums.RewardType;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RandomRewardService {

    private final RandomLevelRewardRepository rewardRepo;
    private final UsuarioBossBattleRepository usuarioRepo;
   
    private final Random random = new Random();

    public RandomRewardService(
            RandomLevelRewardRepository rewardRepo,
            UsuarioBossBattleRepository usuarioRepo
            
    ) {
        this.rewardRepo = rewardRepo;
        this.usuarioRepo = usuarioRepo;
      
    }

    // 👀 PREVIEW — FRONT
    public RandomLevelReward getPreview(Long usuarioId) {
        return rewardRepo.findByUserId(usuarioId)
                .orElseGet(() -> generateAndSave(usuarioId));
    }

 // ⬆️ CHAMAR QUANDO SOBE DE NÍVEL
    
    
    public void onLevelUp(Long usuarioId) {

        RandomLevelReward reward = rewardRepo.findByUserId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Reward não encontrado"));

        UsuarioBossBattle usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        switch (reward.getRewardType()) {

            case CURRENCY -> {
                BigDecimal atual = usuario.getBossCoins() != null
                        ? usuario.getBossCoins()
                        : BigDecimal.ZERO;
                usuario.setBossCoins(atual.add(BigDecimal.valueOf(reward.getAmount())));
            }

            case CONSUMABLE -> {
                if (reward.getRewardItem() == RewardItem.POCAO_VIGOR) {
                    usuario.setPocaoVigor(usuario.getPocaoVigor() + reward.getAmount());
                }
                //ESPADA FLANEJANTE
                if (reward.getRewardItem() == RewardItem.ESPADA_FLANEJANTE) {
                    usuario.setEspadaFlanejante(usuario.getEspadaFlanejante() + reward.getAmount());
                }
                
              //MACHADO DILACERADOR
                if (reward.getRewardItem() == RewardItem.MACHADO_DILACERADOR) {
                    usuario.setMachadoDilacerador(usuario.getMachadoDilacerador() + reward.getAmount());
                }
            }

            case GUERREIRO -> {
                if (reward.getRewardItem() == RewardItem.GUERREIRO_BASICO) {
                    usuario.setGuerreirosInventario(usuario.getGuerreirosInventario() + reward.getAmount());
                }
            }

            case SPECIAL -> {
                if (reward.getRewardItem() == RewardItem.ATAQUE_SPECIAL) {
                    usuario.setAtaqueBase(usuario.getAtaqueBase() + reward.getAmount());
                }
            }

            case PROGRESSION -> {
                if (reward.getRewardItem() == RewardItem.EXP) {
                	 usuario.setExp(usuario.getExp() + reward.getAmount());
                }
            }
        }

        usuarioRepo.save(usuario);

        // consome o reward atual
        rewardRepo.delete(reward);

        // gera o próximo preview
        generateAndSave(usuarioId);
    }



    // 🎲 GERADOR DE PRÊMIO
    @Transactional
    private RandomLevelReward generateAndSave(Long userId) {

        // Remove qualquer reward anterior para o usuário
        rewardRepo.findByUserId(userId).ifPresent(rewardRepo::delete);

        RandomLevelReward reward = new RandomLevelReward();
        reward.setUserId(userId);

        int roll = random.nextInt(100); // 0 a 99
        if (roll < 10) { // Guerreiro
            reward.setRewardType(RewardType.GUERREIRO);
            reward.setRewardItem(RewardItem.GUERREIRO_BASICO);
            reward.setAmount(1);
            reward.setImageUrl("icones/guerreiro_padrao.webp");

        } else if (roll < 20) { // Poção
            reward.setRewardType(RewardType.CONSUMABLE);
            reward.setRewardItem(RewardItem.POCAO_VIGOR);
            reward.setAmount(1);
            reward.setImageUrl("icones/pocao_vigor.webp");

        } else if (roll < 30) { // ESPADA FLANEJANTE
            reward.setRewardType(RewardType.CONSUMABLE);
            reward.setRewardItem(RewardItem.ESPADA_FLANEJANTE);
            reward.setAmount(1);
            reward.setImageUrl("icones/espada_flanejante.webp");

        }
        
        else if (roll < 40) { // MACHADO DILACERADOR
            reward.setRewardType(RewardType.CONSUMABLE);
            reward.setRewardItem(RewardItem.MACHADO_DILACERADOR);
            reward.setAmount(1);
            reward.setImageUrl("icones/machado_dilacerador.webp");

        }
        
        
        else if (roll < 50) { // Ataque especial
            reward.setRewardType(RewardType.SPECIAL);
            reward.setRewardItem(RewardItem.ATAQUE_SPECIAL);
            reward.setAmount(5);
            reward.setImageUrl("icones/ataque_especial.webp");

        } else if (roll < 70) { // Moedas
            reward.setRewardType(RewardType.CURRENCY);
            reward.setRewardItem(RewardItem.BOSS_COIN);
            reward.setAmount(200 + random.nextInt(500));
            reward.setImageUrl("icones/boss_coin.webp");

        } else { // EXP
            reward.setRewardType(RewardType.PROGRESSION);
            reward.setRewardItem(RewardItem.EXP);
            reward.setAmount(250);
            reward.setImageUrl("icones/exp.webp");
        }

        System.out.println("Roll: " + roll + " -> Reward: " + reward.getRewardItem());

        return rewardRepo.save(reward);
    }

    /**
    private RandomLevelReward generateAndSave(Long userId) {

        RandomLevelReward reward = new RandomLevelReward();
        reward.setUserId(userId);

        int roll = random.nextInt(100); // 0 a 99
        if (roll < 10) { // ⚔️ Guerreiro - 10%
            reward.setRewardType(RewardType.GUERREIRO);
            reward.setRewardItem(RewardItem.GUERREIRO_BASICO);
            reward.setAmount(1);
            reward.setImageUrl("icones/guerreiro.webp");

        } else if (roll < 20) { // 🧪 Poção - 10%
            reward.setRewardType(RewardType.CONSUMABLE);
            reward.setRewardItem(RewardItem.POCAO_VIGOR);
            reward.setAmount(1);
            reward.setImageUrl("icones/pocao_vigor.webp");

        } else if (roll < 40) { // ✨ Ataque especial - 20%
            reward.setRewardType(RewardType.SPECIAL);
            reward.setRewardItem(RewardItem.ATAQUE_SPECIAL);
            reward.setAmount(5);
            reward.setImageUrl("icones/ataque_especial.webp");

        } else if (roll < 70) { // 💰 Moedas - 30%
            reward.setRewardType(RewardType.CURRENCY);
            reward.setRewardItem(RewardItem.BOSS_COIN);
            reward.setAmount(200 + random.nextInt(500));
            reward.setImageUrl("icones/boss_coin.webp");

        } else { // ⭐ EXP - 30%
            reward.setRewardType(RewardType.PROGRESSION);
            reward.setRewardItem(RewardItem.EXP);
            reward.setAmount(250);
            reward.setImageUrl("icones/exp.webp");
        }

        // Para debug: mostrar chance e roll
        System.out.println("Roll: " + roll + " -> Reward: " + reward.getRewardItem());

        return rewardRepo.save(reward);
    }
*/
}

