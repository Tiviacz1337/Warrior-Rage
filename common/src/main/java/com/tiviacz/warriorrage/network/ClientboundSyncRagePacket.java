package com.tiviacz.warriorrage.network;

import com.tiviacz.warriorrage.WarriorRage;
import com.tiviacz.warriorrage.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundSyncRagePacket(int entityID, int killCount, int remainingDuration) implements IPacket<ClientboundSyncRagePacket> {
    public static final ResourceLocation ID = new ResourceLocation(WarriorRage.MODID, "sync_rage");

    public static ClientboundSyncRagePacket decode(FriendlyByteBuf buffer) {
        int entityID = buffer.readInt();
        int killCount = buffer.readInt();
        int remainingDuration = buffer.readInt();
        return new ClientboundSyncRagePacket(entityID, killCount, remainingDuration);
    }

    @Override
    public void encode(ClientboundSyncRagePacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityID);
        buffer.writeInt(message.killCount);
        buffer.writeInt(message.remainingDuration);
    }

    public static void handle(int entityID, int killCount, int remainingDuration) {
        Player player = (Player)Minecraft.getInstance().player.level().getEntity(entityID);
        Platform.getAttachment(player).ifPresent(rage -> {
            rage.setKillCount(killCount);
            rage.setRageDuration(remainingDuration);
        });
    }

    @Override
    public ResourceLocation getPacketId() {
        return ID;
    }
}