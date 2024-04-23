package com.tiviacz.warriorrage.component;

import com.tiviacz.warriorrage.WarriorRage;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ComponentUtils implements EntityComponentInitializer
{
    public static final ComponentKey<IRage> RAGE = ComponentRegistry.getOrCreate(new Identifier(WarriorRage.MODID, "warriorrage"), IRage.class);

    public static IRage getComponent(PlayerEntity player)
    {
        return RAGE.get(player);
    }

    public static void sync(PlayerEntity player)
    {
        if(player instanceof ServerPlayerEntity)
        {
            getComponent(player).sync();
        }
    }

    public static void syncToTracking(PlayerEntity player)
    {
        if(player instanceof ServerPlayerEntity serverPlayer)
        {
            getComponent(player).syncToTracking(serverPlayer);
        }
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
    {
        registry.registerForPlayers(RAGE, Rage::new, RespawnCopyStrategy.CHARACTER);
    }
}