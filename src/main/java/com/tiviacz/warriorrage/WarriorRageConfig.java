package com.tiviacz.warriorrage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = WarriorRage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarriorRageConfig
{
    public static class Server
    {
        public final ForgeConfigSpec.IntValue minimalKillCount;
        public final ForgeConfigSpec.IntValue rageDuration;
        public final ForgeConfigSpec.IntValue maxKillCountCap;
        public final ForgeConfigSpec.IntValue killIntervalBetweenNextBonus;
        public final ForgeConfigSpec.DoubleValue bonusDamage;
        public final ForgeConfigSpec.BooleanValue enableFireDamage;
        public final ForgeConfigSpec.IntValue fireDamageRequiredKillCount;

        Server(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Server config settings")
                    .push("server");

            minimalKillCount = builder
                    .comment("Minimal kill count for rage to enable")
                    .translation("warriorrage.config.server.minimalKillCount")
                    .defineInRange("minimalKillCount", 3, 0, 1000);

            rageDuration = builder
                    .comment("In Seconds")
                    .translation("warriorrage.config.server.rageDuration")
                    .defineInRange("rageDuration", 20, 0, 1000);

            maxKillCountCap = builder
                    .comment("Max kill count for rage bonus damage")
                    .translation("warriorrage.config.server.maxKillCountCap")
                    .defineInRange("maxKillCountCap", 20, 0, 1000);

            killIntervalBetweenNextBonus = builder
                    .comment("Number of kills, which will multiply the bonus damage eg. 5 means, every 5 kills attack damage will be increased by bonusDamage value")
                    .translation("warriorrage.config.server.killIntervalBetweenNextBonus")
                    .defineInRange("killIntervalBetweenNextBonus", 4, 1, 1000);

            bonusDamage = builder
                    .comment("Bonus damage per 4 kills in a row")
                    .translation("warriorrage.config.server.bonusDamage")
                    .defineInRange("bonusDamage", 0.5D, 0.01D, 10.0D);

            enableFireDamage = builder
                    .comment("Enable Fire Damage")
                    .translation("warriorrage.config.server.enableFireDamage")
                    .define("enableFireDamage", true);

            fireDamageRequiredKillCount = builder
                    .comment("Required minimal kill count for fire damage to apply")
                    .translation("warriorrage.config.server.fireDamageRequiredKillCount")
                    .defineInRange("fireDamageRequiredKillCount", 20, 0, 1000);

            builder.pop();
        }
    }

    public static class Client
    {
        public final ForgeConfigSpec.BooleanValue renderRageOverlay;
        public final ForgeConfigSpec.IntValue offsetX;
        public final ForgeConfigSpec.IntValue offsetY;

        Client(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client-only settings")
                    .push("client");

            renderRageOverlay = builder
                    .comment("Render Rage Overlay on experience bar")
                    .translation("warriorrage.config.client.renderRageOverlay")
                    .define("renderRageOverlay", true);

            offsetX = builder
                    .comment("Negative offsets to left side, positive to right")
                    .defineInRange("offsetX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            offsetY = builder
                    .comment("Negative offsets to up, positive to down")
                    .defineInRange("offsetY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    //COMMON
    private static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    //CLIENT
    private static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    //Register
    public static void register(final ModLoadingContext context)
    {
        context.registerConfig(ModConfig.Type.SERVER, serverSpec);
        context.registerConfig(ModConfig.Type.CLIENT, clientSpec);
    }
}
