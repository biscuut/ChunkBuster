package codes.biscuit.chunkbuster.utils;

import codes.biscuit.chunkbuster.ChunkBuster;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ConfigValues {

    private ChunkBuster main;


    /**
     * This class simplifies retrieving config values. If trying to grab a message, please see {@link Utils#sendMessage}
     */
    public ConfigValues(ChunkBuster main) {
        this.main = main;
    }

    public Material getChunkBusterMaterial() {
        return main.getUtils().itemFromString(main.getConfig().getString("chunkbuster.material")).getType();
    }

    @SuppressWarnings("deprecation")
    short getChunkBusterDamage() {
        return main.getUtils().itemFromString(main.getConfig().getString("chunkbuster.material")).getData().getData();
    }

    String getChunkBusterName() { return Utils.color(main.getConfig().getString("chunkbuster.name")); }

    List<String> getChunkBusterLore(int chunkRadius) {
        List<String> lore = main.getConfig().getStringList("chunkbuster.lore");
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, Utils.color(lore.get(i)).replace("{area}", chunkRadius+"x"+chunkRadius));
        }
        return lore; // For convenience
    }

    public int getBlockPerTick() { return main.getConfig().getInt("blocks-removed-per-tick"); }

    public boolean canPlaceInWilderness() {
        return main.getConfig().getBoolean("can-place-in-wilderness");
    }

    public int getChunkBusterWarmup() {
        return main.getConfig().getInt("warmup.seconds");
    }

    public int getGUIRows() { return main.getConfig().getInt("confirm-gui.rows"); }

    public ItemStack getConfirmBlockItemStack() {
        return main.getUtils().itemFromString(main.getConfig().getString("confirm-gui.confirm-material"));
    }

    public ItemStack getCancelBlockItemStack() {
        return main.getUtils().itemFromString(main.getConfig().getString("confirm-gui.cancel-material"));
    }

    public ItemStack getFillItemStack() {
        return main.getUtils().itemFromString(main.getConfig().getString("confirm-gui.fill-material"));
    }

    public String getGUITitle() {
        return Utils.color(main.getConfig().getString("confirm-gui.title"));
    }

    public String getConfirmName() {
        return Utils.color(main.getConfig().getString("confirm-gui.confirm-block-name"));
    }

    public String getCancelName() {
        return Utils.color(main.getConfig().getString("confirm-gui.cancel-block-name"));
    }

    public String getFillName() {
        return Utils.color(main.getConfig().getString("confirm-gui.fill-name"));
    }

    public List<String> getConfirmLore() {
        return main.getUtils().colorLore(main.getConfig().getStringList("confirm-gui.confirm-block-lore"));
    }

    public List<String> getCancelLore() {
        return main.getUtils().colorLore(main.getConfig().getStringList("confirm-gui.cancel-block-lore"));
    }

    public List<String> getFillLore() {
        return main.getUtils().colorLore(main.getConfig().getStringList("confirm-gui.fill-lore"));
    }

    public boolean sendWarmupEverySecond() {
        return main.getConfig().getBoolean("warmup.send-message-every-second");
    }

    public boolean dropFullInv() {
        return main.getConfig().getBoolean("full-inv-drop-on-floor");
    }

    public String getMinimumRole() {
        return main.getConfig().getString("minimum-factions-role").toLowerCase();
    }

    public boolean warmupSoundEnabled() {
        return main.getConfig().getBoolean("warmup.warmup-sound-enabled");
    }

    public String getWarmupSoundString() {
        return main.getConfig().getString("warmup.warmup-sound");
    }

    public int getWarmupSoundInterval() {
        return main.getConfig().getInt("warmup.warmup-sound-interval");
    }

    public float getWarmupSoundVolume() {
        return (float)main.getConfig().getDouble("warmup.warmup-sound-volume");
    }

    public float getWarmupSoundPitch() {
        return (float)main.getConfig().getDouble("warmup.warmup-sound-pitch");
    }

    public boolean clearingSoundEnabled() {
        return main.getConfig().getBoolean("warmup.clearing-sound-enabled");
    }

    public String getClearingSoundString() {
        return main.getConfig().getString("warmup.clearing-sound");
    }

    public float getClearingSoundVolume() {
        return (float)main.getConfig().getDouble("warmup.clearing-volume");
    }

    public float getClearingSoundPitch() {
        return (float)main.getConfig().getDouble("warmup.clearing-pitch");
    }

    public boolean confirmSoundEnabled() {
        return main.getConfig().getBoolean("confirm-gui.confirm-sound-enabled");
    }

    public String getConfirmSoundString() {
        return main.getConfig().getString("confirm-gui.confirm-sound");
    }

    public float getConfirmSoundVolume() {
        return (float)main.getConfig().getDouble("confirm-gui.confirm-volume");
    }

    public float getConfirmSoundPitch() {
        return (float)main.getConfig().getDouble("confirm-gui.confirm-pitch");
    }

    public boolean cancelSoundEnabled() {
        return main.getConfig().getBoolean("confirm-gui.cancel-sound-enabled");
    }

    public String getCancelSoundString() {
        return main.getConfig().getString("confirm-gui.cancel-sound");
    }

    public float getCancelSoundVolume() {
        return (float)main.getConfig().getDouble("confirm-gui.cancel-volume");
    }

    public float getCancelSoundPitch() {
        return (float)main.getConfig().getDouble("confirm-gui.cancel-pitch");
    }

    Set<Material> getIgnoredBlocks() {
        List<String> rawMaterials = main.getConfig().getStringList("ignored-materials");
        Set<Material> materials = EnumSet.noneOf(Material.class);
        for (String rawMaterial : rawMaterials) {
            try {
                materials.add(Material.valueOf(rawMaterial));
            } catch (IllegalArgumentException ignored) {}
        }
        return materials;
    }

    public int getCooldown() {
        return main.getConfig().getInt("cooldown");
    }

    public boolean showUpdateMessage() {
        return main.getConfig().getBoolean("show-update-messages");
    }

    int getMinimumY(Player p) {
        if (main.getConfig().getString("minimum-y").toLowerCase().contains("{player}")) {
            return (int)p.getLocation().getY();
        } else {
            try {
                return Integer.valueOf(main.getConfig().getString("minimum-y"));
            } catch (NumberFormatException ex) {
                main.getLogger().warning("Your minimum-y value is invalid, please either change this to an integer or '{player}' in the config.");
                return 0;
            }
        }
    }

    int getMaximumY(Player p) {
        if (main.getConfig().getString("maximum-y").toLowerCase().contains("{player}")) {
            return (int)p.getLocation().getY();
        } else {
            try {
                return Integer.valueOf(main.getConfig().getString("maximum-y"));
            } catch (NumberFormatException ex) {
                main.getLogger().warning("Your maximum-y value is invalid, please either change this to an integer or '{player}' in the config.");
                return 255;
            }
        }
    }

    double getConfigVersion() {
        return main.getConfig().getDouble("config-version");
    }

    public boolean factionsHookEnabled() {
        return main.getConfig().getBoolean("hooks.factions");
    }

    public boolean coreprotectHookEnabled() {
        return main.getConfig().getBoolean("hooks.coreprotect");
    }

    public boolean worldguardHookEnabled() {
        return main.getConfig().getBoolean("hooks.worldguard");
    }

    public boolean townyHookEnabled() {
        return main.getConfig().getBoolean("hooks.towny");
    }

    public int getNoFallMillis() {
        return main.getConfig().getInt("no-fall-seconds")*1000;
    }

    boolean itemShouldGlow() {
        return main.getConfig().getBoolean("chunkbuster.glow");
    }

    String getMessage(Message message, Object... params) {
        String messageText = Utils.color(main.getConfig().getString(message.getPath()));
        if (message == Message.GIVE) { // name string, amount int
            messageText = messageText.replace("{player}", (String)params[0]).replace("{amount}", String.valueOf(params[1]));
        } else if (message == Message.RECEIVE) { // amount int
            messageText = messageText.replace("{amount}", String.valueOf(params[0]));
        } else if (message == Message.CLEARING_IN_SECONDS) { // seconds int
            messageText = messageText.replace("{seconds}",  String.valueOf(params[0]));
        } else if (message == Message.COOLDOWN) {
            messageText = messageText.replace("{minutes}", String.valueOf(params[0])).replace("{seconds}", String.valueOf(params[1]));
        }
        return messageText;
    }

    public enum Message {
        GIVE("give"),
        RECEIVE("receive"),
        NO_FACTION("no-faction"),
        CANNOT_PLACE("cannot-place"),
        CLEARING_CHUNKS("clearing-chunks"),
        COOLDOWN("cooldown"),
        NO_ITEM("no-item"),
        NO_PERMISSION_PLACE("no-permission-place"),
        NO_PERMISSION_COMMAND("no-permission-command"),
        CLEARING_IN_SECONDS("clearing-in-seconds"),
        NOT_MINIMUM_ROLE("not-minimum-role"),
        GUI_CANCEL("gui-cancel");

        private String path;

        Message(String path) {
            this.path = "messages."+path;
        }

        public String getPath() {
            return path;
        }
    }
}
