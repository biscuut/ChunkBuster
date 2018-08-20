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

    public void clearChunks(int chunkBusterArea, Location chunkBusterLocation, Player p) {
        if (chunkBusterArea == 1) {
            main.getWaterChunks().add(chunkBusterLocation.getChunk());
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
        } else if (chunkBusterArea > 2 && chunkBusterArea % 2 != 0) {
            int upperSearchBound = ((chunkBusterArea - 1) / 2) + 1;
            int lowerSearchBound = (chunkBusterArea - 1) / -2;
            int startingX = chunkBusterLocation.getChunk().getX();
            int startingZ = chunkBusterLocation.getChunk().getZ();
            for (int y = 127; y > -1; y--) {
                for (int chunkX = lowerSearchBound; chunkX < upperSearchBound; chunkX++) {
                    for (int chunkZ = lowerSearchBound; chunkZ < upperSearchBound; chunkZ++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(startingX + chunkX, startingZ + chunkZ);
                        Location chunkCheckLoc = chunk.getBlock(7, 63, 7).getLocation();
                        if (main.getHookUtils().compareLocToPlayer(chunkCheckLoc, p) || main.getHookUtils().isWilderness(chunkCheckLoc)) {
                            if (main.getHookUtils().isWilderness(chunkCheckLoc) && !main.getConfigValues().canPlaceInWilderness()) {
                                continue;
                            }
                            main.getWaterChunks().add(chunk);
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

    public void updateConfig(ChunkBuster main) {
        if (main.getConfig().getDouble("config-version") < 1.1) {
            if (!main.getConfig().isSet("messages.region-protected")) {
                main.getConfig().set("messages.region-protected", "&cYou cannot place chunk busters in this region!");
            }
            main.getConfig().set("config-version", 1.1);
            main.saveConfig();
        }
    }
}
