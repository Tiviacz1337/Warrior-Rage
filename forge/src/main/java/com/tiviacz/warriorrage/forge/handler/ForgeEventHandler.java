package com.tiviacz.warriorrage.forge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.attachment.RageUtils;
import com.tiviacz.warriorrage.forge.capability.RageCapability;
import com.tiviacz.warriorrage.handler.ModEventUtils;
import com.tiviacz.warriorrage.platform.forge.PlatformImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WarriorRage.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        RageUtils.targetDie(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void playerAttackMob(AttackEntityEvent event) {
        ModEventUtils.onAttack(event.getEntity(), event.getTarget());
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            RageUtils.tick(event.player);
        }
    }

    @SubscribeEvent
    public static void hurtEntity(LivingHurtEvent event) {
        if(event.getSource().getEntity() instanceof Player player) {
            float newAmount = RageUtils.hurt(player, event.getAmount());
            event.setAmount(newAmount);
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Rage.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            Rage rage = new Rage(0, 0);
            event.addCapability(RageCapability.ID, RageCapability.createProvider(rage));
        }
    }

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