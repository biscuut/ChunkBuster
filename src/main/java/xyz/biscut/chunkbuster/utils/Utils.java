package xyz.biscut.chunkbuster.utils;

import net.md_5.bungee.api.ChatColor;
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

import java.util.ArrayList;
import java.util.HashSet;

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
        if (chunkBusterArea == 1) {
            RemovalQueue removalQueue = new RemovalQueue(main);
            waterChunks.add(chunkBusterLocation.getChunk());
            for (int y = chunkBusterLocation.getWorld().getMaxHeight(); y >= 0; y--) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block b = chunkBusterLocation.getChunk().getBlock(x, y, z);
                        if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR)) {
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
                                    if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR)) {
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
                    main.getConfig().set("messages.no-permission", null);
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
                    main.getConfig().set("chunkbuster-warmup", null);
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
            if (!main.getConfig().isSet("warmup.sound-interval-seconds")) {
                main.getConfig().set("warmup.sound-interval-seconds", 1);
            }
            if (!main.getConfig().isSet("warmup.sound-volume")) {
                main.getConfig().set("warmup.sound-volume", 1.0);
            }
            if (!main.getConfig().isSet("warmup.sound-pitch")) {
                main.getConfig().set("warmup.sound-pitch", 1.0);
            }
            main.getConfig().set("config-version", 1.4);
            main.saveConfig();
        }
    }

    public HashSet<Chunk> getWaterChunks() { return waterChunks; }
}
