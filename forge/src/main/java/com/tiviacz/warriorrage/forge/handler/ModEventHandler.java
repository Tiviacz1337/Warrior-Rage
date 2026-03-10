package com.tiviacz.warriorrage.forge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = WarriorRage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
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
