package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.fabric.handler.AttackEntityHandler;
import com.tiviacz.warriorrage.fabric.init.ModAttachmentTypes;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class WarriorRageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WarriorRage.init();
        NeoForgeConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.SERVER, WarriorRageConfig.serverSpec);
        NeoForgeModConfigEvents.loading(WarriorRage.MODID).register(config -> {
            if(config.getSpec() == WarriorRageConfig.serverSpec) {
                WarriorRageConfig.reload();
            }
        });
        NeoForgeModConfigEvents.reloading(WarriorRage.MODID).register(config -> {
            if(config.getSpec() == WarriorRageConfig.serverSpec) {
                WarriorRageConfig.reload();
            }
        });

        ModAttachmentTypes.init();
        AttackEntityHandler.registerListener();
    }
}