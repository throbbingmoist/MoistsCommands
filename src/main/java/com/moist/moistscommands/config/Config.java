package com.moist.moistscommands.config;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Integer maxHomes = 1;

    public static Integer secondsDelay = 0;

    public static Integer saveInterval = 300;

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

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
