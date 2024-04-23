package com.tiviacz.warriorrage.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;

public class WarriorRageConfig
{
    public static WarriorRageConfigData getConfig()
    {
        return AutoConfig.getConfigHolder(WarriorRageConfigData.class).getConfig();
    }

    public static void register()
    {
        AutoConfig.register(WarriorRageConfigData.class, JanksonConfigSerializer::new);

        // Listen for when the server is reloading (i.e. /reload), and reload the config
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((s, m) -> AutoConfig.getConfigHolder(WarriorRageConfigData.class).load());
    }

    public static NbtCompound writeToNbt()
    {
        WarriorRageConfigData data = getConfig();
        NbtCompound nbt = new NbtCompound();

        nbt.putInt("minimalKillCount", data.minimalKillCount);
        nbt.putInt("rageDuration", data.rageDuration);
        nbt.putInt("maxKillCountCap", data.maxKillCountCap);
        nbt.putInt("killIntervalBetweenNextBonus", data.killIntervalBetweenNextBonus);
        nbt.putDouble("bonusDamage", data.bonusDamage);
        nbt.putBoolean("enableFireDamage", data.enableFireDamage);

        return nbt;
    }

    public static WarriorRageConfigData readFromNbt(NbtCompound nbt)
    {
        WarriorRageConfigData client = getConfig();
        WarriorRageConfigData data = new WarriorRageConfigData();

        //Overlay
        data.renderRageOverlay = client.renderRageOverlay;
        data.offsetX = client.offsetX;
        data.offsetY = client.offsetY;

        if(nbt == null)
        {
            return data;
        }

        data.minimalKillCount = nbt.getInt("minimalKillCount");
        data.rageDuration = nbt.getInt("rageDuration");
        data.maxKillCountCap = nbt.getInt("maxKillCountCap");
        data.killIntervalBetweenNextBonus = nbt.getInt("killIntervalBetweenNextBonus");
        data.bonusDamage = nbt.getDouble("bonusDamage");
        data.enableFireDamage = nbt.getBoolean("enableFireDamage");

        return data;
    }
}