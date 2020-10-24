package me.jonathing.minecraft.packupdater;

import com.google.gson.Gson;
import me.jonathing.minecraft.packupdater.config.ConfigHandler;
import me.jonathing.minecraft.packupdater.config.ConfigVariables;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Mod(
        modid = PackUpdater.MOD_ID,
        name = PackUpdater.MOD_NAME,
        version = PackUpdater.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required:forge@[14.23.5.2847,);"
)
public class PackUpdater
{

    public static final String MOD_ID = "packupdater";
    public static final String MOD_NAME = "Pack Updater";
    public static final String VERSION = "0.1.0";

    private static boolean needsUpdate = false;
    private static String newVersion = "UNKNOWN";

    public static final Logger LOGGER = LogManager.getLogger("Pack Updater");

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static PackUpdater INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        ConfigHandler.loadConfig(event);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        try
        {
            if (ConfigVariables.checkForUpdates && !(ConfigVariables.modpackName.equals("UNKNOWN")
                    || ConfigVariables.versionJsonUrl.equals("UNKNOWN")
                    || ConfigVariables.modpackUrl.equals("UNKNOWN")
                    || ConfigVariables.currentVersion.equals("UNKNOWN")))
            {
                checkForUpdate();
            }
            else
            {
                if (!ConfigVariables.checkForUpdates)
                {
                    LOGGER.info("Pack Updater has been told not to check for updates. Skipping...");
                }
                else
                {
                    LOGGER.error("One or more of the config values in config/packupdater.cfg has not been configured!" +
                            " Pack Updater will not check for updates!");
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Unable to check for updates for the modpack! Is the URL in the config malformed," +
                    " or is the version String in the URL not correctly formatted?");
            e.printStackTrace();
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("Unable to parse the version number correctly!" +
                    "Is the version element from the URL malformed?");
            e.printStackTrace();
        }
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {

    }

    private static void checkForUpdate() throws IOException, NumberFormatException
    {
        String json = readUrl(ConfigVariables.versionJsonUrl);

        Gson gson = new Gson();
        PackVersion versionFromJSON = gson.fromJson(json, PackVersion.class);

        String[] newVersion = versionFromJSON.version.split("\\.");
        String[] currentVersion = ConfigVariables.currentVersion.split("\\.");

        for (int i = 0; i < 3; i++)
        {
            if (Integer.parseInt(newVersion[i]) > Integer.parseInt(currentVersion[i]))
            {
                needsUpdate = true;
                break;
            }
        }

        PackUpdater.newVersion = versionFromJSON.version;
    }

    public static String getNewVersion()
    {
        return newVersion;
    }

    public static boolean doesNeedUpdate()
    {
        return needsUpdate;
    }

    private static class PackVersion
    {
        String version;
    }

    private static String readUrl(String urlString) throws IOException
    {
        BufferedReader reader = null;
        try
        {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }
}
