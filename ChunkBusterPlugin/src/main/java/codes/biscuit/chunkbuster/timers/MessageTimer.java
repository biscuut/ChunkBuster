package codes.biscuit.chunkbuster.timers;

import codes.biscuit.chunkbuster.utils.ConfigValues;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import codes.biscuit.chunkbuster.ChunkBuster;

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
        main.getUtils().sendMessage(p, ConfigValues.Message.CLEARING_IN_SECONDS, seconds);
        if (!main.getConfigValues().sendWarmupEverySecond()) {
            cancel();
            return;
        }
        seconds--;
    }
}
