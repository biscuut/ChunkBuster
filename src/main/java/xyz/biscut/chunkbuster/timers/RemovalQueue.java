package xyz.biscut.chunkbuster.timers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.biscut.chunkbuster.ChunkBuster;

import java.util.LinkedList;

public class RemovalQueue extends BukkitRunnable {

    private ChunkBuster main;
    private LinkedList<Block> blocks = new LinkedList<>();

    public RemovalQueue(ChunkBuster main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (int count = 0; count < main.getConfigValues().getBlockPerTick(); count++) {
            if (!blocks.isEmpty()) {
                Block b = blocks.getFirst();
                if (!b.getType().equals(Material.AIR)) {
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
