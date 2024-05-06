package com.tiviacz.warriorrage.network;

import com.tiviacz.warriorrage.WarriorRage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;


public class ModNetwork
{
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(WarriorRage.MODID, "network");

    public static SimpleChannel registerNetworkChannel()
    {
        final SimpleChannel channel = ChannelBuilder.named(CHANNEL_NAME)
                .acceptedVersions(Channel.VersionTest.exact(1))
                .networkProtocolVersion(1)
                .simpleChannel();

        WarriorRage.NETWORK = channel;

        channel.messageBuilder(SyncRageCapabilityClient.class, 0)
                .decoder(SyncRageCapabilityClient::decode)
                .encoder(SyncRageCapabilityClient::encode)
                .consumerMainThread(SyncRageCapabilityClient::handle)
                .add();

        return channel;
    }
}
