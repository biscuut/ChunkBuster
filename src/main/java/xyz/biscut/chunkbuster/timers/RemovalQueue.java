package xyz.biscut.chunkbuster.timers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import xyz.biscut.chunkbuster.ChunkBuster;

public class RemovalQueue implements Runnable {

    private ChunkBuster main;

    public RemovalQueue(ChunkBuster main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (int count = 0; count < main.getConfigValues().getBlockPerTick(); count++) {
            if (!main.getRemovalQueue().isEmpty()) {
                Block b = main.getRemovalQueue().getFirst();
                b.setType(Material.AIR);
                main.getRemovalQueue().removeFirst();
            } else {
                return;
            }
        }
    }
}
