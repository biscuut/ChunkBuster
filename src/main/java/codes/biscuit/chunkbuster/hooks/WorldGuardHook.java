package codes.biscuit.chunkbuster.hooks;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class WorldGuardHook {

    boolean checkLocationBreakFlag(Location loc, Player p) {
        WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(p);
        return !(set.queryState(localPlayer, DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY);
    }
}
