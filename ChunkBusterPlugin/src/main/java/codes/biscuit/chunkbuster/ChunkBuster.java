package codes.biscuit.chunkbuster;

import codes.biscuit.chunkbuster.commands.ChunkBusterCommand;
import codes.biscuit.chunkbuster.events.OtherEvents;
import codes.biscuit.chunkbuster.events.PlayerEvents;
import codes.biscuit.chunkbuster.hooks.HookUtils;
import codes.biscuit.chunkbuster.utils.ConfigValues;
import codes.biscuit.chunkbuster.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkBuster extends JavaPlugin {

    private ConfigValues configValues = new ConfigValues(this);
    private Utils utils = new Utils(this);
    private HookUtils hookUtils;

    @Override
    public void onEnable() {
        hookUtils = new HookUtils(this);
        saveDefaultConfig();
        utils.updateConfig(this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new OtherEvents(this), this);
        ChunkBusterCommand chunkBusterCommand = new ChunkBusterCommand(this);
        getCommand("chunkbuster").setExecutor(chunkBusterCommand);
        getCommand("chunkbuster").setTabCompleter(chunkBusterCommand);
    }

    public Utils getUtils() { return utils; }

    public ConfigValues getConfigValues() { return configValues; }

    public HookUtils getHookUtils() { return hookUtils; }

}
