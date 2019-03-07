package codes.biscuit.chunkbuster.commands;

import codes.biscuit.chunkbuster.ChunkBuster;
import codes.biscuit.chunkbuster.utils.ConfigValues;
import codes.biscuit.chunkbuster.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChunkBusterCommand implements TabExecutor {

    private ChunkBuster main;

    public ChunkBusterCommand(ChunkBuster main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>(Arrays.asList("give", "reload", "water"));
            for (String arg : Arrays.asList("give", "reload", "water")) {
                if (!arg.startsWith(args[0].toLowerCase())) {
                    arguments.remove(arg);
                }
            }
            return arguments;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "give":
                    if (sender.hasPermission("chunkbuster.give") || sender.hasPermission("chunkbuster.admin")) {
                        if (args.length > 1) {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (p != null) {
                                int chunkArea;
                                if (args.length > 2) {
                                    try {
                                        chunkArea = Integer.parseInt(args[2]);
                                    } catch (NumberFormatException ex) {
                                        sender.sendMessage(Utils.color("&cThis isn't a valid number!"));
                                        return false;
                                    }
                                    if (chunkArea > 0 && chunkArea % 2 != 0) { // Is positive and odd number
                                        int giveAmount = 1;
                                        if (args.length > 3) {
                                            try {
                                                giveAmount = Integer.parseInt(args[3]);
                                            } catch (NumberFormatException ex) {
                                                sender.sendMessage(Utils.color("&cThis isn't a valid number!"));
                                                return false;
                                            }
                                        }
                                        ItemStack item = main.getUtils().getChunkBusterItem(giveAmount, chunkArea);
                                        HashMap excessItems;
                                        if (!main.getConfigValues().dropFullInv()) {
                                            if (giveAmount < 65) {
                                                if (p.getInventory().firstEmpty() == -1) {
                                                    sender.sendMessage(Utils.color("&cThis player doesn't have any empty slots in their inventory!"));
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage(Utils.color("&cYou can only give 64 at a time!"));
                                                return true;
                                            }
                                        }
                                        excessItems = p.getInventory().addItem(item);
                                        for (Object excessItem : excessItems.values()) {
                                            int itemCount = ((ItemStack) excessItem).getAmount();
                                            while (itemCount > 64) {
                                                ((ItemStack) excessItem).setAmount(64);
                                                p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack) excessItem);
                                                itemCount = itemCount - 64;
                                            }
                                            if (itemCount > 0) {
                                                ((ItemStack) excessItem).setAmount(itemCount);
                                                p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack) excessItem);
                                            }
                                        }
                                        main.getUtils().sendMessage(p, ConfigValues.Message.GIVE, p.getName(), giveAmount);
                                        main.getUtils().sendMessage(p, ConfigValues.Message.RECEIVE, giveAmount);
                                    } else {
                                        sender.sendMessage(Utils.color("&cThe area must be greater than 0 and be an odd number!"));
                                    }
                                } else {
                                    sender.sendMessage(Utils.color("&cPlease specify the chunk area!"));
                                }
                            } else {
                                sender.sendMessage(Utils.color("&cThis player is not online!"));
                            }
                        } else {
                            sender.sendMessage(Utils.color("&cPlease specify a player!"));
                        }
                    } else {
                        main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    }
                    break;
                case "reload":
                    if (sender.hasPermission("chunkbuster.reload") || sender.hasPermission("chunkbuster.admin")) {
                        main.reloadConfig();
                        sender.sendMessage(Utils.color("&aSuccessfully reloaded the config. Most values have been instantly updated."));
                    } else {
                        main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    }
                    break;
                case "water":
                    if (sender.hasPermission("chunkbuster.water") || sender.hasPermission("chunkbuster.admin")) {
                        if (sender instanceof Player) {
                            if (main.getUtils().getWaterChunks().contains(((Player)sender).getLocation().getChunk())) {
                                main.getUtils().getWaterChunks().remove(((Player)sender).getLocation().getChunk());
                                sender.sendMessage(Utils.color("&aWater can now flow normally into this chunk."));
                            } else {
                                sender.sendMessage(Utils.color("&cThis chunk doesn't have water flow disabled!"));
                            }
                        } else {
                            sender.sendMessage(Utils.color("&cYou cannot use this command from here!"));
                        }
                    } else {
                        main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    }
                    break;
                default:
                    sender.sendMessage(Utils.color("&cInvalid argument!"));
            }
        } else {
            sender.sendMessage(Utils.color("&7&m--------------&7[&a&l ChunkBuster &7]&7&m--------------"));
            sender.sendMessage(Utils.color("&a● /cb give <player> <chunk-area> [amount] &7- Give a player a chunk buster"));
            sender.sendMessage(Utils.color("&a● /cb reload &7- Reload the config"));
            sender.sendMessage(Utils.color("&a● /cb water &7- &e[DEBUG] &7 Allow water to flow normally in your current chunk"));
            sender.sendMessage(Utils.color("&7&ov" + main.getDescription().getVersion() + " by Biscut"));
            sender.sendMessage(Utils.color("&7&m-------------------------------------------"));
        }
        return false;
    }
}
