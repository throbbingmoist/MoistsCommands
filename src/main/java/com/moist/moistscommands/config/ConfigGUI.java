package com.moist.moistscommands.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.moist.moistscommands.MoistsCommands;

import cpw.mods.fml.client.config.GuiConfig;

public class ConfigGUI extends GuiConfig {

    public ConfigGUI(GuiScreen parentScreen) {
        super(
            parentScreen,
            new ConfigElement<>(Config.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
            MoistsCommands.MODID,
            "moistscommands.config",
            false,
            false,
            GuiConfig.getAbridgedConfigPath(Config.configuration.toString()));
        this.configElements.add(new ConfigElement<>(Config.configuration.getCategory("save_data")));
    }

    @Override
    public void onGuiClosed() {
        if (this.entryList != null) {
            this.entryList.saveConfigElements();
        }
        MoistsCommands.saveConfig();
        // MoistsCommands.LOG.info("yooo we saving config. {} {}", Config.maxHomes, Config.secondsDelay);
    }
}
