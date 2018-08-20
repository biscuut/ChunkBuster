package xyz.biscut.chunkbuster;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.biscut.chunkbuster.commands.ChunkBusterCommand;
import xyz.biscut.chunkbuster.events.OtherEvents;
import xyz.biscut.chunkbuster.events.PlayerEvents;
import xyz.biscut.chunkbuster.utils.HookUtils;
import xyz.biscut.chunkbuster.hooks.MCoreFactionsHook;
import xyz.biscut.chunkbuster.hooks.FactionsUUIDHook;
import xyz.biscut.chunkbuster.timers.RemovalQueue;
import xyz.biscut.chunkbuster.utils.ConfigValues;
import xyz.biscut.chunkbuster.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ChunkBuster extends JavaPlugin {

    private ConfigValues configValues = new ConfigValues(this);
    private HashMap<Player, Location> chunkBusterLocations = new HashMap<>();
    private LinkedList<Block> removalQueue = new LinkedList<>();
    private HashSet<Chunk> waterChunks = new HashSet<>();
    private Utils utils = new Utils(this);
    private HookUtils hookUtils;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("MassiveCore") != null) {
            hookUtils = new HookUtils(0, new MCoreFactionsHook());
        } else {
            hookUtils = new HookUtils(1, new FactionsUUIDHook());
        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new OtherEvents(this), this);
        getCommand("chunkbuster").setExecutor(new ChunkBusterCommand(this));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new RemovalQueue(this), 1L, 1L);
    }

    public Utils getUtils() { return utils; }

    public ConfigValues getConfigValues() { return configValues; }

    public HookUtils getHookUtils() { return hookUtils; }

    public HashMap<Player, Location> getChunkBusterLocations() { return chunkBusterLocations; }

    public LinkedList<Block> getRemovalQueue() { return removalQueue; }

    public HashSet<Chunk> getWaterChunks() { return waterChunks; }
}
