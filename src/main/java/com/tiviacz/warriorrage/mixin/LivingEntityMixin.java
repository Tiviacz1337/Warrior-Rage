package com.tiviacz.warriorrage.mixin;

import com.tiviacz.warriorrage.component.ComponentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(at = @At(value = "HEAD"), method = "onDeath", cancellable = true)
    private void onDeath(DamageSource source, CallbackInfo ci)
    {
        if(this instanceof Object)
        {
            if((Object)this instanceof Monster)
            {
                Entity deathSource = source.getAttacker();

                if(deathSource instanceof PlayerEntity player)
                {
                    if(ComponentUtils.RAGE.isProvidedBy(player))
                    {
                        ComponentUtils.getComponent(player).addKill(1);
                        ComponentUtils.sync(player);
                    }
                }
            }
        }
    }
}