package xyz.biscut.chunkbuster.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.biscut.chunkbuster.hooks.FactionsUUIDHook;
import xyz.biscut.chunkbuster.hooks.MCoreFactionsHook;
import xyz.biscut.chunkbuster.hooks.WorldGuardHook;

public class HookUtils {

    private MCoreFactionsHook mCoreFactionsHook = null;
    private FactionsUUIDHook factionsUUIDHook = null;
    private WorldGuardHook worldGuardHook = null;
    private int hookType;

    /**
     * Construct the instance with the correct hook
     * <p><ol>
     * <li>MassiveCore Factions
     * <li>FactionsUUID/SavageFactions
     * <li>WorldGuard
     * <p></ol>
     * @param hookType The hook type integer
     */
    public HookUtils(int hookType) {
        this.hookType = hookType;
        if (hookType == 1) {
            mCoreFactionsHook = new MCoreFactionsHook();
        } else if (hookType == 2) {
            factionsUUIDHook = new FactionsUUIDHook();
        } else if (hookType == 3) {
            worldGuardHook = new WorldGuardHook();
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
        if (hookType == 1) {
            return mCoreFactionsHook.hasFaction(p);
        } else if (hookType == 2) {
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
        if (hookType == 1) {
            return mCoreFactionsHook.isWilderness(loc);
        } else if (hookType == 2) {
            return factionsUUIDHook.isWilderness(loc);
        } else {
            return false;
        }
    }

    /**
     * Compare a location to a player using the appropriate hook
     * <p>
     * This method is always applicable since even if a factions hook isn't being
     * used, then it will compare the region to WorldGuard flags.
     * <p>
     * @param loc The location to be compared
     * @param p The player to be compared
     * @return If the player can clear this chunk
     */
    public boolean compareLocToPlayer(Location loc, Player p) {
        if (hookType == 1) {
            return mCoreFactionsHook.compareLocPlayerFaction(loc, p);
        } else if (hookType == 2) {
            return factionsUUIDHook.compareLocPlayerFaction(loc, p);
        } else {
            return worldGuardHook.checkLocationBreakFlag(loc, p);
        }
    }

    public int getHookType() {
        return hookType;
    }
}
