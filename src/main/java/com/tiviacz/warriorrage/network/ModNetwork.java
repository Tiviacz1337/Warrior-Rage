package com.tiviacz.warriorrage.network;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.config.WarriorRageConfigData;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ModNetwork
{
    public static final Identifier UPDATE_CONFIG_ID = new Identifier(WarriorRage.MODID,"update_config");

    public static void initClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_CONFIG_ID, (client, handler, buf, sender) ->
        {
            NbtCompound tag = buf.readNbt();
            client.execute(() ->
            {
                WarriorRage.LOGGER.info("Syncing config from server to client...");
                AutoConfig.getConfigHolder(WarriorRageConfigData.class).setConfig(WarriorRageConfig.readFromNbt(tag));
            });
        });
    }

    public static void initServer()
    {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            //Load default config from file
            WarriorRage.LOGGER.info("Loading config from file...");
            AutoConfig.getConfigHolder(WarriorRageConfigData.class).load();

            //Sync config from server to client if present
            PacketByteBuf data = PacketByteBufs.create();
            data.writeNbt(WarriorRageConfig.writeToNbt());
            ServerPlayNetworking.send(handler.player, UPDATE_CONFIG_ID, data);
        });
    }
}