package codes.biscuit.chunkbuster.hooks;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public interface WorldGuardHook {

    boolean checkLocationBreakFlag(Chunk chunk, Player p);
}
