package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.client.RageOverlay;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

@Environment(EnvType.CLIENT)
public class WarriorRageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeoForgeConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.CLIENT, WarriorRageConfig.clientSpec);
        ConfigScreenFactoryRegistry.INSTANCE.register(WarriorRage.MODID, ConfigurationScreen::new);
        registerRageOverlay();
    }

    public static void registerRageOverlay() {
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> RageOverlay.renderOverlay(Minecraft.getInstance(), guiGraphics));
    }
}
