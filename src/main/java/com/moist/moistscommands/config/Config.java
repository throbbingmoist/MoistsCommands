package com.moist.moistscommands.config;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Integer maxHomes = 1;

    public static Integer secondsDelay = 0;

    public static Integer saveInterval = 300;

    public static Integer homeCooldown = 0;

    public static Integer spawnCooldown = 0;

    public static Integer backCooldown = 0;

    public static Configuration configuration;

    public static void synchronizeConfiguration(Configuration configFile) {
        configuration = configFile;
        maxHomes = configuration
            .getInt("max_homes", Configuration.CATEGORY_GENERAL, maxHomes, 0, 999, "Maximum homes per player");

        secondsDelay = configuration
            .getInt("delay", Configuration.CATEGORY_GENERAL, secondsDelay, 0, 60, "How long should teleporting take?");

        saveInterval = configuration.setCategoryRequiresMcRestart("save_data", true)
            .getInt(
                "save_interval",
                "save_data",
                saveInterval,
                10,
                Integer.MAX_VALUE,
                "How often, in seconds, should a world's homes save?");

        homeCooldown = configuration.getInt(
            "home_cooldown",
            "cooldowns",
            homeCooldown,
            0,
            Integer.MAX_VALUE,
            "How long, in seconds, to deny repeated /home commands for?");

        spawnCooldown = configuration.getInt(
            "spawn_cooldown",
            "cooldowns",
            spawnCooldown,
            0,
            Integer.MAX_VALUE,
            "How long, in seconds, to deny repeated /spawn commands for?");

        backCooldown = configuration.getInt(
            "back_cooldown",
            "cooldowns",
            backCooldown,
            0,
            Integer.MAX_VALUE,
            "How long, in seconds, to deny repeated /back commands for?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
