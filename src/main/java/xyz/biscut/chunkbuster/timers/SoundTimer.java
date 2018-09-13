package xyz.biscut.chunkbuster.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.biscut.chunkbuster.ChunkBuster;

public class SoundTimer extends BukkitRunnable {

    private ChunkBuster main;
    private Player p;
    private int count;

    public SoundTimer(ChunkBuster main, Player p, int count) {
        this.main = main;
        this.p = p;
        this.count = count;
    }

    @Override
    public void run() {
        if (p != null) {
            p.playSound(p.getLocation(), main.getConfigValues().getWarmupSoundString(), main.getConfigValues().getWarmupSoundVolume(), main.getConfigValues().getWarmupSoundPitch());
        }
        count--;
        if (count <= 0) {
            cancel();
        }
    }
}
