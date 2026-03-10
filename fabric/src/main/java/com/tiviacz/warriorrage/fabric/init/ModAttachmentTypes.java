package com.tiviacz.warriorrage.fabric.init;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.attachment.Rage;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

public class ModAttachmentTypes {
    public static final AttachmentType<Rage> RAGE = AttachmentRegistry.<Rage>builder().initializer(() -> new Rage(0, 0)).persistent(Rage.CODEC).buildAndRegister(new ResourceLocation(WarriorRage.MODID, "rage"));

    public static void init() {

    }
}