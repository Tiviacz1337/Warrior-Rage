package com.tiviacz.warriorrage.platform.forge;

import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.forge.WarriorRageForge;
import com.tiviacz.warriorrage.forge.capability.RageCapability;
import com.tiviacz.warriorrage.network.ClientboundSyncRagePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Consumer;

public class PlatformImpl {
    public static Optional<Rage> getAttachment(Player player) {
        return player.getCapability(RageCapability.RAGE_CAPABILITY).resolve();
    }

    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {
        getAttachment(player).ifPresent(rage -> {
            rageConsumer.accept(rage);
        });
    }

    public static void synchronise(Player player) {
        if(player != null && !player.level().isClientSide) {
            PlatformImpl.getAttachment(player).ifPresent(rage -> WarriorRageForge.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new ClientboundSyncRagePacket(player.getId(), rage.getCurrentKillCount(), rage.getRemainingRageDuration())));
        }
    }
}