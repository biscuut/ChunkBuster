package codes.biscuit.chunkbuster.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import codes.biscuit.chunkbuster.ChunkBuster;

public class OtherEvents implements Listener {

    private ChunkBuster main;

    public OtherEvents(ChunkBuster main) {
        this.main = main;
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        if (Bukkit.getVersion().contains("1.13")) {
            if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.LAVA)) {
                if (!main.getUtils().getWaterChunks().contains(e.getBlock().getChunk()) && main.getUtils().getWaterChunks().contains(e.getToBlock().getChunk())) {
                    e.setCancelled(true);
                }
            }
        } else {
            if (e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_WATER"))
                    || e.getBlock().getType().equals(Material.LAVA) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_LAVA"))) {
                if (!main.getUtils().getWaterChunks().contains(e.getBlock().getChunk()) && main.getUtils().getWaterChunks().contains(e.getToBlock().getChunk())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
