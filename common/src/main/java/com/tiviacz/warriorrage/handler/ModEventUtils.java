package com.tiviacz.warriorrage.handler;

import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.platform.Platform;
import com.tiviacz.warriorrage.util.OnHitEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ModEventUtils {
    public static void onAttack(Player player, Entity target) {
        Platform.getAttachment(player).ifPresent(rage -> {
            if(WarriorRageConfig.SERVER.enableFireDamage.get() && rage.getCurrentKillCount() >= WarriorRageConfig.SERVER.fireDamageRequiredKillCount.get()) {
                target.setRemainingFireTicks(40);
            }
            if(target instanceof LivingEntity livingTarget) {
                for(OnHitEffect effect : WarriorRageConfig.ON_HIT_EFFECTS) {
                    if(rage.getCurrentKillCount() >= effect.requiredKillCount()) {
                        livingTarget.addEffect(new MobEffectInstance(effect.mobEffectHolder(), effect.duration(), effect.amplifier(), true, true, true));
                    }
                }
            }
        });
    }
}