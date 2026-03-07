package com.tiviacz.warriorrage.neoforge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.RageUtils;
import com.tiviacz.warriorrage.handler.ModEventUtils;
import com.tiviacz.warriorrage.platform.neoforge.PlatformImpl;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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

    //NEOFORGE ONLY TO SYNC, MISSING SYNCING METHOD ON ATTACHMENT ;-;

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlatformImpl.synchronise(event.getEntity());
    }

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlatformImpl.synchronise(event.getEntity());
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player player) {
            PlatformImpl.synchronise(player);
        }
    }
}