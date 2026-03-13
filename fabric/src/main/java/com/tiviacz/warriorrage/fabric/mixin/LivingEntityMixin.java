package com.tiviacz.warriorrage.fabric.mixin;

import com.tiviacz.warriorrage.attachment.RageUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "die")
    private void onDeath(DamageSource source, CallbackInfo ci) {
        RageUtils.targetDie((LivingEntity)(Object)this, source);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageAmount(float amount, DamageSource source) {
        if(source.getEntity() instanceof Player player) {
            return RageUtils.hurt(player, amount);
        }
        return amount;
    }
}