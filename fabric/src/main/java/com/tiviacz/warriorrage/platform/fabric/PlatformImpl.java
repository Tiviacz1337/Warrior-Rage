package com.tiviacz.warriorrage.platform.fabric;

import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.fabric.init.ModAttachmentTypes;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class PlatformImpl {
    public static Optional<Rage> getAttachment(Player player) {
        return Optional.ofNullable(player.getAttachedOrCreate(ModAttachmentTypes.RAGE));
    }

    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {
        player.modifyAttached(ModAttachmentTypes.RAGE, rage -> {
            if(rage == null) {
                rage = new Rage(0, 0);
            }
            rageConsumer.accept(rage);
            return rage;
        });
    }

    public static void synchronise(Player player) {

    }
}