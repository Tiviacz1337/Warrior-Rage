package com.tiviacz.warriorrage.platform.neoforge;

import com.tiviacz.warriorrage.attachment.Rage;
import com.tiviacz.warriorrage.neoforge.init.ModAttachmentTypes;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class PlatformImpl {
    public static Optional<Rage> getAttachment(Player player) {
        return Optional.ofNullable(player.getData(ModAttachmentTypes.RAGE));
    }

    public static void setAttachment(Player player, Rage rage) {
        player.setData(ModAttachmentTypes.RAGE, rage);
    }

    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {
        getAttachment(player).ifPresent(rage -> {
            rageConsumer.accept(rage);
            player.setData(ModAttachmentTypes.RAGE, rage);
        });
    }
}