package xyz.biscut.chunkbuster;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.biscut.chunkbuster.commands.ChunkBusterCommand;
import xyz.biscut.chunkbuster.events.OtherEvents;
import xyz.biscut.chunkbuster.events.PlayerEvents;
import xyz.biscut.chunkbuster.utils.HookType;
import xyz.biscut.chunkbuster.utils.HookUtils;
import xyz.biscut.chunkbuster.utils.ConfigValues;
import xyz.biscut.chunkbuster.utils.Utils;

public class ChunkBuster extends JavaPlugin {

    private ConfigValues configValues = new ConfigValues(this);
    private Utils utils = new Utils(this);
    private HookUtils hookUtils;

    @Override
    public void onEnable() {
        HookType hookType;
        if (getServer().getPluginManager().getPlugin("MassiveCore") != null &&
                getServer().getPluginManager().getPlugin("Factions") != null) {
            getLogger().info("Hooked into MassiveCore Factions");
            hookType = HookType.MCOREFACTIONS;
        } else if (getServer().getPluginManager().getPlugin("Factions") != null) {
            getLogger().info("Hooked into FactionsUUID/SavageFactions");
            hookType = HookType.FACTIONSUUID;
        } else {
            getLogger().info("No factions plugin found, attempting to hook into WorldGuard");
            if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                hookType = HookType.WORLDGUARD;
                getLogger().info("Hooked into WorldGuard");
            } else {
                getLogger().severe("No factions or worldguard found, disabling...");
                getPluginLoader().disablePlugin(this);
                return;
            }
        }
        hookUtils = new HookUtils(hookType);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        utils.updateConfig(this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new OtherEvents(this), this);
        getCommand("chunkbuster").setExecutor(new ChunkBusterCommand(this));
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Utils getUtils() { return utils; }

    public ConfigValues getConfigValues() { return configValues; }

    public HookUtils getHookUtils() { return hookUtils; }

}
