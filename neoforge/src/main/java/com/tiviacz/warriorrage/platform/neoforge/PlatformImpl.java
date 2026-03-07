package com.tiviacz.warriorrage.platform.neoforge;

import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.neoforge.init.ModAttachmentTypes;
import com.tiviacz.warriorrage.neoforge.network.ClientboundSyncRagePacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Consumer;

public class PlatformImpl {
    public static Optional<Rage> getAttachment(Player player) {
        return Optional.ofNullable(player.getData(ModAttachmentTypes.RAGE));
    }

    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {
        getAttachment(player).ifPresent(rage -> {
            rageConsumer.accept(rage);
            player.setData(ModAttachmentTypes.RAGE, rage);
        });
    }

    public static void synchronise(Player player) {
        if(player != null && !player.level().isClientSide) {
            PlatformImpl.getAttachment(player).ifPresent(rage -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ClientboundSyncRagePacket(player.getId(), rage.getCurrentKillCount(), rage.getRemainingRageDuration())));
        }
    }
}