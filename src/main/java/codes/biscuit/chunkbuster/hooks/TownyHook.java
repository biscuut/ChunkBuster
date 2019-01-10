package codes.biscuit.chunkbuster.hooks;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

class TownyHook {

    boolean canBuild(Chunk chunk, Player p) {
        if (TownySettings.getTownBlockSize() % 16 == 0) {
            Block centerBlock = chunk.getBlock(7,chunk.getWorld().getMaxHeight()/2, 7);
            return PlayerCacheUtil.getCachePermission(p, centerBlock.getLocation(), centerBlock.getType(), TownyPermission.ActionType.DESTROY);
        } else {
            Block[] cornerBlocks = {chunk.getBlock(0,chunk.getWorld().getMaxHeight()/2, 0), chunk.getBlock(0,chunk.getWorld().getMaxHeight()/2, 15),
                    chunk.getBlock(15,chunk.getWorld().getMaxHeight()/2, 0), chunk.getBlock(15,chunk.getWorld().getMaxHeight()/2, 15)};

            for (Block currentBlock : cornerBlocks) {
                if (!PlayerCacheUtil.getCachePermission(p, currentBlock.getLocation(), currentBlock.getType(), TownyPermission.ActionType.DESTROY)) return false;
            }
            return true;
        }
    }
}
