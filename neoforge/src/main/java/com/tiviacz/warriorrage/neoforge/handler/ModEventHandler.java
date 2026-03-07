package com.tiviacz.warriorrage.neoforge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.neoforge.network.ClientboundSyncRagePacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WarriorRage.MODID)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar payloadRegistrar = event.registrar(WarriorRage.MODID);
        payloadRegistrar.playToClient(ClientboundSyncRagePacket.TYPE, ClientboundSyncRagePacket.STREAM_CODEC, ClientboundSyncRagePacket::handle);
    }

    @SubscribeEvent
    public static void onModConfigLoad(ModConfigEvent.Loading configEvent) {
        if(configEvent.getConfig().getSpec() == WarriorRageConfig.serverSpec) {
            WarriorRageConfig.reload();
        }
    }

    @SubscribeEvent
    public static void onModConfigReload(ModConfigEvent.Reloading configEvent) {
        if(configEvent.getConfig().getSpec() == WarriorRageConfig.serverSpec) {
            WarriorRageConfig.reload();
        }
    }
}
