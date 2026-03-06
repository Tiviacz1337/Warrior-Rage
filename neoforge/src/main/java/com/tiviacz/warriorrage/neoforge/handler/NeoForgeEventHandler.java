package com.tiviacz.warriorrage.neoforge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.RageUtils;
import com.tiviacz.warriorrage.handler.ModEventUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = WarriorRage.MODID)
public class NeoForgeEventHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        RageUtils.targetDie(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void playerAttackMob(AttackEntityEvent event) {
        ModEventUtils.onAttack(event.getEntity(), event.getTarget());
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        RageUtils.tick(event.getEntity());
    }
}