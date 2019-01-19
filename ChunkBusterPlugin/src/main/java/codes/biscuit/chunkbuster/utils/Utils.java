package codes.biscuit.chunkbuster.utils;

import codes.biscuit.chunkbuster.timers.RemovalQueue;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import codes.biscuit.chunkbuster.ChunkBuster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {

    private ChunkBuster main;
    private HashSet<Chunk> waterChunks = new HashSet<>();

    public Utils(ChunkBuster main) {
        this.main = main;
    }

    private ItemStack addGlow(ItemStack item, int level) {
        item.addUnsafeEnchantment(Enchantment.LURE, level);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void clearChunks(int chunkBusterArea, Location chunkBusterLocation, Player p) {
        Set<Material> ignoredBlocks = new HashSet<>(main.getConfigValues().getIgnoredBlocks());
        if (chunkBusterArea == 1) {
            RemovalQueue removalQueue = new RemovalQueue(main, p);
            waterChunks.add(chunkBusterLocation.getChunk());
            for (int y = main.getConfigValues().getMaximumY(p); y >= main.getConfigValues().getMinimumY(p); y--) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block b = chunkBusterLocation.getChunk().getBlock(x, y, z);
                        if (!b.getType().equals(Material.AIR) && !ignoredBlocks.contains(b.getType())) {
                            removalQueue.getBlocks().add(b);
                        }
                    }
                }
            }
            removalQueue.runTaskTimer(main, 1L, 1L);
            if (!main.getConfigValues().getClearingMessage().equals("")) {
                p.sendMessage(main.getConfigValues().getClearingMessage());
            }
        } else if (chunkBusterArea > 2 && chunkBusterArea % 2 != 0) {
            RemovalQueue removalQueue = new RemovalQueue(main, p);
            int upperSearchBound = ((chunkBusterArea - 1) / 2) + 1;
            int lowerSearchBound = (chunkBusterArea - 1) / -2;
            int startingX = chunkBusterLocation.getChunk().getX();
            int startingZ = chunkBusterLocation.getChunk().getZ();
            for (int y = main.getConfigValues().getMaximumY(p); y >= main.getConfigValues().getMinimumY(p); y--) {
                for (int chunkX = lowerSearchBound; chunkX < upperSearchBound; chunkX++) {
                    for (int chunkZ = lowerSearchBound; chunkZ < upperSearchBound; chunkZ++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(startingX + chunkX, startingZ + chunkZ);
                        Location chunkCheckLoc = chunk.getBlock(7, 60, 7).getLocation();
                        if (main.getHookUtils().compareLocToPlayer(chunkCheckLoc, p)) {
                            waterChunks.add(chunk);
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    Block b = chunk.getBlock(x, y, z);
                                    if (!b.getType().equals(Material.AIR) && !ignoredBlocks.contains(b.getType())) {
                                        removalQueue.getBlocks().add(b);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            removalQueue.runTaskTimer(main, 1L, 1L);
            if (!main.getConfigValues().getClearingMessage().equals("")) {
                p.sendMessage(main.getConfigValues().getClearingMessage());
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid chunk buster!");
        }
    }

    public void updateConfig(ChunkBuster main) {
        if (main.getConfigValues().getConfigVersion() < 1.9) {
            Map<String, Object> oldValues = new HashMap<>();
            for (String oldKey : main.getConfig().getKeys(true)) {
                oldValues.put(oldKey, main.getConfig().get(oldKey));
            }
            main.saveResource("config.yml", true);
            main.reloadConfig();
            for (String newKey : main.getConfig().getKeys(true)) {
                if (oldValues.containsKey(newKey) && !oldValues.get(newKey).equals(main.getConfig().get(newKey))) {
                    main.getConfig().set(newKey, oldValues.get(newKey));
                }
            }
            main.getConfig().set("config-version", 1.9);
            main.saveConfig();
        }
    }

    public void checkUpdates(Player p) {
        try {
            URL url = new URL("https://raw.githubusercontent.com/biscuut/ChunkBuster/master/pom.xml");
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(5000);
            connection.addRequestProperty("User-Agent", "ChunkBuster update checker");
            connection.setDoOutput(true);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentLine;
            String newestVersion = "";
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains("<version>")) {
                    String[] newestVersionSplit = currentLine.split(Pattern.quote("<version>"));
                    newestVersionSplit = newestVersionSplit[1].split(Pattern.quote("</version>"));
                    newestVersion = newestVersionSplit[0];
                    break;
                }
            }
            reader.close();
            List<Integer> newestVersionNumbers = new ArrayList<>();
            List<Integer> thisVersionNumbers = new ArrayList<>();
            try {
                for (String s : newestVersion.split(Pattern.quote("."))) {
                    newestVersionNumbers.add(Integer.parseInt(s));
                }
                for (String s : main.getDescription().getVersion().split(Pattern.quote("."))) {
                    thisVersionNumbers.add(Integer.parseInt(s));
                }
            } catch (Exception ex) {
                return;
            }
            for (int i = 0; i < 3; i++) {
                if (newestVersionNumbers.get(i) != null && thisVersionNumbers.get(i) != null) {
                    if (newestVersionNumbers.get(i) > thisVersionNumbers.get(i)) {
                        TextComponent newVersion = new TextComponent("A new version of ChunkBuster, " + newestVersion + " is available. Download it by clicking here.");
                        newVersion.setColor(ChatColor.RED);
                        newVersion.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/chunkbuster-1-8-1-12-clear-any-chunk-area.60057/"));
                        p.spigot().sendMessage(newVersion);
                    } else if (thisVersionNumbers.get(i) > newestVersionNumbers.get(i)) {
                        p.sendMessage(ChatColor.RED + "You are running a development version of ChunkBuster, " + main.getDescription().getVersion() + ". The latest online version is " + newestVersion + ".");
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public HashSet<Chunk> getWaterChunks() { return waterChunks; }

    public ItemStack getChunkBusterItem(int giveAmount, int chunkArea) {
        ItemStack item = new ItemStack(main.getConfigValues().getChunkBusterMaterial(), giveAmount, main.getConfigValues().getChunkBusterDamage());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(main.getConfigValues().getChunkBusterName());
        itemMeta.setLore(main.getConfigValues().getChunkBusterLore(chunkArea));
        item.setItemMeta(itemMeta);
        return main.getUtils().addGlow(item, chunkArea); // It must glow
    }
}
