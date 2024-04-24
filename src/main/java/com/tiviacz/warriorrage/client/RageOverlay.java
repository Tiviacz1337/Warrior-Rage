package com.tiviacz.warriorrage.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.WarriorRageConfig;
import com.tiviacz.warriorrage.capability.CapabilityUtils;
import com.tiviacz.warriorrage.capability.IRage;
import com.tiviacz.warriorrage.capability.Rage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;

@OnlyIn(Dist.CLIENT)
public class RageOverlay
{
    public static void init()
    {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, "Warrior Rage Bar Overlay", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            Minecraft mc = Minecraft.getInstance();
            if (!mc.player.isRidingJumpable() && !mc.options.hideGui)
            {
                renderRageBar(gui, mStack, partialTicks, screenWidth, screenHeight);
            }
        });
    }

    public static void renderRageBar(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int screenWidth, int screenHeight)
    {
        if(!WarriorRageConfig.CLIENT.renderRageIcon.get() && !WarriorRageConfig.CLIENT.renderRageBar.get()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;

        if(CapabilityUtils.getCapability(player).isPresent())
        {
            IRage rage = CapabilityUtils.resolveCapability(player);

            if(rage.canStartRage())
            {
                ResourceLocation texture = new ResourceLocation(WarriorRage.MODID, "textures/gui/warrior_rage_bar.png");

                float durationProgress = (float)rage.getRemainingRageDuration() / Rage.DEFAULT_RAGE_DURATION;
                int k = (int)(durationProgress * (183.0F));

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, texture);

                if(WarriorRageConfig.CLIENT.renderRageBar.get())
                {
                    gui.blit(mStack, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5);
                }

                if(WarriorRageConfig.CLIENT.renderRageIcon.get())
                {
                    gui.blit(mStack, screenWidth / 2 + 94 + WarriorRageConfig.CLIENT.offsetX.get(), screenHeight - 32 + 16 + WarriorRageConfig.CLIENT.offsetY.get(), 0, 0, 14, 14);
                    String s = "" + rage.getCurrentKillCount();
                    int i1 = (screenWidth - gui.getFont().width(s)) / 2 + 115 + WarriorRageConfig.CLIENT.offsetX.get();
                    int j1 = screenHeight - 31 + 18 + WarriorRageConfig.CLIENT.offsetY.get();
                    gui.getFont().draw(mStack, s, (float)(i1 + 1), (float)j1, 0);
                    gui.getFont().draw(mStack, s, (float)(i1 - 1), (float)j1, 0);
                    gui.getFont().draw(mStack, s, (float)i1, (float)(j1 + 1), 0);
                    gui.getFont().draw(mStack, s, (float)i1, (float)(j1 - 1), 0);
                    gui.getFont().draw(mStack, s, (float)i1, (float)j1, 6362132);
                }
            }
        }
    }
}