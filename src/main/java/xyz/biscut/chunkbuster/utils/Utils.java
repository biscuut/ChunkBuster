package xyz.biscut.chunkbuster.utils;

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
import xyz.biscut.chunkbuster.ChunkBuster;
import xyz.biscut.chunkbuster.timers.RemovalQueue;

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

    public ItemStack addGlow(ItemStack item, int level) {
        item.addUnsafeEnchantment(Enchantment.LURE, level);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void clearChunks(int chunkBusterArea, Location chunkBusterLocation, Player p) {
        HashSet<Material> ignoredBlocks = new HashSet<>(main.getConfigValues().getIgnoredBlocks());
        if (chunkBusterArea == 1) {
            RemovalQueue removalQueue = new RemovalQueue(main);
            waterChunks.add(chunkBusterLocation.getChunk());
            for (int y = chunkBusterLocation.getWorld().getMaxHeight(); y >= 0; y--) {
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
            RemovalQueue removalQueue = new RemovalQueue(main);
            int upperSearchBound = ((chunkBusterArea - 1) / 2) + 1;
            int lowerSearchBound = (chunkBusterArea - 1) / -2;
            int startingX = chunkBusterLocation.getChunk().getX();
            int startingZ = chunkBusterLocation.getChunk().getZ();
            for (int y = chunkBusterLocation.getWorld().getMaxHeight(); y >= 0; y--) {
                for (int chunkX = lowerSearchBound; chunkX < upperSearchBound; chunkX++) {
                    for (int chunkZ = lowerSearchBound; chunkZ < upperSearchBound; chunkZ++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(startingX + chunkX, startingZ + chunkZ);
                        Location chunkCheckLoc = chunk.getBlock(7, 60, 7).getLocation();
                        if (main.getHookUtils().compareLocToPlayer(chunkCheckLoc, p) || main.getHookUtils().isWilderness(chunkCheckLoc)) {
                            if (main.getHookUtils().isWilderness(chunkCheckLoc) && !main.getConfigValues().canPlaceInWilderness()) {
                                continue;
                            }
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
        if (main.getConfig().getDouble("config-version") < 1.1) {
            if (!main.getConfig().isSet("messages.region-protected")) {
                main.getConfig().set("messages.region-protected", "&cYou cannot place chunk busters in this region!");
            }
            main.getConfig().set("config-version", 1.1);
            main.saveConfig();
        }
        if (main.getConfig().getDouble("config-version") < 1.2) {
            if (!main.getConfig().isSet("confirm-gui.confirm-block-lore")) {
                main.getConfig().set("confirm-gui.confirm-block-lore", new ArrayList<String>());
            }
            if (!main.getConfig().isSet("confirm-gui.cancel-block-lore")) {
                main.getConfig().set("confirm-gui.cancel-block-lore", new ArrayList<String>().add(""));
            }
            if (!main.getConfig().isSet("messages.no-permission-command")) {
                if (main.getConfig().isSet("messages.no-permission")) {
                    main.getConfig().set("messages.no-permission-command", main.getConfig().get("messages.no-permission"));
                } else {
                    main.getConfig().set("messages.no-permission-command", "&cNo permission!");
                }
            }
            if (!main.getConfig().isSet("messages.no-permission-place")) {
                main.getConfig().set("messages.no-permission-place", "&cYou cannot place chunkbusters!");
            }
            if (!main.getConfig().isSet("warmup-message-every-second")) {
                main.getConfig().set("warmup-message-every-second", true);
            }
            if (!main.getConfig().isSet("full-inv-drop-on-floor")) {
                main.getConfig().set("full-inv-drop-on-floor", false);
            }
            main.getConfig().set("messages.no-permission", null);
            main.getConfig().set("config-version", 1.2);
            main.saveConfig();
        }
        if (main.getConfig().getDouble("config-version") < 1.3) {
            if (!main.getConfig().isSet("minimum-factions-role")) {
                main.getConfig().set("minimum-factions-role", "any");
            }
            if (!main.getConfig().isSet("messages.not-minimum-role")) {
                main.getConfig().set("messages.not-minimum-role", "&cYou must be a faction (insert role) to use chunkbusters.");
            }
            main.getConfig().set("config-version", 1.3);
            main.saveConfig();
        }
        if (main.getConfig().getDouble("config-version") < 1.4) {
            if (!main.getConfig().isSet("confirm-gui.fill-material")) {
                main.getConfig().set("confirm-gui.fill-material", "AIR");
            }
            if (!main.getConfig().isSet("confirm-gui.fill-name")) {
                main.getConfig().set("confirm-gui.fill-name", "");
            }
            if (!main.getConfig().isSet("confirm-gui.fill-lore")) {
                main.getConfig().set("confirm-gui.fill-lore", new ArrayList<String>());
            }
            if (!main.getConfig().isSet("warmup.seconds")) {
                if (main.getConfig().isSet("chunkbuster-warmup")) {
                    main.getConfig().set("warmup.seconds", main.getConfig().get("chunkbuster-warmup"));
                } else {
                    main.getConfig().set("warmup.seconds", 0);
                }
            }
            if (!main.getConfig().isSet("warmup.send-message-every-second")) {
                if (main.getConfig().isSet("warmup-message-every-second")) {
                    main.getConfig().set("warmup.send-message-every-second", main.getConfig().get("warmup-message-every-second"));
                    main.getConfig().set("warmup-message-every-second", null);
                } else {
                    main.getConfig().set("warmup.send-message-every-second", true);
                }
            }
            if (!main.getConfig().isSet("warmup.sound-enabled")) {
                main.getConfig().set("warmup.sound-enabled", false);
            }
            if (!main.getConfig().isSet("warmup.sound")) {
                main.getConfig().set("warmup.sound", "random.levelup");
            }
            if (!main.getConfig().isSet("warmup.sound-volume")) {
                main.getConfig().set("warmup.sound-volume", 1.0);
            }
            if (!main.getConfig().isSet("warmup.sound-pitch")) {
                main.getConfig().set("warmup.sound-pitch", 1.0);
            }
            if (!main.getConfig().isSet("warmup.sound-interval-seconds")) {
                main.getConfig().set("warmup.sound-interval-seconds", 1);
            }
            main.getConfig().set("chunkbuster-warmup", null);
            main.getConfig().set("config-version", 1.4);
            main.saveConfig();
        }
        if (main.getConfig().getDouble("config-version") < 1.5) {
            if (!main.getConfig().isSet("warmup.warmup-sound-enabled")) {
                if (main.getConfig().isSet("warmup.sound-enabled")) {
                    main.getConfig().set("warmup.warmup-sound-enabled", main.getConfig().get("warmup.sound-enabled"));
                } else {
                    main.getConfig().set("warmup.warmup-sound-enabled", false);
                }
            }
            if (!main.getConfig().isSet("warmup.warmup-sound")) {
                if (main.getConfig().isSet("warmup.sound")) {
                    main.getConfig().set("warmup.warmup-sound", main.getConfig().get("warmup.sound"));
                } else {
                    main.getConfig().set("warmup.warmup-sound", "random.levelup");
                }
            }
            if (!main.getConfig().isSet("warmup.warmup-sound-volume")) {
                if (main.getConfig().isSet("warmup.sound-volume")) {
                    main.getConfig().set("warmup.warmup-sound-volume", main.getConfig().get("warmup.sound-volume"));
                } else {
                    main.getConfig().set("warmup.warmup-sound-volume", 1.0);
                }
            }
            if (!main.getConfig().isSet("warmup.warmup-sound-pitch")) {
                if (main.getConfig().isSet("warmup.sound-pitch")) {
                    main.getConfig().set("warmup.warmup-sound-pitch", main.getConfig().get("warmup.sound-pitch"));
                } else {
                    main.getConfig().set("warmup.warmup-sound-pitch", 1.0);
                }
            }
            if (!main.getConfig().isSet("warmup.warmup-sound-interval")) {
                if (main.getConfig().isSet("warmup.sound-interval-seconds")) {
                    main.getConfig().set("warmup.warmup-sound-interval", main.getConfig().get("warmup.sound-interval-seconds"));
                } else {
                    main.getConfig().set("warmup.warmup-sound-interval", 1);
                }
            }
            if (!main.getConfig().isSet("warmup.clearing-sound-enabled")) {
                main.getConfig().set("warmup.clearing-sound-enabled", false);
            }
            if (!main.getConfig().isSet("warmup.clearing-sound")) {
                main.getConfig().set("warmup.clearing-sound", "random.levelup");
            }
            if (!main.getConfig().isSet("warmup.clearing-volume")) {
                main.getConfig().set("warmup.clearing-volume", 1.0);
            }
            if (!main.getConfig().isSet("warmup.clearing-pitch")) {
                main.getConfig().set("warmup.clearing-pitch", 1.0);
            }
            if (!main.getConfig().isSet("confirm-gui.confirm-sound-enabled")) {
                main.getConfig().set("confirm-gui.confirm-sound-enabled", false);
            }
            if (!main.getConfig().isSet("confirm-gui.confirm-sound")) {
                main.getConfig().set("confirm-gui.confirm-sound", "random.levelup");
            }
            if (!main.getConfig().isSet("confirm-gui.confirm-volume")) {
                main.getConfig().set("confirm-gui.confirm-volume", 1.0);
            }
            if (!main.getConfig().isSet("confirm-gui.confirm-pitch")) {
                main.getConfig().set("confirm-gui.confirm-pitch", 1.0);
            }
            if (!main.getConfig().isSet("confirm-gui.cancel-sound-enabled")) {
                main.getConfig().set("confirm-gui.cancel-sound-enabled", false);
            }
            if (!main.getConfig().isSet("confirm-gui.cancel-sound")) {
                main.getConfig().set("confirm-gui.cancel-sound", "mob.villager.hit");
            }
            if (!main.getConfig().isSet("confirm-gui.cancel-volume")) {
                main.getConfig().set("confirm-gui.cancel-volume", 1.0);
            }
            if (!main.getConfig().isSet("confirm-gui.cancel-pitch")) {
                main.getConfig().set("confirm-gui.cancel-pitch", 1.0);
            }
            if (!main.getConfig().isSet("messages.gui-cancel")) {
                main.getConfig().set("messages.gui-cancel", "&cThe chunkbuster has been cancelled.");
            }
            if (!main.getConfig().isSet("ignored-materials")) {
                main.getConfig().set("ignored-materials", new ArrayList<String>().add("BEDROCK"));
            }
            if (!main.getConfig().isSet("cooldown")) {
                main.getConfig().set("cooldown", 0);
            }
            main.getConfig().set("warmup.sound-enabled", null);
            main.getConfig().set("warmup.sound", null);
            main.getConfig().set("warmup.sound-volume", null);
            main.getConfig().set("warmup.sound-pitch", null);
            main.getConfig().set("warmup.sound-interval-seconds", null);
            main.getConfig().set("config-version", 1.5);
            main.saveConfig();
        }
        if (main.getConfig().getDouble("config-version") < 1.6) {
            if (!main.getConfig().isSet("show-update-messages")) {
                main.getConfig().set("show-update-messages", true);
            }
            main.getConfig().set("config-version", 1.6);
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
            ArrayList<Integer> newestVersionNumbers = new ArrayList<>();
            try {
                for (String s : newestVersion.split(Pattern.quote("."))) {
                    newestVersionNumbers.add(Integer.parseInt(s));
                }
            } catch (Exception ex) {
                return;
            }
            if (newestVersionNumbers.size() != 3) {
                return;
            }
            ArrayList<Integer> thisVersionNumbers = new ArrayList<>();
            try {
                for (String s : main.getDescription().getVersion().split(Pattern.quote("."))) {
                    thisVersionNumbers.add(Integer.parseInt(s));
                }
            } catch (Exception ex) {
                return;
            }
            if (newestVersionNumbers.get(0) > thisVersionNumbers.get(0) || newestVersionNumbers.get(1) > thisVersionNumbers.get(1) || newestVersionNumbers.get(2) > thisVersionNumbers.get(2)) {
                TextComponent one = new TextComponent("A new version of ChunkBuster, " + newestVersion + " is available. Download it by clicking here.");
                one.setColor(ChatColor.RED);
                one.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/chunkbuster-1-8-1-12-clear-any-chunk-area.60057/"));
                p.spigot().sendMessage(one);
            }
        } catch (Exception ignored) {}
    }

    public HashSet<Chunk> getWaterChunks() { return waterChunks; }
}
