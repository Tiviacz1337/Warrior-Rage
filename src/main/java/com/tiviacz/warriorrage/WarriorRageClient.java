package com.tiviacz.warriorrage;

import com.tiviacz.warriorrage.network.ModNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WarriorRageClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ModNetwork.initClient();
    }
}