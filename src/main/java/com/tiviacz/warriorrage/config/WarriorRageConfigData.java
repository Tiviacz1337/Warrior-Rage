package com.tiviacz.warriorrage.config;

import com.tiviacz.warriorrage.WarriorRage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = WarriorRage.MODID)
public class WarriorRageConfigData implements ConfigData
{
    @ConfigEntry.Gui.Tooltip
    @Comment("Minimal kill count for rage to enable")
    public int minimalKillCount = 3;

    @ConfigEntry.Gui.Tooltip
    @Comment("In Seconds")
    public int rageDuration = 20;

    @ConfigEntry.Gui.Tooltip
    @Comment("Max kill count for rage bonus damage")
    public int maxKillCountCap = 20;

    @ConfigEntry.Gui.Tooltip
    @Comment("Number of kills, which will multiply the bonus damage eg. 5 means, every 5 kills attack damage will be increased by bonusDamageMultiplier value")
    public int killIntervalBetweenNextBonus = 4;

    @ConfigEntry.Gui.Tooltip
    @Comment("Bonus damage per 4 kills in a row")
    public double bonusDamageMultiplier = 0.5D;

    @ConfigEntry.Gui.Tooltip
    @Comment("Enable Fire Damage on full kill count")
    public boolean enableFireDamage = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Render Rage Overlay on experience bar")
    public boolean renderRageOverlay = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Negative offsets to left side, positive to right")
    public int offsetX = 0;

    @ConfigEntry.Gui.Tooltip
    @Comment("Negative offsets to up, positive to down")
    public int offsetY = 0;
}