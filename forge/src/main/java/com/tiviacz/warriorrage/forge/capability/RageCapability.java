package com.tiviacz.warriorrage.forge.capability;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.Rage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RageCapability {
    public static final Capability<Rage> RAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation ID = new ResourceLocation(WarriorRage.MODID, "rage");

    public static ICapabilityProvider createProvider(Rage rage) {
        return new Provider(rage);
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        final Rage rage;
        final LazyOptional<Rage> optional;

        public Provider(Rage rage) {
            this.rage = rage;
            this.optional = LazyOptional.of(() -> rage);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return RageCapability.RAGE_CAPABILITY.orEmpty(cap, this.optional);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt(Rage.DURATION, rage.getRemainingRageDuration());
            tag.putInt(Rage.KILL_COUNT, rage.getCurrentKillCount());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            int duration = nbt.getInt(Rage.DURATION);
            int killCount = nbt.getInt(Rage.KILL_COUNT);
            rage.setRageDuration(duration);
            rage.setKillCountNoLogic(killCount);
            rage.refreshInRageStatus();
        }
    }
}