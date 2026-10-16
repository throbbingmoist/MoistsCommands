package com.moist.moistscommands;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.moist.moistscommands.commands.BackCommand;
import com.moist.moistscommands.commands.SpawnCommand;
import com.moist.moistscommands.commands.homes.DelhomeCommand;
import com.moist.moistscommands.commands.homes.HomeCommand;
import com.moist.moistscommands.commands.homes.SethomeCommand;
import com.moist.moistscommands.config.Config;
import com.moist.moistscommands.helper.DelayedEventHelper;
import com.moist.moistscommands.helper.PlayerLocation;
import com.moist.moistscommands.proxy.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(
    modid = MoistsCommands.MODID,
    name = "Moist's Commands",
    guiFactory = "com.moist.moistscommands.helper.GUIFactory",
    acceptedMinecraftVersions = "[1.7.10]")
public class MoistsCommands {

    public static final String MODID = "moistscommands";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static File worldDir;
    public static File locationsFile;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SidedProxy(
        clientSide = "com.moist.moistscommands.proxy.ClientProxy",
        serverSide = "com.moist.moistscommands.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        FMLCommonHandler.instance()
            .bus()
            .register(new DelayedEventHelper());
        scheduler.scheduleAtFixedRate(
            MoistsCommands::saveLocationFile,
            Config.saveInterval.longValue(),
            Config.saveInterval.longValue(),
            TimeUnit.SECONDS);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        worldDir = DimensionManager.getCurrentSaveRootDirectory();
        locationsFile = new File(worldDir, "locations.dat");
        PlayerLocation.readLocationFile(locationsFile);

        event.registerServerCommand(new BackCommand());
        event.registerServerCommand(new HomeCommand());
        event.registerServerCommand(new SethomeCommand());
        event.registerServerCommand(new DelhomeCommand());
        event.registerServerCommand(new SpawnCommand());
        proxy.serverStarting(event);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityPlayer player) {
            PlayerLocation.updateLastLocation(player);
        }
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        PlayerLocation.saveLocationFile(locationsFile);
    }

    public static void saveLocationFile() {
        PlayerLocation.saveLocationFile(locationsFile);
    }

    public static void saveConfig() {
        Config.synchronizeConfiguration(Config.configuration);
    }

}
