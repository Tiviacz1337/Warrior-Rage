package com.tiviacz.warriorrage.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.component.IRage;
import com.tiviacz.warriorrage.component.Rage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RageOverlay extends Screen
{
    public MinecraftClient mc;
    public Window mainWindow;

    public RageOverlay()
    {
        super(new LiteralText(""));

        this.mc = MinecraftClient.getInstance();
        this.mainWindow = MinecraftClient.getInstance().getWindow();
    }

    public void renderOverlay(MatrixStack matrices)
    {
        PlayerEntity player = mc.player;

        int screenWidth = mainWindow.getScaledWidth();
        int screenHeight = mainWindow.getScaledHeight();

        if(ComponentUtils.RAGE.isProvidedBy(player))
        {
            IRage rage = ComponentUtils.getComponent(player);

            if(rage.canStartRage())
            {
                Identifier texture = new Identifier(WarriorRage.MODID, "textures/gui/warrior_rage_bar.png");

                float durationProgress = (float)rage.getRemainingRageDuration() / Rage.DEFAULT_RAGE_DURATION;
                int k = (int)(durationProgress * (183.0F));

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, texture);

                drawTexture(matrices, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5);
                drawTexture(matrices, screenWidth / 2 + 94 + WarriorRageConfig.getConfig().offsetX, screenHeight - 32 + 16 + WarriorRageConfig.getConfig().offsetY, 0, 0, 14, 14);
                //mStack.scale(1.0F / 6, 1.0F / 6, 1.0F / 6);

                String s = "" + rage.getCurrentKillCount();
                int i1 = (screenWidth - mc.textRenderer.getWidth(s)) / 2 + 115 + WarriorRageConfig.getConfig().offsetX;
                int j1 = screenHeight - 31 + 18 + WarriorRageConfig.getConfig().offsetY;
                mc.textRenderer.draw(matrices, s, (float)(i1 + 1), (float)j1, 0);
                mc.textRenderer.draw(matrices, s, (float)(i1 - 1), (float)j1, 0);
                mc.textRenderer.draw(matrices, s, (float)i1, (float)(j1 + 1), 0);
                mc.textRenderer.draw(matrices, s, (float)i1, (float)(j1 - 1), 0);
                mc.textRenderer.draw(matrices, s, (float)i1, (float)j1, 6362132);
            }
            // float durationProgress = (float)CapabilityUtils.getCapability(player).resolve().get().getRemainingRageDuration() / Rage.DEFAULT_RAGE_DURATION;
            // int k = (int)(durationProgress * (183.0F / 2));
            // gui.blit(mStack, (screenWidth / 2 - 91) + 91, screenHeight - 32 + 3, 0, 69, k, 5);
            //gui.blit(mStack, (screenWidth / 2 - 91), screenHeight - 32 + 3, -(91 - k), 74, 91, 5);
            //gui.blit(mStack, screenWidth / 2 + 91, screenHeight - 32 + 3, 0, 69, k + 91, 5);
        }
    }
}