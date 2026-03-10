package com.tiviacz.warriorrage.util;

import net.minecraft.world.effect.MobEffect;

public record OnHitEffect(int requiredKillCount, int duration, int amplifier, MobEffect mobEffect) {
}
