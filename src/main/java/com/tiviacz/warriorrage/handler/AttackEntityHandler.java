package com.tiviacz.warriorrage.handler;

import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.component.IRage;
import com.tiviacz.warriorrage.component.Rage;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;

public class AttackEntityHandler
{
    public static void registerListener()
    {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
        {
            if(ComponentUtils.RAGE.isProvidedBy(player))
            {
                IRage rage = ComponentUtils.getComponent(player);

                if(rage.getCurrentKillCount() >= Rage.MAX_KILL_COUNT_CAP)
                {
                    if(entity != null)
                    {
                        entity.setOnFireFor(2);
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}