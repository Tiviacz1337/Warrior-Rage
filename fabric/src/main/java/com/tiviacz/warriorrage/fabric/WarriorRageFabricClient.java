package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.client.RageOverlay;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

@Environment(EnvType.CLIENT)
public class WarriorRageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigRegistry.INSTANCE.register(WarriorRage.MODID, ModConfig.Type.CLIENT, WarriorRageConfig.clientSpec);
        ConfigScreenFactoryRegistry.INSTANCE.register(WarriorRage.MODID, ConfigurationScreen::new);
        registerRageOverlay();
    }

    public static void registerRageOverlay() {
        HudLayerRegistrationCallback.EVENT.register((layeredDrawer -> layeredDrawer.attachLayerAfter(IdentifiedLayer.HOTBAR_AND_BARS, ResourceLocation.fromNamespaceAndPath(WarriorRage.MODID, "rage"), (guiGraphics, deltaTracker) -> RageOverlay.renderOverlay(Minecraft.getInstance(), guiGraphics))));
    }
}