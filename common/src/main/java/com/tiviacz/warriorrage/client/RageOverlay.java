package com.tiviacz.warriorrage.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class RageOverlay {
    public static final ResourceLocation RAGE_OVERLAY = ResourceLocation.fromNamespaceAndPath(WarriorRage.MODID, "textures/gui/warrior_rage_overlay.png");

    private static float currentAlpha = 0.0F;

    public static void renderOverlay(Minecraft mc, GuiGraphics guiGraphics) {
        if(!WarriorRageConfig.CLIENT.renderRageIcon.get() && !WarriorRageConfig.CLIENT.renderRageBar.get()) return;

        Player player = mc.player;
        if(mc.gameMode != null && !mc.gameMode.hasExperience()) return;
        if(player == null) return;

        Window mainWindow = mc.getWindow();
        int screenWidth = mainWindow.getGuiScaledWidth();
        int screenHeight = mainWindow.getGuiScaledHeight();

        Platform.getAttachment(player).ifPresent(rage -> {
            float targetAlpha = 0.0F;
            if(rage.isInRage()) {
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(WarriorRage.MODID, "textures/gui/warrior_rage_bar.png");

                float durationProgress = (float)rage.getRemainingRageDuration() / rage.getDefaultRageDuration();
                int k = (int)(durationProgress * (183.0F));

                if(WarriorRageConfig.CLIENT.renderRageBar.get()) {
                    guiGraphics.blit(texture, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5);
                }

                if(WarriorRageConfig.CLIENT.renderRageIcon.get()) {
                    guiGraphics.blit(texture, screenWidth / 2 + 94 + WarriorRageConfig.CLIENT.offsetX.get(), screenHeight - 32 + 16 + WarriorRageConfig.CLIENT.offsetY.get(), 0, 0, 14, 14);
                    String s = "" + rage.getCurrentKillCount();
                    int i1 = (screenWidth - mc.font.width(s)) / 2 + 115 + WarriorRageConfig.CLIENT.offsetX.get();
                    int j1 = screenHeight - 31 + 18 + WarriorRageConfig.CLIENT.offsetY.get();
                    guiGraphics.drawString(mc.font, s, (i1 + 1), j1, 0, false);
                    guiGraphics.drawString(mc.font, s, (i1 - 1), j1, 0, false);
                    guiGraphics.drawString(mc.font, s, i1, (j1 + 1), 0, false);
                    guiGraphics.drawString(mc.font, s, i1, (j1 - 1), 0, false);
                    guiGraphics.drawString(mc.font, s, i1, j1, 6362132, false);
                }

                int maxKills = rage.MAX_KILL_COUNT_CAP;
                if(maxKills > 0) {
                    targetAlpha = (float)rage.getCurrentKillCount() / (float)maxKills;
                    targetAlpha = (float)Mth.clamp(targetAlpha, 0.0D, WarriorRageConfig.CLIENT.rageOverlayOpacity.get());
                }
            }

            if(currentAlpha < targetAlpha) {
                currentAlpha += (targetAlpha - currentAlpha) * 0.05F;
            }
            else if(currentAlpha > targetAlpha) {
                currentAlpha -= 0.005F;

                if(currentAlpha < targetAlpha) {
                    currentAlpha = targetAlpha;
                }
            }

            if(currentAlpha < 0.001F) {
                currentAlpha = 0.0F;
            }

            if(currentAlpha > 0.0F) {
                renderTextureOverlay(guiGraphics, RAGE_OVERLAY, currentAlpha);
            }
        });
    }

    private static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation shaderLocation, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(shaderLocation, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}