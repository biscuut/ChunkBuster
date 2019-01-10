package codes.biscuit.chunkbuster.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import codes.biscuit.chunkbuster.ChunkBuster;

import java.util.ArrayList;
import java.util.List;

public class ConfigValues {

    private ChunkBuster main;

    public ConfigValues(ChunkBuster main) {
        this.main = main;
    }

    public Material getChunkBusterMaterial() {
        String rawMaterial = main.getConfig().getString("chunkbuster.material");
        Material mat;
        if (rawMaterial.contains(":")) {
            String[] materialSplit = rawMaterial.split(":");
            try {
                mat = Material.valueOf(materialSplit[0]);
            } catch (IllegalArgumentException ex) {
                mat = getEnderPortalMaterial();
                main.getLogger().severe("Your chunk buster material is invalid!");
            }
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = getEnderPortalMaterial();
                main.getLogger().severe("Your chunk buster material is invalid!");
            }
        }
        return mat;
    }

    short getChunkBusterDamage() {
        String rawDamage = main.getConfig().getString("chunkbuster.material");
        if (rawDamage.contains(":")) {
            String[] materialSplit = rawDamage.split(":");
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 1;
                main.getLogger().severe("Your chunk buster damage is invalid!");
            }
            return damage;
        } else {
            return 0;
        }
    }

    String getChunkBusterName() { return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("chunkbuster.name")); }

    List<String> getChunkBusterLore(int chunkRadius) {
        List<String> uncolouredList = main.getConfig().getStringList("chunkbuster.lore");
        List<String> colouredList = new ArrayList<>();
        for (String s : uncolouredList) {
            colouredList.add(ChatColor.translateAlternateColorCodes('&', s).replace("{area}", chunkRadius+"x"+chunkRadius));
        }
        return colouredList;
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
        String rawMaterial = main.getConfig().getString("confirm-gui.confirm-material");
        Material mat;
        if (rawMaterial.contains(":")) {
            String[] materialSplit = rawMaterial.split(":");
            try {
                mat = Material.valueOf(materialSplit[0]);
            } catch (IllegalArgumentException ex) {
                mat = getDefaultConfirmMaterial();
                main.getLogger().severe("Your accept-block material is invalid!");
            }
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 1;
                main.getLogger().severe("Your accept-block damage is invalid!");
            }
            return new ItemStack(mat, 1, damage);
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = getDefaultConfirmMaterial();
                main.getLogger().severe("Your accept-block material is invalid!");
            }
            return new ItemStack(mat, 1);
        }
    }

    public ItemStack getCancelBlockItemStack() {
        String rawMaterial = main.getConfig().getString("confirm-gui.cancel-material");
        Material mat;
        if (rawMaterial.contains(":")) {
            String[] materialSplit = rawMaterial.split(":");
            try {
                mat = Material.valueOf(materialSplit[0]);
            } catch (IllegalArgumentException ex) {
                mat = getDefaultCancelMaterial();
                main.getLogger().severe("Your cancel-block material is invalid!");
            }
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 1;
                main.getLogger().severe("Your cancel-block damage is invalid!");
            }
            return new ItemStack(mat, 1, damage);
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = getDefaultCancelMaterial();
                main.getLogger().severe("Your cancel-block material is invalid!");
            }
            return new ItemStack(mat, 1);
        }
    }

    public ItemStack getFillItemStack() {
        String rawMaterial = main.getConfig().getString("confirm-gui.fill-material");
        Material mat;
        if (rawMaterial.contains(":")) {
            String[] materialSplit = rawMaterial.split(":");
            try {
                mat = Material.valueOf(materialSplit[0]);
            } catch (IllegalArgumentException ex) {
                mat = Material.AIR;
                main.getLogger().severe("Your fill-block material is invalid!");
            }
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 1;
                main.getLogger().severe("Your fill-block damage is invalid!");
            }
            return new ItemStack(mat, 1, damage);
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = Material.AIR;
                main.getLogger().severe("Your fill-block material is invalid!");
            }
            return new ItemStack(mat, 1);
        }
    }

    public String getGUITitle() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("confirm-gui.title"));
    }

    public String getConfirmName() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("confirm-gui.confirm-block-name"));
    }

    public String getCancelName() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("confirm-gui.cancel-block-name"));
    }

    public String getFillName() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("confirm-gui.fill-name"));
    }

    public List<String> getConfirmLore() {
        List<String> uncolouredList = main.getConfig().getStringList("confirm-gui.confirm-block-lore");
        List<String> colouredList = new ArrayList<>();
        for (String s : uncolouredList) {
            colouredList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return colouredList;
    }

    public List<String> getCancelLore() {
        List<String> uncolouredList = main.getConfig().getStringList("confirm-gui.cancel-block-lore");
        List<String> colouredList = new ArrayList<>();
        for (String s : uncolouredList) {
            colouredList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return colouredList;
    }

    public List<String> getFillLore() {
        List<String> uncolouredList = main.getConfig().getStringList("confirm-gui.fill-lore");
        List<String> colouredList = new ArrayList<>();
        for (String s : uncolouredList) {
            colouredList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return colouredList;
    }

    public String getGiveMessage(Player p, int amount) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.give"))
                .replace("{player}", p.getName()).replace("{amount}", String.valueOf(amount));
    }

    public String getReceiveMessage(int amount) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.receive"))
                .replace("{amount}", String.valueOf(amount));
    }

    public String getNoPermissionMessageCommand() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.no-permission-command"));
    }

    public String getNoPermissionMessagePlace() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.no-permission-place"));
    }

    public String getNoFactionMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.no-faction"));
    }

    public String getOnlyWildernessClaimMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.only-wilderness-and-claim"));
    }

    public String getOnlyClaimMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.only-claim"));
    }

    String getClearingMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.clearing-chunks"));
    }

    public String getNoItemMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.no-item"));
    }

    public String getClearingSecondsMessage(int seconds) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.clearing-in-seconds")
        .replace("{seconds}", String.valueOf(seconds)));
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

    public String getMinimumRoleMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.not-minimum-role"));
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

    public String getGUICancelMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.gui-cancel"));
    }

    List<Material> getIgnoredBlocks() {
        List<String> stringList = main.getConfig().getStringList("ignored-materials");
        List<Material> materialList = new ArrayList<>();
        for (String s : stringList) {
            try {
                materialList.add(Material.valueOf(s));
            } catch (IllegalArgumentException ignored) {}
        }
        return materialList;
    }

    public int getCooldown() {
        return main.getConfig().getInt("cooldown");
    }

    public String getCooldownMessage(int minutes, int seconds) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.cooldown")).replace("{minutes}", String.valueOf(minutes)).replace("{seconds}", String.valueOf(seconds));
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

    private Material getDefaultCancelMaterial() {
        Material mat;
        if (Bukkit.getVersion().contains("1.13")) {
            mat = Material.valueOf("RED_WOOL");
        } else {
            mat = Material.valueOf("WOOL");
        }
        return mat;
    }

    private Material getDefaultConfirmMaterial() {
        Material mat;
        if (Bukkit.getVersion().contains("1.13")) {
            mat = Material.valueOf("GREEN_WOOL");
        } else {
            mat = Material.valueOf("WOOL");
        }
        return mat;
    }

    private Material getEnderPortalMaterial() {
        Material mat;
        if (Bukkit.getVersion().contains("1.13")) {
            mat = Material.valueOf("END_PORTAL_FRAME");
        } else {
            mat = Material.valueOf("ENDER_PORTAL_FRAME");
        }
        return mat;
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
}
