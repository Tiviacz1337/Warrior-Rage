package com.tiviacz.warriorrage.platform.fabric;

import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.fabric.init.ModAttachmentTypes;
import com.tiviacz.warriorrage.network.ClientboundSyncRagePacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlatformImpl {
    public static Optional<Rage> getAttachment(Player player) {
        return Optional.ofNullable(player.getAttachedOrCreate(ModAttachmentTypes.RAGE));
    }

    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {
        player.modifyAttached(ModAttachmentTypes.RAGE, rage -> {
            if(rage == null) {
                rage = new Rage(0, 0);
            }
            rageConsumer.accept(rage);
            return rage;
        });
    }

    public static void synchronise(Player player) {
        if(!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        //Construct packet
        FriendlyByteBuf payload = PacketByteBufs.create();
        AtomicReference<ClientboundSyncRagePacket> packet = new AtomicReference<>();
        PlatformImpl.getAttachment(serverPlayer).ifPresent(rage -> {
            packet.set(new ClientboundSyncRagePacket(serverPlayer.getId(), rage.getCurrentKillCount(), rage.getRemainingRageDuration()));
            packet.get().encode(packet.get(), payload);
        });

        if(packet.get() == null) {
            return;
        }

        //Send to self
        ServerPlayNetworking.send(serverPlayer, packet.get().getPacketId(), payload);

        //Send to tracking
        for(ServerPlayer recipient : PlayerLookup.tracking(serverPlayer)) {
            if(serverPlayer.getId() == player.getId()) {
                continue;
            }
            ServerPlayNetworking.send(recipient, packet.get().getPacketId(), payload);
        }
    }
}