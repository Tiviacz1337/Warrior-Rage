package com.tiviacz.warriorrage.neoforge.handler;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.client.RageOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = WarriorRage.MODID, value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_LEVEL, Identifier.fromNamespaceAndPath(WarriorRage.MODID, "rage"), (pGuiGraphics, pPartialTick) -> RageOverlay.renderOverlay(Minecraft.getInstance(), pGuiGraphics));
    }
}