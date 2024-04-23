package com.tiviacz.warriorrage;

import com.tiviacz.warriorrage.config.WarriorRageConfig;
import com.tiviacz.warriorrage.handler.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;

public class WarriorRage implements ModInitializer
{
    public static final String MODID = "warriorrage";

    @Override
    public void onInitialize()
    {
        WarriorRageConfig.setup();
        AttackEntityHandler.registerListener();
    }
}