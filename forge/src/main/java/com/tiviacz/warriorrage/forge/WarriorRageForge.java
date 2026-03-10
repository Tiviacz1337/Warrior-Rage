package com.tiviacz.warriorrage.forge;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.forge.init.ModNetwork;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod("warriorrage")
public class WarriorRageForge {
    public static SimpleChannel NETWORK;

    public WarriorRageForge() {
        WarriorRage.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WarriorRageConfig.serverSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WarriorRageConfig.clientSpec);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::registerNetworkChannel);
    }
}