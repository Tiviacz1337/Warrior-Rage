package com.tiviacz.warriorrage.capability;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.WarriorRageConfig;
import com.tiviacz.warriorrage.network.SyncRageCapabilityClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class Rage implements IRage
{
    private static final UUID RAGE = UUID.fromString("6e982d48-e5e6-11ec-8fea-0242ac120002");
    public static final int DEFAULT_RAGE_DURATION = 20 * WarriorRageConfig.SERVER.rageDuration.get(); //20ticks*10 = 10 seconds
    public static final int MAX_KILL_COUNT_CAP = WarriorRageConfig.SERVER.maxKillCountCap.get();
    public static final double BASE_MULTIPLIER = WarriorRageConfig.SERVER.bonusDamage.get();
    private int rageDuration = 0;
    private int killCount = 0;
    private final Player playerEntity;

    public Rage(final Player playerEntity)
    {
        this.playerEntity = playerEntity;
    }

    @Override
    public void startRage()
    {
        //System.out.println("current kill count is" + this.killCount);
        // System.out.println("remaining rage time" + getRemainingRageDuration());
        // System.out.println("attack damage" + playerEntity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        AttributeModifier attackDamageModifier = new AttributeModifier(RAGE, "RageBonusDamage", calculateBonusDamage(this.killCount, BASE_MULTIPLIER), AttributeModifier.Operation.ADDITION);
        AttributeInstance attribute = playerEntity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(attribute.getModifier(RAGE) != null)
        {
            if(attribute.getModifier(RAGE).getAmount() != attackDamageModifier.getAmount())
            {
                attribute.removeModifier(RAGE);
                attribute.addPermanentModifier(attackDamageModifier);
            }
        }
        else {
            attribute.addPermanentModifier(attackDamageModifier);
        }
    }

    @Override
    public boolean canStartRage()
    {
        return this.killCount >= WarriorRageConfig.SERVER.minimalKillCount.get() && getRemainingRageDuration() > 0;
    }
    public double calculateBonusDamage(int killCount, double multiplier)
    {
        return WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get() == 0 ? killCount * multiplier : (killCount / WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get()) * multiplier;
    }

    @Override
    public int getRemainingRageDuration()
    {
        return this.rageDuration;
    }

    @Override
    public int getCurrentKillCount()
    {
        return this.killCount;
    }

    @Override
    public void addKill(int count)
    {
        if(this.killCount + count <= MAX_KILL_COUNT_CAP)
        {
            this.killCount += count;
        }
        refreshRageDuration();
    }

    @Override
    public void decreaseRageDuration()
    {
        if(this.rageDuration > 0)
        {
            this.rageDuration -= 1;
        }
        if(rageDuration == 0)
        {
            this.killCount = 0;
        }
    }

    @Override
    public void removeRageEffects()
    {
        if(playerEntity.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(RAGE) != null)
        {
            playerEntity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(RAGE);
        }
    }

    @Override
    public void refreshRageDuration()
    {
        if(this.rageDuration < DEFAULT_RAGE_DURATION)
        {
            this.rageDuration = DEFAULT_RAGE_DURATION;
        }
    }

    @Override
    public void setKillCount(int count)
    {
        if(count > MAX_KILL_COUNT_CAP)
        {
            this.killCount = MAX_KILL_COUNT_CAP;
            refreshRageDuration();
        }
        else if(count >= 0)
        {
            this.killCount = count;
            if(count > 0)
            {
                refreshRageDuration();
            }
        }
    }

    @Override
    public void setRageDuration(int timeInTicks)
    {
        this.rageDuration = timeInTicks;
    }

    @Override
    public void synchronise()
    {
        if(playerEntity != null && !playerEntity.level.isClientSide)
        {
            ServerPlayer serverPlayer = (ServerPlayer)playerEntity;
            CapabilityUtils.getCapability(serverPlayer).ifPresent(cap -> WarriorRage.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncRageCapabilityClient(this.killCount, this.rageDuration, serverPlayer.getId())));
        }
    }

    @Override
    public void synchroniseToOthers(Player player)
    {
        if(player != null && !player.level.isClientSide)
        {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            //CapabilityUtils.getCapability(serverPlayer).ifPresent(cap -> WarriorRage.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncBackpackCapabilityClient(this.wearable.save(new CompoundTag()), serverPlayer.getId())));
        }
    }

    @Override
    public CompoundTag saveTag()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("KillCount", this.killCount);
        tag.putInt("Duration", this.rageDuration);
        return tag;
    }

    @Override
    public void loadTag(CompoundTag compoundTag)
    {
        this.killCount = compoundTag.getInt("KillCount");
        this.rageDuration = compoundTag.getInt("Duration");
    }
}
