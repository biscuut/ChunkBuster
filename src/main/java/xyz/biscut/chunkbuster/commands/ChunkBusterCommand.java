package xyz.biscut.chunkbuster.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.biscut.chunkbuster.ChunkBuster;

import java.util.HashMap;

public class ChunkBusterCommand implements CommandExecutor {

    private ChunkBuster main;

    public ChunkBusterCommand(ChunkBuster main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("chunkbuster.admin") || sender.isOp()) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "give":
                        if (args.length > 1) {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (p != null) {
                                int chunkArea;
                                if (args.length > 2) {
                                    try {
                                        chunkArea = Integer.parseInt(args[2]);
                                    } catch (NumberFormatException ex) {
                                        sender.sendMessage(ChatColor.RED + "This isn't a valid number!");
                                        return false;
                                    }
                                    if (chunkArea > 0 && chunkArea % 2 != 0) { // Is positive and odd number
                                        int giveAmount = 1;
                                        if (args.length > 3) {
                                            try {
                                                giveAmount = Integer.parseInt(args[3]);
                                            } catch (NumberFormatException ex) {
                                                sender.sendMessage(ChatColor.RED + "This isn't a valid number!");
                                                return false;
                                            }
                                        }
                                        ItemStack item = new ItemStack(main.getConfigValues().getChunkBusterMaterial(), giveAmount, main.getConfigValues().getChunkBusterDamage());
                                        ItemMeta itemMeta = item.getItemMeta();
                                        itemMeta.setDisplayName(main.getConfigValues().getChunkBusterName());
                                        itemMeta.setLore(main.getConfigValues().getChunkBusterLore(chunkArea));
                                        item.setItemMeta(itemMeta);
                                        item = main.getUtils().addGlow(item, chunkArea);
                                        HashMap excessItems;
                                        if (!main.getConfigValues().dropFullInv()) {
                                            if (giveAmount < 65) {
                                                if (p.getInventory().firstEmpty() == -1) {
                                                    sender.sendMessage(ChatColor.RED + "This player doesn't have any empty slots in their inventory!");
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "You can only give 64 at a time!");
                                                return true;
                                            }
                                        }
                                        excessItems = p.getInventory().addItem(item);
                                        for (Object excessItem : excessItems.values()) {
                                            int itemCount = ((ItemStack)excessItem).getAmount();
                                            while (itemCount > 64) {
                                                ((ItemStack) excessItem).setAmount(64);
                                                p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack)excessItem);
                                                itemCount = itemCount - 64;
                                            }
                                            if (itemCount > 0) {
                                                ((ItemStack) excessItem).setAmount(itemCount);
                                                p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack)excessItem);
                                            }
                                        }
                                        if (!main.getConfigValues().getGiveMessage(p, giveAmount).equals("")) {
                                            sender.sendMessage(main.getConfigValues().getGiveMessage(p, giveAmount));
                                        }
                                        if (!main.getConfigValues().getReceiveMessage(giveAmount).equals("")) {
                                            p.sendMessage(main.getConfigValues().getReceiveMessage(giveAmount));
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "The area must be greater than 0 and be an odd number!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Please specify the chunk area!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "This player is not online!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Please specify a player!");
                        }
                        break;
                    case "reload":
                        main.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded the config. Most values have been instantly updated.");
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Invalid argument!");
                }
            } else {
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------" + ChatColor.GRAY +"[" + ChatColor.GREEN + ChatColor.BOLD + " ChunkBuster " + ChatColor.GRAY + "]" + ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------");
                sender.sendMessage(ChatColor.GREEN + "● /cb give <player> <chunk-area> [amount] " + ChatColor.GRAY + "- Give a player a chunk buster");
                sender.sendMessage(ChatColor.GREEN + "● /cb reload " + ChatColor.GRAY + "- Reload the config");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "v" + main.getDescription().getVersion() + " by Biscut");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------------------");
            }
        } else {
            if (!main.getConfigValues().getNoPermissionMessageCommand().equals("")) {
                sender.sendMessage(main.getConfigValues().getNoPermissionMessageCommand());
            }
        }
        return false;
    }
}
