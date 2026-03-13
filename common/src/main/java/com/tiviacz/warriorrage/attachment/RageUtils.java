package com.tiviacz.warriorrage.attachment;

import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.platform.Platform;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public class RageUtils {
    public static void targetDie(LivingEntity target, DamageSource source) {
        if(target instanceof Monster) {
            Entity deathSource = source.getEntity();
            if(deathSource instanceof Player player) {
                Platform.modifyAttachment(player, rage -> rage.addKill(player, 1));
            }
        }
    }

    public static void tick(Player player) {
        Platform.getAttachment(player).ifPresent(rage -> {
            if(rage.isInRage()) {
                addParticlesAroundSelf(ParticleTypes.FLAME, player);
                rage.decreaseRageDuration(player);
            } else if(rage.getCurrentKillCount() > 0) {
                rage.decreaseRageDuration(player);
            }
        });
    }

    public static float hurt(Player attacker, float originalAmount) {
        AtomicReference<Float> amount = new AtomicReference<>(originalAmount);
        Platform.getAttachment(attacker).ifPresent(rage -> {
            if(rage.isInRage()) {
                float newAmount = (float)rage.calculateBonusPercentageDamage(originalAmount);
                amount.set(newAmount);
            }
        });
        return amount.get();
    }

    private static int tick = 0;

    private static void addParticlesAroundSelf(ParticleOptions particleOptions, Player player) {
        if(!WarriorRageConfig.clientSpec.isLoaded()) return;
        if(!WarriorRageConfig.CLIENT.renderFireParticles.get()) return;

        tick++;

        if(tick >= 50) {
            for(int i = 0; i < 5; ++i) {
                double d0 = player.level().random.nextGaussian() * 0.02D;
                double d1 = player.level().random.nextGaussian() * 0.02D;
                double d2 = player.level().random.nextGaussian() * 0.02D;
                player.level().addParticle(particleOptions, player.getRandomX(1.0D), player.getRandomY() + 1.0D, player.getRandomZ(1.0D), d0, d1, d2);
            }
            tick = 0;
        }
    }
}