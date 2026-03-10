package com.tiviacz.warriorrage.fabric;

import com.tiviacz.warriorrage.client.RageOverlay;
import com.tiviacz.warriorrage.fabric.init.ModNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class WarriorRageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModNetwork.initClient();
        registerRageOverlay();
    }

    public static void registerRageOverlay() {
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> RageOverlay.renderOverlay(Minecraft.getInstance(), guiGraphics));
    }
}