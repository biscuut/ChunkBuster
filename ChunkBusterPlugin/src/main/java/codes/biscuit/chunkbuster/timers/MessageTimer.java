package codes.biscuit.chunkbuster.timers;

import codes.biscuit.chunkbuster.ChunkBuster;
import codes.biscuit.chunkbuster.utils.ConfigValues;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MessageTimer extends BukkitRunnable {

    private int seconds;
    private UUID uuid;
    private ChunkBuster main;

    public MessageTimer(int seconds, UUID uuid, ChunkBuster main) {
        this.seconds = seconds;
        this.uuid = uuid;
        this.main = main;
    }

    @Override
    public void run() {
        if (seconds <= 0) {
            cancel();
            return;
        }
        main.getUtils().sendMessage(main.getServer().getPlayer(uuid), ConfigValues.Message.CLEARING_IN_SECONDS, seconds);
        if (!main.getConfigValues().sendWarmupEverySecond()) {
            cancel();
            return;
        }
        seconds--;
    }
}
