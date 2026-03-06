package com.tiviacz.warriorrage.fabric.handler;

import com.tiviacz.warriorrage.handler.ModEventUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionResult;

public class AttackEntityHandler {
    public static void registerListener() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ModEventUtils.onAttack(player, entity);
            return InteractionResult.PASS;
        });
    }
}
