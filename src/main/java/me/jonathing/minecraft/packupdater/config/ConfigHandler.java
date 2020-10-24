package me.jonathing.minecraft.packupdater.config;

import me.jonathing.minecraft.packupdater.PackUpdater;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler
{
    public static void loadConfig(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        ConfigVariables.checkForUpdates = config.getBoolean("Check for updates", "Update checker", true,
                ConfigDescriptions.CHECK_FOR_UPDATE);
        ConfigVariables.modpackName = config.getString("Mod pack name", "Mod pack information", "UNKNOWN",
                ConfigDescriptions.MOD_PACK_NAME);
        ConfigVariables.currentVersion = config.getString("Mod pack current version", "Mod pack information", "UNKNOWN",
                ConfigDescriptions.CURRENT_VERSION);
        ConfigVariables.modpackUrl = config.getString("URL to your modpack's CurseForge page", "Mod pack information", "UNKNOWN",
                ConfigDescriptions.MOD_PACK_URL);
        ConfigVariables.versionJsonUrl = config.getString("URL to your version JSON", "Mod pack information", "UNKNOWN",
                ConfigDescriptions.VERSION_JSON_URL);

        config.save();

        PackUpdater.LOGGER.info("Configuration file loaded.");
    }
}
