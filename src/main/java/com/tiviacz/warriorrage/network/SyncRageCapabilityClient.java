package com.tiviacz.warriorrage.network;

import com.tiviacz.warriorrage.capability.CapabilityUtils;
import com.tiviacz.warriorrage.capability.IRage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

public class SyncRageCapabilityClient
{
    private final int killCount;
    private final int remainingDuration;
    private final int entityID;

    public SyncRageCapabilityClient(int killCount, int remainingDuration, int entityID)
    {
        this.killCount = killCount;
        this.remainingDuration = remainingDuration;
        this.entityID = entityID;
    }

    public static SyncRageCapabilityClient decode(final FriendlyByteBuf buffer)
    {
        final int killCount = buffer.readInt();
        final int remainingDuration = buffer.readInt();
        final int entityID = buffer.readInt();

        return new SyncRageCapabilityClient(killCount, remainingDuration, entityID);
    }

    public static void encode(final SyncRageCapabilityClient message, final FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.killCount);
        buffer.writeInt(message.remainingDuration);
        buffer.writeInt(message.entityID);
    }

    public static void handle(final SyncRageCapabilityClient message, final CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

            final Player playerEntity = (Player)Minecraft.getInstance().player.level().getEntity(message.entityID);
            IRage cap = CapabilityUtils.getCapability(playerEntity).orElse(null);
            if (cap != null) {
                cap.setKillCount(message.killCount);
                cap.setRageDuration(message.remainingDuration);
            }
        }));

        ctx.setPacketHandled(true);
    }
}