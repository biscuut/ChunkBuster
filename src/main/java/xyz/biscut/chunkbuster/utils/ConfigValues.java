package xyz.biscut.chunkbuster.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.biscut.chunkbuster.ChunkBuster;

import java.util.ArrayList;
import java.util.List;

public class ConfigValues {

    private ChunkBuster main;

    public ConfigValues(ChunkBuster main) {
        this.main = main;
    }

    public Material getChunkBusterMaterial() {
        Material mat;
        try {
            mat = Material.valueOf(main.getConfig().getString("chunkbuster.material"));
        } catch (IllegalArgumentException ex) {
            mat = Material.ENDER_PORTAL_FRAME;
            Bukkit.getLogger().severe("[ChunkBuster] Your chunk buster material is invalid!");
        }
        return mat;
    }

    public String getChunkBusterName() { return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("chunkbuster.name")); }

    public List<String> getChunkBusterLore(int chunkRadius) {
        List<String> uncolouredList = main.getConfig().getStringList("chunkbuster.lore");
        List<String> colouredList = new ArrayList<>();
        for (String s : uncolouredList) {
            colouredList.add(ChatColor.translateAlternateColorCodes('&', s).replace("{area}", String.valueOf(chunkRadius) + "x" + String.valueOf(chunkRadius)));
        }
        return colouredList;
    }

    public int getBlockPerTick() { return main.getConfig().getInt("blocks-removed-per-tick"); }

    public boolean canPlaceInWilderness() {
        return main.getConfig().getBoolean("can-place-in-wilderness");
    }

    public int getChunkBusterWarmup() {
        return main.getConfig().getInt("chunkbuster-warmup");
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
                mat = Material.WOOL;
                Bukkit.getLogger().severe("[ChunkBuster] Your accept-block material is invalid!");
            }
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 0;
                Bukkit.getLogger().severe("[ChunkBuster] Your accept-block damage is invalid!");
            }
            return new ItemStack(mat, 1, damage);
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = Material.WOOL;
                Bukkit.getLogger().severe("[ChunkBuster] Your accept-block material is invalid!");
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
                mat = Material.WOOL;
                Bukkit.getLogger().severe("[ChunkBuster] Your cancel-block material is invalid!");
            }
            short damage;
            try {
                damage = Short.valueOf(materialSplit[1]);
            } catch (IllegalArgumentException ex) {
                damage = 0;
                Bukkit.getLogger().severe("[ChunkBuster] Your cancel-block damage is invalid!");
            }
            return new ItemStack(mat, 1, damage);
        } else {
            try {
                mat = Material.valueOf(rawMaterial);
            } catch (IllegalArgumentException ex) {
                mat = Material.WOOL;
                Bukkit.getLogger().severe("[ChunkBuster] Your cancel-block material is invalid!");
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

    public String getGiveMessage(Player p, int amount) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.give"))
                .replace("{player}", p.getName()).replace("{amount}", String.valueOf(amount));
    }

    public String getReceiveMessage(int amount) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.receive"))
                .replace("{amount}", String.valueOf(amount));
    }

    public String getNoPermissionMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.no-permission"));
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

    public String getClearingMessage() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.clearing-chunks"));
    }

    public String getClearingSecondsMessage(int seconds) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.clearing-in-seconds")
        .replace("{seconds}", String.valueOf(seconds)));
    }
}
