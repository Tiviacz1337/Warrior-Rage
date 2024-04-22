package com.tiviacz.warriorrage;

import com.tiviacz.warriorrage.capability.CapabilityUtils;
import com.tiviacz.warriorrage.capability.IRage;
import com.tiviacz.warriorrage.capability.Rage;
import com.tiviacz.warriorrage.capability.RageCapability;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WarriorRage.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler
{
    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event)
    {
        event.register(IRage.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player player)
        {
            final Rage rage = new Rage(player);
            event.addCapability(RageCapability.ID, RageCapability.createProvider(rage));
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDeathEvent event)
    {
        if(event.getEntity() instanceof Player player)
        {
            if(CapabilityUtils.getCapability(player).isPresent())
            {
                CapabilityUtils.getCapability(player).ifPresent(IRage::removeRageEffects);
                CapabilityUtils.getCapability(player).ifPresent(r -> r.setKillCount(0));
            }
            CapabilityUtils.synchronise(player);
        }
    }

    @SubscribeEvent
    public static void playerClone(final PlayerEvent.Clone event)
    {
        Player oldPlayer = event.getOriginal();
        oldPlayer.revive();

        CapabilityUtils.getCapability(oldPlayer)
                .ifPresent(oldRage -> CapabilityUtils.getCapability(event.getEntity())
                        .ifPresent(newRage -> newRage.setKillCount(oldRage.getCurrentKillCount())));
    }

    @SubscribeEvent
    public static void playerChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event)
    {
        CapabilityUtils.synchronise(event.getEntity());
    }

    @SubscribeEvent
    public static void playerJoin(final PlayerEvent.PlayerLoggedInEvent event)
    {
        CapabilityUtils.synchronise(event.getEntity());
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event)
    {
        if(event.getEntity() instanceof Player player)
        {
            CapabilityUtils.synchronise(player);
        }
    }

    @SubscribeEvent
    public static void playerTracking(final PlayerEvent.StartTracking event)
    {
        if(event.getTarget() instanceof Player && !event.getTarget().level().isClientSide)
        {
            ServerPlayer target = (ServerPlayer)event.getTarget();

           // CapabilityUtils.getCapability(target).ifPresent(c -> WarriorRage.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()),
          //          new SyncBackpackCapabilityClient(CapabilityUtils.getWearingBackpack(target).save(new CompoundTag()), target.getId())));
        }
    }

    @SubscribeEvent
    public static void playerAttackMob(final LivingAttackEvent event)
    {
        if(event.getSource().getEntity() instanceof Player player)
        {
            if(CapabilityUtils.getCapability(player).isPresent())
            {
                IRage rage = CapabilityUtils.getCapability(player).resolve().get();

                if(WarriorRageConfig.SERVER.enableFireDamage.get() && rage.getCurrentKillCount() >= WarriorRageConfig.SERVER.fireDamageRequiredKillCount.get())
                {
                    event.getEntity().setSecondsOnFire(2);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerSlayMob(final LivingDeathEvent event)
    {
        if(event.getSource().getEntity() instanceof Player player)
        {
            LazyOptional<IRage> rage = CapabilityUtils.getCapability(player);

            if(rage.isPresent() && event.getEntity() instanceof Monster)
            {
                rage.ifPresent(r -> r.addKill(1));
                CapabilityUtils.synchronise(player);
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(final TickEvent.PlayerTickEvent event)
    {
        LazyOptional<IRage> rage = CapabilityUtils.getCapability(event.player);

        if(rage.isPresent())
        {
            IRage irage = rage.resolve().get();

            if(irage.canStartRage())
            {
                //event.player.level.addParticle(ParticleTypes.FLAME, event.player.getX(), event.player.getY(), event.player.getZ(), 0.0D, 1.0D, 0.0D);
                addParticlesAroundSelf(ParticleTypes.FLAME, event.player);
                irage.startRage();
                irage.decreaseRageDuration();
            }
            else if(irage.getRemainingRageDuration() > 0 && irage.getCurrentKillCount() < WarriorRageConfig.SERVER.minimalKillCount.get())
            {
                irage.decreaseRageDuration();
            }
            else
            {
                irage.removeRageEffects();
            }
        }
    }

    private static int tick = 0;

    protected static void addParticlesAroundSelf(ParticleOptions particleOptions, Player player)
    {
        tick++;

        if(tick == 50)
        {
            for(int i = 0; i < 5; ++i)
            {
                double d0 = player.level().random.nextGaussian() * 0.02D;
                double d1 = player.level().random.nextGaussian() * 0.02D;
                double d2 = player.level().random.nextGaussian() * 0.02D;
                player.level().addParticle(particleOptions, player.getRandomX(1.0D), player.getRandomY() + 1.0D, player.getRandomZ(1.0D), d0, d1, d2);
            }
            tick = 0;
        }
    }
}