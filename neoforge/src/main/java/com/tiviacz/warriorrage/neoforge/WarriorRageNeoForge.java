package com.tiviacz.warriorrage.neoforge;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.neoforge.init.ModAttachmentTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("warriorrage")
public class WarriorRageNeoForge {
    public WarriorRageNeoForge(IEventBus eventBus, ModContainer modContainer) {
        WarriorRage.init();
        modContainer.registerConfig(ModConfig.Type.SERVER, WarriorRageConfig.serverSpec);
        modContainer.registerConfig(ModConfig.Type.CLIENT, WarriorRageConfig.clientSpec);
        if(FMLEnvironment.getDist() == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ModAttachmentTypes.ATTACHMENT_TYPES.register(eventBus);
    }
}
