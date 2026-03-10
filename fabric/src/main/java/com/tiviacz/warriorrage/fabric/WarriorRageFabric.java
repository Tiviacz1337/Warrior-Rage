package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.fabric.handler.AttackEntityHandler;
import com.tiviacz.warriorrage.fabric.init.ModAttachmentTypes;
import com.tiviacz.warriorrage.fabric.init.ModNetwork;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.fml.config.ModConfig;

public class WarriorRageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WarriorRage.init();
        ForgeConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.SERVER, WarriorRageConfig.serverSpec);
        ForgeConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.CLIENT, WarriorRageConfig.clientSpec);
        ModConfigEvents.loading(WarriorRage.MODID).register(config -> {
            if(config.getSpec() == WarriorRageConfig.serverSpec) {
                WarriorRageConfig.reload();
            }
        });
        ModConfigEvents.reloading(WarriorRage.MODID).register(config -> {
            if(config.getSpec() == WarriorRageConfig.serverSpec) {
                WarriorRageConfig.reload();
            }
        });

        ModAttachmentTypes.init();
        AttackEntityHandler.registerListener();
        ModNetwork.initLoginSync();
    }
}