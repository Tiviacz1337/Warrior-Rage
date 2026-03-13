package com.tiviacz.warriorrage.client;

import com.mojang.blaze3d.platform.Window;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class RageOverlay {
    public static final Identifier RAGE_OVERLAY = Identifier.fromNamespaceAndPath(WarriorRage.MODID, "textures/gui/warrior_rage_overlay.png");

    private static float currentAlpha = 0.0F;

    public static void renderOverlay(Minecraft mc, GuiGraphics guiGraphics) {
        Player player = mc.player;
        if(mc.gameMode != null && !mc.gameMode.hasExperience()) return;
        if(player == null) return;

        Window mainWindow = mc.getWindow();
        int screenWidth = mainWindow.getGuiScaledWidth();
        int screenHeight = mainWindow.getGuiScaledHeight();

        Platform.getAttachment(player).ifPresent(rage -> {
            float targetAlpha = 0.0F;
            if(rage.isInRage()) {
                Identifier texture = Identifier.fromNamespaceAndPath(WarriorRage.MODID, "textures/gui/warrior_rage_bar.png");

                float durationProgress = (float)rage.getRemainingRageDuration() / rage.getDefaultRageDuration();
                int k = (int)(durationProgress * (183.0F));

                if(WarriorRageConfig.CLIENT.renderRageBar.get()) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5, 256, 256);
                }

                if(WarriorRageConfig.CLIENT.renderRageIcon.get()) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, screenWidth / 2 + 94 + WarriorRageConfig.CLIENT.offsetX.get(), screenHeight - 32 + 16 + WarriorRageConfig.CLIENT.offsetY.get(), 0, 0, 14, 14, 256, 256);
                    String s = "" + rage.getCurrentKillCount();
                    int i1 = (screenWidth - mc.font.width(s)) / 2 + 115 + WarriorRageConfig.CLIENT.offsetX.get();
                    int j1 = screenHeight - 31 + 18 + WarriorRageConfig.CLIENT.offsetY.get();
                    guiGraphics.enableScissor(i1 - 25, j1 - 25, i1 + 25, j1 + 25);
                    guiGraphics.drawString(mc.font, s, (i1 + 1), j1, 0xFF000000, false);
                    guiGraphics.drawString(mc.font, s, (i1 - 1), j1, 0xFF000000, false);
                    guiGraphics.drawString(mc.font, s, i1, (j1 + 1), 0xFF000000, false);
                    guiGraphics.drawString(mc.font, s, i1, (j1 - 1), 0xFF000000, false);
                    guiGraphics.drawString(mc.font, s, i1, j1, 0xFF611414, false);
                    guiGraphics.disableScissor();
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
                if(WarriorRageConfig.CLIENT.renderRageOverlay.get()) {
                    renderTextureOverlay(guiGraphics, RAGE_OVERLAY, currentAlpha);
                }
            }
        });
    }

    private static void renderTextureOverlay(GuiGraphics guiGraphics, Identifier shaderLocation, float alpha) {
        int i = ARGB.white(alpha);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, shaderLocation, 0, 0, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight(), i);
    }
}