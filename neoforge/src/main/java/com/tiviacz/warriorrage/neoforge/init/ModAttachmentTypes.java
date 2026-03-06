package com.tiviacz.warriorrage.neoforge.init;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.Rage;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, WarriorRage.MODID);

    public static final Supplier<AttachmentType<Rage>> RAGE = ATTACHMENT_TYPES.register("rage",
            () -> AttachmentType.builder(() -> new Rage(0, 0)).serialize(Rage.CODEC).sync(Rage.STREAM_CODEC).build());
}