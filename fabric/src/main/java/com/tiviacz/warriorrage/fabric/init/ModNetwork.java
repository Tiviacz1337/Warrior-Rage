package com.tiviacz.warriorrage.fabric.init;

import com.tiviacz.warriorrage.network.ClientboundSyncRagePacket;
import com.tiviacz.warriorrage.platform.fabric.PlatformImpl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModNetwork {
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSyncRagePacket.ID, (client, handler, buf, responseSender) -> {
            ClientboundSyncRagePacket packet = ClientboundSyncRagePacket.decode(buf);
            client.execute(() -> ClientboundSyncRagePacket.handle(packet.entityID(), packet.killCount(), packet.remainingDuration()));
        });
    }

    public static void initLoginSync() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlatformImpl.synchronise(handler.getPlayer());
        });
    }
}
