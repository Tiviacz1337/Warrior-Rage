package com.tiviacz.warriorrage.mixin;

import com.tiviacz.warriorrage.client.RageOverlay;
import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
    @Inject(at = @At(value = "TAIL"), method = "renderExperienceBar")
    private void renderOverlay(MatrixStack matrices, int x, CallbackInfo ci)
    {
        if(WarriorRageConfig.getConfig().renderRageOverlay && !MinecraftClient.getInstance().player.isRiding() && !MinecraftClient.getInstance().options.hudHidden)
        {
            if(ComponentUtils.RAGE.isProvidedBy(MinecraftClient.getInstance().player))
            {
                RageOverlay gui = new RageOverlay();
                gui.renderOverlay(matrices);
            }
        }
    }
}