package com.boss_battle.dto;

import com.boss_battle.model.RandomLevelReward;
import com.boss_battle.model.RandomLevelReward.RewardItem;
import com.boss_battle.model.RandomLevelReward.RewardType;

public class RandomLevelRewardDTO {

    private RewardType rewardType;
    private RewardItem rewardItem;
    private long amount;
    private String imageUrl;

    // 🔄 Mapper simples
    public static RandomLevelRewardDTO fromEntity(RandomLevelReward reward) {
        RandomLevelRewardDTO dto = new RandomLevelRewardDTO();
        dto.rewardType = reward.getRewardType();
        dto.rewardItem = reward.getRewardItem();
        dto.amount = reward.getAmount();
        dto.imageUrl = reward.getImageUrl();
        return dto;
    }

    // GETTERS
    public RewardType getRewardType() {
        return rewardType;
    }

    public RewardItem getRewardItem() {
        return rewardItem;
    }

    public long getAmount() {
        return amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

