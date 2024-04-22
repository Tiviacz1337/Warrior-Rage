package com.tiviacz.warriorrage.capability;

import com.tiviacz.warriorrage.WarriorRage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RageCapability
{
    public static final Capability<IRage> RAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Direction DEFAULT_FACING = null;

    public static final ResourceLocation ID = new ResourceLocation(WarriorRage.MODID, "warrior_rage");

    public static ICapabilityProvider createProvider(final IRage backpack)
    {
        return new Provider(backpack);
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag>
    {
        final IRage rage;
        final LazyOptional<IRage> optional;

        public Provider(final IRage rage)
        {
            this.rage = rage;
            this.optional = LazyOptional.of(() -> rage);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return RageCapability.RAGE_CAPABILITY.orEmpty(cap, this.optional);
        }

        @Override
        public CompoundTag serializeNBT()
        {
            return rage.saveTag();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt)
        {
            rage.loadTag(nbt);
        }
    }
}
