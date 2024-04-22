package com.tiviacz.warriorrage.capability;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityUtils
{
    public static LazyOptional<IRage> getCapability(final Player player)
    {
        return player.getCapability(RageCapability.RAGE_CAPABILITY, RageCapability.DEFAULT_FACING);
    }

    public static IRage resolveCapability(final Player player)
    {
        return getCapability(player).resolve().get();
    }

    public static void synchronise(Player player)
    {
        CapabilityUtils.getCapability(player)
                .ifPresent(IRage::synchronise);
    }

    public static void synchroniseToOthers(Player player)
    {
        CapabilityUtils.getCapability(player)
                .ifPresent(i -> i.synchroniseToOthers(player));
    }
}
