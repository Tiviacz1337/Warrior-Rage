package com.tiviacz.warriorrage.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public record OnHitEffect(int requiredKillCount, int duration, int amplifier, Holder<MobEffect> mobEffectHolder) {
}
