package com.tiviacz.warriorrage.forge.init;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.forge.WarriorRageForge;
import com.tiviacz.warriorrage.network.ClientboundSyncRagePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(WarriorRage.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(WarriorRage.MODID, "1").toString();

    public static SimpleChannel registerNetworkChannel() {
        SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        WarriorRageForge.NETWORK = channel;

        channel.messageBuilder(ClientboundSyncRagePacket.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundSyncRagePacket::decode)
                .encoder((message, buffer) -> message.encode(message, buffer))
                .consumerMainThread((message, contextSupplier) -> {
                    contextSupplier.get().enqueueWork(() -> ClientboundSyncRagePacket.handle(message.entityID(), message.killCount(), message.remainingDuration()));
                    contextSupplier.get().setPacketHandled(true);
                })
                .add();

        return channel;
    }
}