package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.fabric.handler.AttackEntityHandler;
import com.tiviacz.warriorrage.fabric.init.ModAttachmentTypes;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class WarriorRageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WarriorRage.init();
        ConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.SERVER, WarriorRageConfig.serverSpec);
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
    }
}