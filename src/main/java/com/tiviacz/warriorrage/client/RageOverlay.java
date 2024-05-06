package com.tiviacz.warriorrage.client;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.WarriorRageConfig;
import com.tiviacz.warriorrage.capability.CapabilityUtils;
import com.tiviacz.warriorrage.capability.IRage;
import com.tiviacz.warriorrage.capability.Rage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = WarriorRage.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RageOverlay
{
    public static void renderRageBar(ForgeGui gui, GuiGraphics guiGraphics, float partialTicks, int screenWidth, int screenHeight)
    {
        if(!WarriorRageConfig.CLIENT.renderRageIcon.get() && !WarriorRageConfig.CLIENT.renderRageBar.get()) return;

        Minecraft mc = gui.getMinecraft();
        Player player = mc.player;
        assert player != null;

        if(mc.gameMode != null && !mc.gameMode.hasExperience()) return;

        if(CapabilityUtils.getCapability(player).isPresent())
        {
            IRage rage = CapabilityUtils.resolveCapability(player);

            if(rage.canStartRage())
            {
                ResourceLocation texture = new ResourceLocation(WarriorRage.MODID, "textures/gui/warrior_rage_bar.png");

                float durationProgress = (float)rage.getRemainingRageDuration() / Rage.DEFAULT_RAGE_DURATION;
                int k = (int)(durationProgress * (183.0F));

                if(WarriorRageConfig.CLIENT.renderRageBar.get())
                {
                    guiGraphics.blit(texture, screenWidth / 2 - 91, screenHeight - 32 + 3, 0, 69, k, 5);
                }

                if(WarriorRageConfig.CLIENT.renderRageIcon.get())
                {
                    guiGraphics.blit(texture, screenWidth / 2 + 94 + WarriorRageConfig.CLIENT.offsetX.get(), screenHeight - 32 + 16 + WarriorRageConfig.CLIENT.offsetY.get(), 0, 0, 14, 14);
                    String s = "" + rage.getCurrentKillCount();
                    int i1 = (screenWidth - gui.getFont().width(s)) / 2 + 115 + WarriorRageConfig.CLIENT.offsetX.get();
                    int j1 = screenHeight - 31 + 18 + WarriorRageConfig.CLIENT.offsetY.get();
                    guiGraphics.drawString(gui.getFont(), s, (float)(i1 + 1), (float)j1, 0, false);
                    guiGraphics.drawString(gui.getFont(), s, (float)(i1 - 1), (float)j1, 0, false);
                    guiGraphics.drawString(gui.getFont(), s, (float)i1, (float)(j1 + 1), 0, false);
                    guiGraphics.drawString(gui.getFont(), s, (float)i1, (float)(j1 - 1), 0, false);
                    guiGraphics.drawString(gui.getFont(), s, (float)i1, (float)j1, 6362132, false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerOverlay(final RegisterGuiOverlaysEvent evt)
    {
        evt.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "warrior_rage_bar", (gui, guiGraphics, partialTick, width, height) ->
        {
            if(!gui.getMinecraft().options.hideGui)
            {
                renderRageBar(gui, guiGraphics, partialTick, width, height);
            }
        });
    }
}