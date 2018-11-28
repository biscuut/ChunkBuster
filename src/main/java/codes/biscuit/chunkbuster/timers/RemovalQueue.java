package codes.biscuit.chunkbuster.timers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import codes.biscuit.chunkbuster.ChunkBuster;

import java.util.LinkedList;

public class RemovalQueue extends BukkitRunnable {

    private ChunkBuster main;
    private LinkedList<Block> blocks = new LinkedList<>();
    private Player p;

    public RemovalQueue(ChunkBuster main, Player p) {
        this.main = main;
        this.p = p;
    }

    @Override
    public void run() {
        for (int count = 0; count < main.getConfigValues().getBlockPerTick(); count++) {
            if (!blocks.isEmpty()) {
                Block b = blocks.getFirst();
                if (!b.getType().equals(Material.AIR)) {
                    if (main.getHookUtils().isCoreProtectEnabled()) {
                        main.getHookUtils().logBlock(p, b.getLocation(), b.getType(), b.getData());
                    }
                    b.setType(Material.AIR);
                } else {
                    count--;
                }
                blocks.removeFirst();
            } else {
                break;
            }
        }
        if (blocks.isEmpty()) {
            cancel();
        }
    }

    public LinkedList<Block> getBlocks() { return this.blocks; }
}
