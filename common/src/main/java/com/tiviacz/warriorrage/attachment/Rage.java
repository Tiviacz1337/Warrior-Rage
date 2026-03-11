package com.tiviacz.warriorrage.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.util.OnHitEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class Rage {
    private static final UUID RAGE_UUID = UUID.fromString("6e982d48-e5e6-11ec-8fea-0242ac120002");
    public static final ResourceLocation RAGE = new ResourceLocation(WarriorRage.MODID, "rage_bonus_damage");
    public static final String KILL_COUNT = "KillCount";
    public static final String DURATION = "Duration";
    public int MAX_KILL_COUNT_CAP = WarriorRageConfig.SERVER.maxKillCountCap.get();
    public double BASE_MULTIPLIER = WarriorRageConfig.SERVER.bonusDamage.get();
    public static final Codec<Rage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf(DURATION).forGetter(Rage::getRemainingRageDuration),
            Codec.INT.fieldOf(KILL_COUNT).forGetter(Rage::getCurrentKillCount)).apply(instance, Rage::new));
    private int rageDuration;
    private int killCount;
    private boolean inRage;

    public Rage(int rageDuration, int killCount) {
        this.rageDuration = rageDuration;
        this.killCount = killCount;
        this.inRage = rageDuration > 0 && killCount > WarriorRageConfig.SERVER.minimalKillCount.get();
    }

    public void refreshInRageStatus() {
        this.inRage = this.rageDuration > 0 && this.killCount > WarriorRageConfig.SERVER.minimalKillCount.get();
    }

    public boolean isInRage() {
        return this.inRage;
    }

    public double calculateBonusDamage(int killCount, double multiplier) {
        return WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get() == 0 ? killCount * multiplier : (killCount / WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get()) * multiplier;
    }

    public int getRemainingRageDuration() {
        return this.rageDuration;
    }

    public int getCurrentKillCount() {
        return this.killCount;
    }

    public void addKill(int count) {
        if(this.killCount + count <= MAX_KILL_COUNT_CAP) {
            this.killCount += count;
        }
        refreshRageDuration();
        if(!this.inRage) {
            refreshInRageStatus();
        }
    }

    public void addAttributes(Player player) {
        AttributeModifier attackDamageModifier = new AttributeModifier(RAGE_UUID, RAGE.getPath(), calculateBonusDamage(this.killCount, BASE_MULTIPLIER), AttributeModifier.Operation.ADDITION);
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if(attribute.getModifier(RAGE_UUID) != null) {
            if(attribute.getModifier(RAGE_UUID).getAmount() != attackDamageModifier.getAmount()) {
                attribute.removeModifier(RAGE_UUID);
                attribute.addPermanentModifier(attackDamageModifier);
            }
        } else {
            attribute.addPermanentModifier(attackDamageModifier);
        }
        for(OnHitEffect effect : WarriorRageConfig.PLAYER_EFFECTS) {
            if(getCurrentKillCount() == effect.requiredKillCount()) {
                player.addEffect(new MobEffectInstance(effect.mobEffect(), effect.duration(), effect.amplifier(), true, true, true));
            }
        }
    }

    public void removeAttributes(Player player) {
        if(player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(RAGE_UUID) != null) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(RAGE_UUID);
        }
    }

    public void decreaseRageDuration() {
        if(this.rageDuration > 0) {
            this.rageDuration -= 1;
        }
        if(this.rageDuration == 0) {
            refreshInRageStatus();
        }
    }

    public void refreshRageDuration() {
        if(this.rageDuration < getDefaultRageDuration()) {
            this.rageDuration = getDefaultRageDuration();
        }
    }

    public int getDefaultRageDuration() {
        return 20 * WarriorRageConfig.SERVER.rageDuration.get();
    }

    public void setKillCount(int count) {
        if(count > MAX_KILL_COUNT_CAP) {
            this.killCount = MAX_KILL_COUNT_CAP;
            refreshRageDuration();
        } else {
            this.killCount = count;
            if(count > 0) {
                refreshRageDuration();
            }
            if(!this.inRage) {
                refreshInRageStatus();
            }
        }
    }

    public void setKillCountNoLogic(int count) {
        this.killCount = count;
    }

    public void setRageDuration(int timeInTicks) {
        this.rageDuration = timeInTicks;
    }
}