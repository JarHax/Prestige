package com.jarhax.prestige.compat.loot;

import com.google.gson.*;
import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.data.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public class LootConditionPrestige implements LootCondition {
    
    private final String rewardId;
    
    public LootConditionPrestige(String rewardId) {
        
        this.rewardId = rewardId;
    }
    
    @Override
    public boolean testCondition(Random rand, LootContext context) {
        
        final Entity entityPlayer = context.getKillerPlayer();
        if(!(entityPlayer instanceof EntityPlayer)) {
            return false;
        }
        PlayerData data = GlobalPrestigeData.getPlayerData((EntityPlayer) entityPlayer);
        
        return data.hasReward(Prestige.REGISTRY.get(this.rewardId));
    }
    
    public static class Serializer extends LootCondition.Serializer<LootConditionPrestige> {
        
        public Serializer() {
            
            super(new ResourceLocation("required_prestige"), LootConditionPrestige.class);
        }
        
        public void serialize(JsonObject json, LootConditionPrestige value, JsonSerializationContext context) {
            
            json.addProperty("reward_name", Float.valueOf(value.rewardId));
        }
        
        public LootConditionPrestige deserialize(JsonObject json, JsonDeserializationContext context) {
            
            return new LootConditionPrestige(JsonUtils.getString(json, "reward_name"));
        }
    }
}
