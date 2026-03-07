package com.tiviacz.warriorrage.neoforge.network;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.platform.neoforge.PlatformImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundSyncRagePacket(int entityID, int killCount, int remainingDuration) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(WarriorRage.MODID, "sync_rage");
    public static final Type<ClientboundSyncRagePacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncRagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientboundSyncRagePacket::entityID,
            ByteBufCodecs.INT, ClientboundSyncRagePacket::killCount,
            ByteBufCodecs.INT, ClientboundSyncRagePacket::remainingDuration,
            ClientboundSyncRagePacket::new
    );

    public static void handle(ClientboundSyncRagePacket message, IPayloadContext ctx) {
        if(ctx.flow().isClientbound()) {
            ctx.enqueueWork(() -> {
                Player player = (Player)Minecraft.getInstance().player.level().getEntity(message.entityID());
                PlatformImpl.getAttachment(player).ifPresent(rage -> {
                    rage.setKillCount(message.killCount());
                    rage.setRageDuration(message.remainingDuration());
                });
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}