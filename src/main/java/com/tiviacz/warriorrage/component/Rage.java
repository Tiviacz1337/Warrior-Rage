package com.tiviacz.warriorrage.component;

import com.tiviacz.warriorrage.config.WarriorRageConfig;
import dev.onyxstudios.cca.internal.entity.CardinalComponentsEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class Rage implements IRage
{
    private static final UUID RAGE = UUID.fromString("6e982d48-e5e6-11ec-8fea-0242ac120002");
    public static final int DEFAULT_RAGE_DURATION = 20 * WarriorRageConfig.getConfig().rageDuration; //20ticks*10 = 10 seconds
    public static final int MAX_KILL_COUNT_CAP = WarriorRageConfig.getConfig().maxKillCountCap;
    public static final double BASE_MULTIPLIER = WarriorRageConfig.getConfig().bonusDamage;
    private int rageDuration = 0;
    private int killCount = 0;
    private final PlayerEntity playerEntity;

    public Rage(final PlayerEntity playerEntity)
    {
        this.playerEntity = playerEntity;
    }

    @Override
    public void startRage()
    {
        //System.out.println("current kill count is" + this.killCount);
        // System.out.println("remaining rage time" + getRemainingRageDuration());
        // System.out.println("attack damage" + playerEntity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        EntityAttributeModifier attackDamageModifier = new EntityAttributeModifier(RAGE, "RageBonusDamage", calculateBonusDamage(this.killCount, BASE_MULTIPLIER), EntityAttributeModifier.Operation.ADDITION);
        EntityAttributeInstance attribute = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if(attribute.getModifier(RAGE) != null)
        {
            if(attribute.getModifier(RAGE).getValue() != attackDamageModifier.getValue())
            {
                attribute.removeModifier(RAGE);
                attribute.addPersistentModifier(attackDamageModifier);
            }
        }
        else {
            attribute.addPersistentModifier(attackDamageModifier);
        }
    }

    @Override
    public boolean canStartRage()
    {
        return this.killCount >= WarriorRageConfig.getConfig().minimalKillCount && getRemainingRageDuration() > 0;
    }
    public double calculateBonusDamage(int killCount, double multiplier)
    {
        return WarriorRageConfig.getConfig().killIntervalBetweenNextBonus == 0 ? killCount * multiplier : (killCount / WarriorRageConfig.getConfig().killIntervalBetweenNextBonus) * multiplier;
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
        if(playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getModifier(RAGE) != null)
        {
            playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).removeModifier(RAGE);
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
    public void sync()
    {
        syncWith((ServerPlayerEntity)this.playerEntity);
    }

    public void syncToTracking(ServerPlayerEntity player)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(player.getId());
        buf.writeIdentifier(ComponentUtils.RAGE.getId());
        this.writeSyncPacket(buf, player);
        for(ServerPlayerEntity serverPlayer : PlayerLookup.tracking(player))
        {
            ServerPlayNetworking.send(serverPlayer, CardinalComponentsEntity.PACKET_ID, buf);
        }
    }

    public void syncWith(ServerPlayerEntity player)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(player.getId());
        buf.writeIdentifier(ComponentUtils.RAGE.getId());
        this.writeSyncPacket(buf, player);
        ServerPlayNetworking.send(player, CardinalComponentsEntity.PACKET_ID, buf);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.killCount = tag.getInt("KillCount");
        this.rageDuration = tag.getInt("Duration");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("KillCount", this.killCount);
        tag.putInt("Duration", this.rageDuration);
    }
}