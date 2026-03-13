package com.tiviacz.warriorrage.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.platform.Platform;
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
    public double BASE_MULTIPLIER = WarriorRageConfig.SERVER.bonusFlatDamage.get();
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
    private boolean inRage;

    public Rage(int rageDuration, int killCount) {
        this.rageDuration = rageDuration;
        this.killCount = killCount;
        refreshInRageStatus();
    }

    public void refreshInRageStatus() {
        this.inRage = this.killCount >= WarriorRageConfig.SERVER.minimalKillCount.get();
    }

    public boolean isInRage() {
        return this.inRage;
    }

    public int getRemainingRageDuration() {
        return this.rageDuration;
    }

    public int getCurrentKillCount() {
        return this.killCount;
    }

    public void addKill(Player player, int count) {
        this.killCount = Math.min(this.killCount + count, MAX_KILL_COUNT_CAP);
        refreshRageDuration();
        refreshInRageStatus();

        if(isInRage()) {
            addAttributes(player);
        }
    }

    public void decreaseRageDuration(Player player) {
        this.rageDuration = Math.max(0, this.rageDuration - 1);

        if(this.inRage && this.rageDuration == 0) {
            Platform.modifyAttachment(player, synced -> {
                synced.setKillCount(0);
                synced.removeAttributes(player);
            });
        }
    }

    public void refreshRageDuration() {
        this.rageDuration = getDefaultRageDuration();
    }

    public int getDefaultRageDuration() {
        return 20 * WarriorRageConfig.SERVER.rageDuration.get();
    }

    public void setKillCount(int count) {
        this.killCount = Math.min(count, MAX_KILL_COUNT_CAP);
        refreshInRageStatus();
    }

    public void setRageDuration(int timeInTicks) {
        this.rageDuration = timeInTicks;
    }

    public double calculateBonusDamage(int killCount, double multiplier) {
        return WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get() == 0 ? killCount * multiplier : (killCount / WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get()) * multiplier;
    }

    public double calculateBonusPercentageDamage(double currentAmount) {
        double percentage = (killCount / WarriorRageConfig.SERVER.killIntervalBetweenNextBonus.get()) * WarriorRageConfig.SERVER.bonusPercentageDamage.get();
        currentAmount += currentAmount * percentage;
        return currentAmount;
    }

    public void addAttributes(Player player) {
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

    public void removeAttributes(Player player) {
        if(player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(RAGE) != null) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(RAGE);
        }
    }
}