package codes.biscuit.chunkbuster.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuard_7 implements WorldGuardHook {

    public boolean checkLocationBreakFlag(Chunk chunk, Player p) {
        WorldGuard worldGuard = WorldGuard.getInstance();
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        Block[] cornerBlocks = {chunk.getBlock(0, chunk.getWorld().getMaxHeight() / 2, 0), chunk.getBlock(0, chunk.getWorld().getMaxHeight() / 2, 15),
                chunk.getBlock(15, chunk.getWorld().getMaxHeight() / 2, 0), chunk.getBlock(15, chunk.getWorld().getMaxHeight() / 2, 15)};
        for (Block currentBlock : cornerBlocks) {
            RegionQuery regionQuery = container.createQuery();
            ApplicableRegionSet set = regionQuery.getApplicableRegions(BukkitAdapter.adapt(currentBlock.getLocation()));
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            if (set.queryState(localPlayer, Flags.BLOCK_BREAK) == StateFlag.State.DENY) return false; // If block break is set to deny
        }
        return true;
    }
}
