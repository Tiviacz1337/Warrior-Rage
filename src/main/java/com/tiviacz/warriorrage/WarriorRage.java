package com.tiviacz.warriorrage;

import com.tiviacz.warriorrage.client.RageOverlay;
import com.tiviacz.warriorrage.network.ModNetwork;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("warriorrage")
public class WarriorRage
{
    public static final String MODID = "warriorrage";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = ModNetwork.getNetworkChannel();

    public WarriorRage()
    {
        WarriorRageConfig.register(ModLoadingContext.get());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        RageOverlay.init();
    }
}