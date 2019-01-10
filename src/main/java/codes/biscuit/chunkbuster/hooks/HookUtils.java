package codes.biscuit.chunkbuster.hooks;

import codes.biscuit.chunkbuster.ChunkBuster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.EnumMap;
import java.util.Map;

public class HookUtils {

    private Map<HookType, Object> enabledHooks = new EnumMap<>(HookType.class);
    private ChunkBuster main;

    /**
     * Construct the instance with the correct hook
     */
    public HookUtils(ChunkBuster main) {
        this.main = main;
        PluginManager pm = main.getServer().getPluginManager();
        if (pm.getPlugin("MassiveCore") != null &&
                pm.getPlugin("Factions") != null) {
            main.getLogger().info("Hooked into MassiveCore Factions");
            enabledHooks.put(HookType.MCOREFACTIONS, new MCoreFactionsHook(main));
        } else if (pm.getPlugin("Factions") != null) {
            main.getLogger().info("Hooked into FactionsUUID/SavageFactions");
            enabledHooks.put(HookType.FACTIONSUUID, new FactionsUUIDHook(main));
        }
        if (pm.getPlugin("WorldGuard") != null) {
            main.getLogger().info("Hooked into WorldGuard");
            enabledHooks.put(HookType.WORLDGUARD, new WorldGuardHook());
        }
        if (pm.getPlugin("Towny") != null) {
            main.getLogger().info("Hooked into Towny");
            enabledHooks.put(HookType.TOWNY, new WorldGuardHook());
        }
        if (pm.getPlugin("CoreProtect") != null) {
            main.getLogger().info("Hooked into CoreProtect");
            enabledHooks.put(HookType.COREPROTECT, new CoreProtectHook());
        }
    }

    /**
     * Check if a player has a faction
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return true (since
     * as false it will stop all the logic). Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param p The player to check
     * @return If this player has a faction
     */
    public boolean hasFaction(Player p) {
        if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.MCOREFACTIONS)) {
            MCoreFactionsHook mCoreFactionsHook = (MCoreFactionsHook)enabledHooks.get(HookType.MCOREFACTIONS);
            return mCoreFactionsHook.hasFaction(p);
        } else if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook)enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.hasFaction(p);
        } else {
            return true;
        }
    }

    /**
     * Check if a location is wilderness
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return false. Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param loc The location to check
     * @return If this chunk is wilderness
     */
    public boolean isWilderness(Location loc) {
        if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.MCOREFACTIONS)) {
            MCoreFactionsHook mCoreFactionsHook = (MCoreFactionsHook)enabledHooks.get(HookType.MCOREFACTIONS);
            return mCoreFactionsHook.isWilderness(loc);
        } else if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook)enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.isWilderness(loc);
        } else {
            return false;
        }
    }

    /**
     * Compare a location to a player using the appropriate hook
     * <p>
     * This method will check using all enabled hooks.
     * <p>
     * @param loc The location to be compared
     * @param p The player to be compared
     * @return If the player can clear this chunk
     */
    public boolean compareLocToPlayer(Location loc, Player p) {
        boolean canBuild = true;
        if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.MCOREFACTIONS)) {
            MCoreFactionsHook mCoreFactionsHook = (MCoreFactionsHook)enabledHooks.get(HookType.MCOREFACTIONS);
            if (!mCoreFactionsHook.compareLocPlayerFaction(loc, p)) canBuild = false;
        } else if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook)enabledHooks.get(HookType.FACTIONSUUID);
            if (!factionsUUIDHook.compareLocPlayerFaction(loc, p)) canBuild = false;
        }
        if (main.getConfigValues().worldguardHookEnabled() && enabledHooks.containsKey(HookType.WORLDGUARD)) {
            WorldGuardHook worldGuardHook = (WorldGuardHook)enabledHooks.get(HookType.WORLDGUARD);
            if (!worldGuardHook.checkLocationBreakFlag(loc.getChunk(), p)) canBuild = false;
        }
        if (main.getConfigValues().townyHookEnabled() && enabledHooks.containsKey(HookType.TOWNY)) {
            TownyHook townyHook = (TownyHook)enabledHooks.get(HookType.TOWNY);
            if (!townyHook.canBuild(loc.getChunk(), p)) canBuild = false;
        }
        return canBuild;
    }

    /**
     * Check if a player has the minimum role to place chunkbusters
     * <p>
     * This method isn't always applicable since they may not be using
     * a factions hook, so in that case it will return true. Otherwise
     * it will check using the appropriate factions hook.
     * <p>
     * @param p The player to check
     * @return If this player has the minimum role
     */
    public boolean checkRole(Player p) {
        if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.MCOREFACTIONS)) {
            MCoreFactionsHook mCoreFactionsHook = (MCoreFactionsHook)enabledHooks.get(HookType.MCOREFACTIONS);
            return mCoreFactionsHook.checkRole(p, main.getConfigValues().getMinimumRole());
        } else if (main.getConfigValues().factionsHookEnabled() && enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook)enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.checkRole(p, main.getConfigValues().getMinimumRole());
        } else {
            return true;
        }
    }

    /**
     * Log a block as removed in CoreProtect
     * @param p The player to log
     * @param loc The block's location
     * @param mat The block's material
     * @param damage The block's damage value
     */
    public void logBlock(Player p, Location loc, Material mat, byte damage) {
        if (main.getConfigValues().coreprotectHookEnabled() && enabledHooks.containsKey(HookType.COREPROTECT)) {
            CoreProtectHook coreProtectHook = (CoreProtectHook)enabledHooks.get(HookType.COREPROTECT);
            coreProtectHook.logBlock(p.getName(), loc, mat, damage);
        }
    }

    public enum HookType {
        MCOREFACTIONS,
        FACTIONSUUID,
        WORLDGUARD,
        COREPROTECT,
        TOWNY
    }

}
