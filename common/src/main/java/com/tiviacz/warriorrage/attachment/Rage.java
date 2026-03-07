package com.tiviacz.warriorrage.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.util.OnHitEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class Rage {
    public static final Identifier RAGE = Identifier.fromNamespaceAndPath(WarriorRage.MODID, "rage_bonus_damage");
    public static final String KILL_COUNT = "KillCount";
    public static final String DURATION = "Duration";
    public int MAX_KILL_COUNT_CAP = WarriorRageConfig.SERVER.maxKillCountCap.get();
    public double BASE_MULTIPLIER = WarriorRageConfig.SERVER.bonusDamage.get();
    public static final MapCodec<Rage> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf(DURATION).forGetter(Rage::getRemainingRageDuration),
            Codec.INT.fieldOf(KILL_COUNT).forGetter(Rage::getCurrentKillCount)
    ).apply(instance, Rage::new));
    public static final Codec<Rage> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<FriendlyByteBuf, Rage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Rage::getRemainingRageDuration,
            ByteBufCodecs.INT, Rage::getCurrentKillCount,
            Rage::new
    );
    private int rageDuration;
    private int killCount;

    public Rage(int rageDuration, int killCount) {
        this.rageDuration = rageDuration;
        this.killCount = killCount;
    }

    public void startRage(Player player) {
        AttributeModifier attackDamageModifier = new AttributeModifier(RAGE, calculateBonusDamage(this.killCount, BASE_MULTIPLIER), AttributeModifier.Operation.ADD_VALUE);
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if(attribute.getModifier(RAGE) != null) {
            if(attribute.getModifier(RAGE).amount() != attackDamageModifier.amount()) {
                attribute.removeModifier(RAGE);
                attribute.addPermanentModifier(attackDamageModifier);
            }
        } else {
            attribute.addPermanentModifier(attackDamageModifier);
        }
        for(OnHitEffect effect : WarriorRageConfig.PLAYER_EFFECTS) {
            if(getCurrentKillCount() == effect.requiredKillCount()) {
                player.addEffect(new MobEffectInstance(effect.mobEffectHolder(), effect.duration(), effect.amplifier(), true, true, true));
            }
        }
    }

    public boolean isInRage() {
        return this.killCount >= WarriorRageConfig.SERVER.minimalKillCount.get() && getRemainingRageDuration() > 0;
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
    }

    public void decreaseRageDuration() {
        if(this.rageDuration > 0) {
            this.rageDuration -= 1;
        }
        if(rageDuration == 0) {
            this.killCount = 0;
        }
    }

    public void removeRageEffects(Player player) {
        if(player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(RAGE) != null) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(RAGE);
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
        } else if(count >= 0) {
            this.killCount = count;
            if(count > 0) {
                refreshRageDuration();
            }
        }
    }

    public void setRageDuration(int timeInTicks) {
        this.rageDuration = timeInTicks;
    }
}