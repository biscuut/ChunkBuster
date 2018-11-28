package codes.biscuit.chunkbuster.hooks;


import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;

import static org.bukkit.Bukkit.getServer;

// CoreProtect API v5, can't use v6 since 1.8 doesn't support it.
public class CoreProtectHook {

    private CoreProtectAPI getCoreProtect() {
        return ((CoreProtect)getServer().getPluginManager().getPlugin("CoreProtect")).getAPI();
    }

    public void logBlock(String p, Location loc, Material mat, byte damage) {
        getCoreProtect().logRemoval(p + " (ChunkBuster)", loc, mat, damage);
    }
}
