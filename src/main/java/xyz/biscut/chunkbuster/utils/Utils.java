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

public class Utils {

    private ChunkBuster main;

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

    public void clearChunks(int chunkBusterDiameter, Location chunkBusterLocation, Player p) {
        if (chunkBusterDiameter == 1) {
            for (int y = 127; y > -1; y--) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block b = chunkBusterLocation.getChunk().getBlock(x, y, z);
                        if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR)) {
                            main.getRemovalQueue().add(b);
                        }
                    }
                }
            }
            p.sendMessage(main.getConfigValues().getClearingMessage());
        } else if (chunkBusterDiameter == 3) {
            int startingX = chunkBusterLocation.getChunk().getX();
            int startingZ = chunkBusterLocation.getChunk().getZ();
            for (int y = 127; y > -1; y--) {
                for (int xC = -1; xC < 2; xC++) {
                    for (int zC = -1; zC < 2; zC++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(startingX + xC, startingZ + zC);
                        Location chunkCheckLoc = chunk.getBlock(7, 63, 7).getLocation();
                        if (main.getHookUtils().compareLocPlayerFaction(chunkCheckLoc, p) || main.getHookUtils().isWilderness(chunkCheckLoc)) {
                            if (main.getHookUtils().isWilderness(chunkCheckLoc) && !main.getConfigValues().canPlaceInWilderness()) {
                                continue;
                            }
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    Block b = chunk.getBlock(x, y, z);
                                    if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR)) {
                                        main.getRemovalQueue().add(b);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            p.sendMessage(main.getConfigValues().getClearingMessage());
        } else if (chunkBusterDiameter == 5) {
            int startingX = chunkBusterLocation.getChunk().getX();
            int startingZ = chunkBusterLocation.getChunk().getZ();
            for (int y = 127; y > -1; y--) {
                for (int xC = -2; xC < 3; xC++) {
                    for (int zC = -2; zC < 3; zC++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(startingX + xC, startingZ + zC);
                        Location chunkCheckLoc = chunk.getBlock(7, 63, 7).getLocation();
                        if (main.getHookUtils().compareLocPlayerFaction(chunkCheckLoc, p) || main.getHookUtils().isWilderness(chunkCheckLoc)) {
                            if (main.getHookUtils().isWilderness(chunkCheckLoc) && !main.getConfigValues().canPlaceInWilderness()) {
                                continue;
                            }
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    Block b = chunk.getBlock(x, y, z);
                                    if (!b.getType().equals(Material.BEDROCK) && !b.getType().equals(Material.AIR)) {
                                        main.getRemovalQueue().add(b);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            p.sendMessage(main.getConfigValues().getClearingMessage());
        } else {
            p.sendMessage(ChatColor.RED + "Invalid chunk buster!");
        }
    }
}
