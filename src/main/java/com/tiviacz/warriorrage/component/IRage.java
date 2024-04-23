package com.tiviacz.warriorrage.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.server.network.ServerPlayerEntity;

public interface IRage extends PlayerComponent<Component>, AutoSyncedComponent
{
    void startRage();

    boolean canStartRage();

    int getRemainingRageDuration();

    int getCurrentKillCount();

    void addKill(int count);

    void decreaseRageDuration();

    void removeRageEffects();

    void refreshRageDuration();

    void setKillCount(int count);

    void setRageDuration(int time);

    void sync();

    void syncToTracking(ServerPlayerEntity player);
}