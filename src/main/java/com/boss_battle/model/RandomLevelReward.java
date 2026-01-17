package com.boss_battle.model;

import java.time.LocalDateTime;

import  com.boss_battle.enums.RewardItem;
import  com.boss_battle.enums.RewardType;
import jakarta.persistence.*;

@Entity
@Table(
    name = "random_level_reward",
    uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)
public class RandomLevelReward {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário dono do preview
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Tipo do prêmio
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    // Item específico (pode ser null)
    @Enumerated(EnumType.STRING)
    private RewardItem rewardItem;

    // Quantidade
    @Column(nullable = false)
    private Integer amount;

    // Imagem pro front
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // Data de criação
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    
    
    // =====================
    // ENUMS
    // =====================

  
    // =====================
    // GETTERS / SETTERS
    // =====================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public RewardItem getRewardItem() {
        return rewardItem;
    }

    public void setRewardItem(RewardItem rewardItem) {
        this.rewardItem = rewardItem;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
