package com.tiviacz.warriorrage.config;

import com.tiviacz.warriorrage.WarriorRage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = WarriorRage.MODID)
public class WarriorRageConfigData implements ConfigData
{
    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Minimal kill count for rage to enable")
    public int minimalKillCount = 3;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("In Seconds")
    public int rageDuration = 20;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Max kill count for rage bonus damage")
    public int maxKillCountCap = 20;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Number of kills, which will multiply the bonus damage eg. 5 means, every 5 kills attack damage will be increased by bonusDamage value")
    public int killIntervalBetweenNextBonus = 4;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Bonus damage per 4 kills in a row")
    public double bonusDamage = 0.5D;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Enable Fire Damage")
    public boolean enableFireDamage = true;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Required minimal kill count for fire damage to apply")
    public int fireDamageRequiredKillCount = 20;

    @ConfigEntry.Category("client")
    @Comment("Render Rage Bar on experience bar")
    public boolean renderRageBar = true;

    @ConfigEntry.Category("client")
    @Comment("Render Rage Icon next to player's Hotbar")
    public boolean renderRageIcon = true;

    @ConfigEntry.Category("client")
    @Comment("Negative offsets to left side, positive to right")
    public int offsetX = 0;

    @ConfigEntry.Category("client")
    @Comment("Negative offsets to up, positive to down")
    public int offsetY = 0;
}