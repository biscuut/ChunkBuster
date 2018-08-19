package xyz.biscut.chunkbuster.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.biscut.chunkbuster.hooks.FactionsUUIDHook;
import xyz.biscut.chunkbuster.hooks.MCoreFactionsHook;

public class HookUtils {

    private int factionsVersion;
    private MCoreFactionsHook mCoreFactionsHook = null;
    private FactionsUUIDHook factionsUUIDHook = null;

    public HookUtils(int factionsVersion, Object o) {
        this.factionsVersion = factionsVersion;
        if (factionsVersion == 0) {
            mCoreFactionsHook = (MCoreFactionsHook)o;
        } else {
            factionsUUIDHook = (FactionsUUIDHook)o;
        }
    }

    public boolean hasFaction(Player p) {
        if (factionsVersion == 0) {
            return mCoreFactionsHook.hasFaction(p);
        } else {
            return factionsUUIDHook.hasFaction(p);
        }
    }

    public boolean isWilderness(Location loc) {
        if (factionsVersion == 0) {
            return mCoreFactionsHook.isWilderness(loc);
        } else {
            return factionsUUIDHook.isWilderness(loc);
        }
    }

    public boolean compareLocPlayerFaction(Location loc, Player p) {
        if (factionsVersion == 0) {
            return mCoreFactionsHook.compareLocPlayerFaction(loc, p);
        } else {
            return factionsUUIDHook.compareLocPlayerFaction(loc, p);
        }
    }
}
