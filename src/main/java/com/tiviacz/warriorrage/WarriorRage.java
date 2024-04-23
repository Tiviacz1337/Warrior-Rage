package com.tiviacz.warriorrage;

import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.handler.AttackEntityHandler;
import com.tiviacz.warriorrage.network.ModNetwork;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WarriorRage implements ModInitializer
{
    public static final String MODID = "warriorrage";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize()
    {
        WarriorRageConfig.register();
        AttackEntityHandler.registerListener();
        ModNetwork.initServer();
    }
}