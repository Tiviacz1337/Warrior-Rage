package com.tiviacz.warriorrage.handler;

import com.tiviacz.warriorrage.component.ComponentUtils;
import com.tiviacz.warriorrage.component.IRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
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

                if(WarriorRageConfig.getConfig().enableFireDamage && rage.getCurrentKillCount() >= WarriorRageConfig.getConfig().fireDamageRequiredKillCount)
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