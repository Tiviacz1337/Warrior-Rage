package com.tiviacz.warriorrage.network;

import com.tiviacz.warriorrage.WarriorRage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork
{
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(WarriorRage.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(WarriorRage.MODID, "1").toString();

    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        channel.messageBuilder(SyncRageCapabilityClient.class, 0)
                .decoder(SyncRageCapabilityClient::decode)
                .encoder(SyncRageCapabilityClient::encode)
                .consumerMainThread(SyncRageCapabilityClient::handle)
                .add();

        return channel;
    }
}
