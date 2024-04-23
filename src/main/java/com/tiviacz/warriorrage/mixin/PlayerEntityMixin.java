package com.tiviacz.warriorrage.mixin;

import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.component.IRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(at = @At(value = "HEAD"), method = "dropInventory")
    private void onDeath(CallbackInfo info)
    {
        if(this instanceof Object)
        {
            if((Object)this instanceof PlayerEntity player)
            {
                if(ComponentUtils.RAGE.isProvidedBy(player))
                {
                    ComponentUtils.getComponent(player).removeRageEffects();
                    ComponentUtils.getComponent(player).setKillCount(0);
                }
                ComponentUtils.sync(player);
            }
        }
    }

    private static int tick = 0;

    @Inject(at = @At(value = "TAIL"), method = "tick")
    private void rageTick(CallbackInfo info)
    {
        if(this instanceof Object)
        {
            if((Object) this instanceof PlayerEntity player)
            {
                if(ComponentUtils.RAGE.isProvidedBy(player))
                {
                    IRage rage = ComponentUtils.getComponent(player);

                    if(rage.canStartRage())
                    {
                        addParticlesAroundSelf(ParticleTypes.FLAME, player);
                        rage.startRage();
                        rage.decreaseRageDuration();
                    }
                    else if(rage.getRemainingRageDuration() > 0 && rage.getCurrentKillCount() < WarriorRageConfig.minimalKillCount)
                    {
                        rage.decreaseRageDuration();
                    }
                    else
                    {
                        rage.removeRageEffects();
                    }
                }
            }
        }
    }

    private static void addParticlesAroundSelf(ParticleEffect particleEffect, PlayerEntity player)
    {
        tick++;

        if(tick == 50)
        {
            for(int i = 0; i < 5; ++i) {
                double d0 = player.world.random.nextGaussian() * 0.02D;
                double d1 = player.world.random.nextGaussian() * 0.02D;
                double d2 = player.world.random.nextGaussian() * 0.02D;
                player.world.addParticle(particleEffect, player.getParticleX(1.0D), player.getRandomBodyY() + 1.0D, player.getParticleZ(1.0D), d0, d1, d2);
            }
            tick = 0;
        }
    }
}