package codes.biscuit.chunkbuster.hooks;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public interface IWorldGuardHook {

    boolean checkLocationBreakFlag(Chunk chunk, Player p);
}
