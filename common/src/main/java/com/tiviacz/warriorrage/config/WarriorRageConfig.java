package com.tiviacz.warriorrage.config;

import com.google.common.collect.Sets;
import com.tiviacz.warriorrage.util.OnHitEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarriorRageConfig {
    private static final String ON_HIT_EFFECT_MATCHER = "([a-z0-9_.-]+:[a-z0-9_/.-]+),\\s*(100|[1-9][0-9]?),\\s*([1-9][0-9]{0,2}),\\s*(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    public static class Server {
        public final ModConfigSpec.IntValue minimalKillCount;
        public final ModConfigSpec.IntValue rageDuration;
        public final ModConfigSpec.IntValue maxKillCountCap;
        public final ModConfigSpec.IntValue killIntervalBetweenNextBonus;
        public final ModConfigSpec.DoubleValue bonusFlatDamage;
        public final ModConfigSpec.DoubleValue bonusPercentageDamage;
        public final ModConfigSpec.BooleanValue enableFireDamage;
        public final ModConfigSpec.IntValue fireDamageRequiredKillCount;
        public final ModConfigSpec.ConfigValue<List<? extends String>> onHitEffects;
        public final ModConfigSpec.ConfigValue<List<? extends String>> playerEffects;

        Server(ModConfigSpec.Builder builder) {
            builder.comment("Server config settings")
                    .push("server");

            minimalKillCount = builder
                    .comment("Minimal kill count for rage to enable")
                    .defineInRange("minimalKillCount", 3, 0, 1000);

            rageDuration = builder
                    .comment("In Seconds")
                    .defineInRange("rageDuration", 20, 0, 1000);

            maxKillCountCap = builder
                    .comment("Max kill count for rage bonus damage")
                    .defineInRange("maxKillCountCap", 20, 0, 1000);

            killIntervalBetweenNextBonus = builder
                    .comment("Number of kills, which will multiply the bonus damage eg. 5 means, every 5 kills attack damage will be increased by bonusDamage value")
                    .defineInRange("killIntervalBetweenNextBonus", 4, 1, 1000);

            bonusFlatDamage = builder
                    .comment("Flat bonus damage per consecutive kills, for example 4 kills = +0.5 damage, 8 kills = +1.0 damage")
                    .defineInRange("bonusFlatDamage", 0.5D, 0.0D, 10.0D);

            bonusPercentageDamage = builder
                    .comment("Bonus percentage damage per kill consecutive kills (including weapon damage), for example 4 kills = +5% damage, 8 kills +10% damage")
                    .defineInRange("bonusPercentageDamage", 0.0D, 0.0D, 10.0D);

            enableFireDamage = builder
                    .comment("Enable Fire Damage")
                    .define("enableFireDamage", true);

            fireDamageRequiredKillCount = builder
                    .comment("Required minimal kill count for fire damage to apply")
                    .defineInRange("fireDamageRequiredKillCount", 20, 0, 1000);

            onHitEffects = builder
                    .comment("List of effects that are being applied on hit, use the following syntax 'registryEffectName, requiredKillCount (1-100), duration in ticks (1-999)[20ticks = 1 second], amplifier (0-255)' - example: 'minecraft:slowness, 5, 40, 1'")
                    .worldRestart()
                    .defineList("onHitEffects", new ArrayList<>(), () -> "", mapping -> ((String)mapping).matches(ON_HIT_EFFECT_MATCHER));

            playerEffects = builder
                    .comment("List of effects that are being applied to player, use the following syntax 'registryEffectName, requiredKillCount (1-100), duration in ticks (1-999)[20ticks = 1 second], amplifier (0-255)' - example: 'minecraft:regeneration, 15, 40, 1'")
                    .worldRestart()
                    .defineList("playerEffects", new ArrayList<>(), () -> "", mapping -> ((String)mapping).matches(ON_HIT_EFFECT_MATCHER));

            builder.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue renderRageOverlay;
        public final ModConfigSpec.DoubleValue rageOverlayOpacity;
        public final ModConfigSpec.BooleanValue renderRageBar;
        public final ModConfigSpec.BooleanValue renderRageIcon;
        public final ModConfigSpec.BooleanValue renderFireParticles;
        public final ModConfigSpec.IntValue offsetX;
        public final ModConfigSpec.IntValue offsetY;

        Client(ModConfigSpec.Builder builder) {
            builder.comment("Client-only settings")
                    .push("client");

            renderRageOverlay = builder
                    .comment("Render Freeze like rage overlay")
                    .define("renderRageOverlay", true);

            rageOverlayOpacity = builder
                    .comment("Final opacity of rage overlay")
                    .defineInRange("rageOverlayOpacity", 0.5D, 0.0D, 1.0D);

            renderRageBar = builder
                    .comment("Render Rage Bar on experience bar")
                    .define("renderRageBar", true);

            renderRageIcon = builder
                    .comment("Render Rage Icon next to player's Hotbar")
                    .define("renderRageIcon", true);

            renderFireParticles = builder
                    .comment("Render fire particles around player when rage is active")
                    .define("renderFireParticles", true);

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
    public static final ModConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    //CLIENT
    public static final ModConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void reload() {
        if(!serverSpec.isLoaded()) {
            return;
        }

        ON_HIT_EFFECTS.clear();
        PLAYER_EFFECTS.clear();
        loadEffectsFromConfig(WarriorRageConfig.SERVER.onHitEffects.get(), ON_HIT_EFFECTS);
        loadEffectsFromConfig(WarriorRageConfig.SERVER.playerEffects.get(), PLAYER_EFFECTS);
    }

    public static void loadEffectsFromConfig(List<? extends String> configList, Set<OnHitEffect> targetList) {
        targetList.clear();
        for(String configEntry : configList) {
            String[] onHitEffect = configEntry.replace(" ", "").split(",");
            ResourceLocation res = ResourceLocation.tryParse(onHitEffect[0]);
            int requiredKillCount = Integer.parseInt(onHitEffect[1]);
            int duration = Integer.parseInt(onHitEffect[2]);
            int amplifier = Integer.parseInt(onHitEffect[3]);
            BuiltInRegistries.MOB_EFFECT.getHolder(res).ifPresent(holder -> targetList.add(new OnHitEffect(requiredKillCount, duration, amplifier, holder)));
        }
    }

    public static final Set<OnHitEffect> ON_HIT_EFFECTS = Sets.newHashSet();
    public static final Set<OnHitEffect> PLAYER_EFFECTS = Sets.newHashSet();
}