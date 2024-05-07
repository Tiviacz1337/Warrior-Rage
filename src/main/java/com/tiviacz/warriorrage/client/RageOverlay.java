package com.tiviacz.warriorrage.client;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.component.IRage;
import com.tiviacz.warriorrage.component.Rage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RageOverlay extends Screen
{
    public MinecraftClient mc;
    public Window mainWindow;

    public RageOverlay()
    {
        super(Text.literal(""));

        this.mc = MinecraftClient.getInstance();
        this.mainWindow = MinecraftClient.getInstance().getWindow();
    }

    public void renderOverlay(DrawContext drawContext)
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

                if(WarriorRageConfig.getConfig().renderRageBar)
                {
                    drawContext.drawTexture(texture, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5);
                }

                if(WarriorRageConfig.getConfig().renderRageIcon)
                {
                    drawContext.drawTexture(texture, screenWidth / 2 + 94 + WarriorRageConfig.getConfig().offsetX, screenHeight - 32 + 16 + WarriorRageConfig.getConfig().offsetY, 0, 0, 14, 14);
                    String s = "" + rage.getCurrentKillCount();
                    int i1 = (screenWidth - mc.textRenderer.getWidth(s)) / 2 + 115 + WarriorRageConfig.getConfig().offsetX;
                    int j1 = screenHeight - 31 + 18 + WarriorRageConfig.getConfig().offsetY;
                    drawContext.drawText(mc.textRenderer, s, (i1 + 1), j1, 0, false);
                    drawContext.drawText(mc.textRenderer, s, (i1 - 1), j1, 0, false);
                    drawContext.drawText(mc.textRenderer, s, i1, (j1 + 1), 0, false);
                    drawContext.drawText(mc.textRenderer, s, i1, (j1 - 1), 0, false);
                    drawContext.drawText(mc.textRenderer, s, i1, j1, 6362132, false);
                }
            }
        }
    }
}