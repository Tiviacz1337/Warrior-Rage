package com.tiviacz.warriorrage.config;

import com.tiviacz.warriorrage.network.ModNetwork;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;

public class WarriorRageConfig
{
    public static int minimalKillCount;
    public static int rageDuration;
    public static int maxKillCountCap;
    public static int killIntervalBetweenNextBonus;
    public static double bonusDamageMultiplier;
    public static boolean enableFireDamage;

    //CLIENT
    public static boolean renderRageOverlay;
    public static int offsetX;
    public static int offsetY;

    public static void setup() {
        WarriorRageConfigData data =
                AutoConfig.register(WarriorRageConfigData.class, JanksonConfigSerializer::new).getConfig();

        bake(null, data); // To load the initial config into the data.  Not sure why the config is not loaded until server start after this
        ServerLifecycleEvents.SERVER_STARTED.register(server -> bake(server, data));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, b) -> bake(server, data));
    }

    public static void bake(MinecraftServer server, WarriorRageConfigData data)
    {
        minimalKillCount = data.minimalKillCount;
        rageDuration = data.rageDuration;
        maxKillCountCap = data.maxKillCountCap;
        killIntervalBetweenNextBonus = data.killIntervalBetweenNextBonus;
        bonusDamageMultiplier = data.bonusDamageMultiplier;
        enableFireDamage = data.enableFireDamage;

        if(server == null)
        {
            //Client
            renderRageOverlay = data.renderRageOverlay;
            offsetX = data.offsetX;
            offsetY = data.offsetY;
        }
        else
        {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(toNbt());
            server.getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, ModNetwork.UPDATE_CONFIG_ID, buf));
        }
    }

    public static NbtCompound toNbt()
    {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("minimalKillCount", minimalKillCount);
        nbt.putInt("rageDuration", rageDuration);
        nbt.putInt("maxKillCountCap", maxKillCountCap);
        nbt.putInt("killIntervalBetweenNextBonus", killIntervalBetweenNextBonus);
        nbt.putDouble("bonusDamageMultiplier", bonusDamageMultiplier);
        nbt.putBoolean("enableFireDamage", enableFireDamage);

        return nbt;
    }

    public static void fromNbt(NbtCompound nbt)
    {
        minimalKillCount = nbt.getInt("minimalKillCount");
        rageDuration = nbt.getInt("rageDuration");
        maxKillCountCap = nbt.getInt("maxKillCountCap");
        killIntervalBetweenNextBonus = nbt.getInt("killIntervalBetweenNextBonus");
        bonusDamageMultiplier = nbt.getDouble("bonusDamageMultiplier");
        enableFireDamage = nbt.getBoolean("enableFireDamage");
    }
}