package com.moist.moistscommands.proxy;

import net.minecraftforge.common.config.Configuration;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.Tags;
import com.moist.moistscommands.config.Config;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(new Configuration(event.getSuggestedConfigurationFile()));

        MoistsCommands.LOG.info("MoistsCommands version " + Tags.VERSION + "loaded successfully!");
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
