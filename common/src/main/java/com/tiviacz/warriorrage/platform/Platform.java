package com.tiviacz.warriorrage.platform;

import com.tiviacz.warriorrage.attachment.Rage;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class Platform {
    @ExpectPlatform
    public static Optional<Rage> getAttachment(Player player) {
        return null;
    }

    @ExpectPlatform
    public static void modifyAttachment(Player player, Consumer<Rage> rageConsumer) {

    }
}