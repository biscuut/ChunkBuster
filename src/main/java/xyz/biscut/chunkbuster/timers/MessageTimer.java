package xyz.biscut.chunkbuster.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.biscut.chunkbuster.ChunkBuster;

public class MessageTimer extends BukkitRunnable {

    private int seconds;
    private Player p;
    private ChunkBuster main;

    public MessageTimer(int seconds, Player p, ChunkBuster main) {
        this.seconds = seconds;
        this.p = p;
        this.main = main;
    }

    @Override
    public void run() {
        if (seconds <= 0) {
            cancel();
            return;
        }
        if (!main.getConfigValues().getClearingSecondsMessage(seconds).equals("")) {
            p.sendMessage(main.getConfigValues().getClearingSecondsMessage(seconds));
        }
        if (!main.getConfigValues().sendWarmupEverySecond()) {
            cancel();
            return;
        }
        seconds--;
    }
}
