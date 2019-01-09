package codes.biscuit.chunkbuster.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import codes.biscuit.chunkbuster.ChunkBuster;
import codes.biscuit.chunkbuster.timers.MessageTimer;
import codes.biscuit.chunkbuster.timers.SoundTimer;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvents implements Listener {

    private ChunkBuster main;
    private Map<Player, Location> chunkBusterLocations = new HashMap<>();
    private Map<Player, Long> playerCooldowns = new HashMap<>();

    public PlayerEvents(ChunkBuster main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) //TODO experimental, test this more
    public void onChunkBusterPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType().equals(main.getConfigValues().getChunkBusterMaterial()) && e.getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LURE) > 0) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            if (p.hasPermission("chunkbuster.use")) {
                if (main.getHookUtils().hasFaction(p)) {
                    if (main.getHookUtils().checkRole(p)) {
                        if (main.getHookUtils().compareLocToPlayer(e.getBlock().getLocation(), p) || main.getHookUtils().isWilderness(e.getBlock().getLocation())) { //TODO remove this check
                            if (main.getHookUtils().isWilderness(e.getBlock().getLocation()) && !main.getConfigValues().canPlaceInWilderness()) {
                                if (!main.getConfigValues().getOnlyClaimMessage().equals("")) {
                                    p.sendMessage(main.getConfigValues().getOnlyClaimMessage());
                                }
                            } else {
                                if (main.getConfigValues().getCooldown() > 0) {
                                    if (playerCooldowns.containsKey(p)) {
                                        if (System.currentTimeMillis() < playerCooldowns.get(p)) {
                                            long longDifference = playerCooldowns.get(e.getPlayer()) - System.currentTimeMillis();
                                            int secondsDifference = (int) (longDifference / 1000);
                                            int seconds = secondsDifference % 60;
                                            int minutes = secondsDifference / 60;
                                            p.sendMessage(main.getConfigValues().getCooldownMessage(minutes, seconds));
                                            return;
                                        }
                                    }
                                }
                                chunkBusterLocations.put(p, e.getBlock().getLocation());
                                if (!p.getOpenInventory().getTitle().contains(main.getConfigValues().getGUITitle())) {
                                    Inventory confirmInv = Bukkit.createInventory(null, 9 * main.getConfigValues().getGUIRows(), main.getConfigValues().getGUITitle());
                                    ItemStack acceptItem = main.getConfigValues().getConfirmBlockItemStack();
                                    if (!acceptItem.getType().equals(Material.AIR)) {
                                        ItemMeta acceptItemMeta = acceptItem.getItemMeta();
                                        acceptItemMeta.setDisplayName(main.getConfigValues().getConfirmName());
                                        acceptItemMeta.setLore(main.getConfigValues().getConfirmLore());
                                        acceptItem.setItemMeta(acceptItemMeta);
                                    }
                                    ItemStack cancelItem = main.getConfigValues().getCancelBlockItemStack();
                                    if (!cancelItem.getType().equals(Material.AIR)) {
                                        ItemMeta cancelItemMeta = cancelItem.getItemMeta();
                                        cancelItemMeta.setDisplayName(main.getConfigValues().getCancelName());
                                        cancelItemMeta.setLore(main.getConfigValues().getCancelLore());
                                        cancelItem.setItemMeta(cancelItemMeta);
                                    }
                                    ItemStack fillItem = main.getConfigValues().getFillItemStack();
                                    if (!fillItem.getType().equals(Material.AIR)) {
                                        ItemMeta fillItemMeta = fillItem.getItemMeta();
                                        fillItemMeta.setDisplayName(main.getConfigValues().getFillName());
                                        fillItemMeta.setLore(main.getConfigValues().getFillLore());
                                        fillItem.setItemMeta(fillItemMeta);
                                    }
                                    int slotCounter = 1;
                                    for (int i = 0; i < 9 * main.getConfigValues().getGUIRows(); i++) {
                                        if (slotCounter < 5) {
                                            confirmInv.setItem(i, acceptItem);
                                        } else if (slotCounter > 5) {
                                            confirmInv.setItem(i, cancelItem);
                                        } else {
                                            confirmInv.setItem(i, fillItem);
                                        }
                                        if (slotCounter >= 9) {
                                            slotCounter = 1;
                                        } else {
                                            slotCounter++;
                                        }
                                    }
                                    p.openInventory(confirmInv);
                                }
                            }
                        } else {
                            if (main.getConfigValues().canPlaceInWilderness()) {
                                if (!main.getConfigValues().getOnlyWildernessClaimMessage().equals("")) {
                                    p.sendMessage(main.getConfigValues().getOnlyWildernessClaimMessage());
                                }
                            } else {
                                if (!main.getConfigValues().getOnlyClaimMessage().equals("")) {
                                    p.sendMessage(main.getConfigValues().getOnlyClaimMessage());
                                }
                            }
                        }
                    } else {
                        if (!main.getConfigValues().getMinimumRoleMessage().equals("")) {
                            p.sendMessage(main.getConfigValues().getMinimumRoleMessage());
                        }
                    }
                } else {
                    if (!main.getConfigValues().getNoFactionMessage().equals("")) {
                        p.sendMessage(main.getConfigValues().getNoFactionMessage());
                    }
                }
            } else {
                if (!main.getConfigValues().getNoPermissionMessagePlace().equals("")) {
                    p.sendMessage(main.getConfigValues().getNoPermissionMessagePlace());
                }
            }
        }
    }

    @EventHandler
    public void onConfirmClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null &&
                e.getClickedInventory().getName().equals(main.getConfigValues().getGUITitle())) {
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            Location chunkBusterLocation = chunkBusterLocations.get(e.getWhoClicked());
            if (chunkBusterLocation != null) {
                if (p.getItemInHand() != null && p.getItemInHand().getType().equals(main.getConfigValues().getChunkBusterMaterial()) && p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LURE) > 0) {
                    int chunkBusterDiameter = e.getWhoClicked().getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LURE);
                    if (main.getHookUtils().hasFaction(p)) {
                        if (main.getHookUtils().checkRole(p)) {
                            if (main.getHookUtils().compareLocToPlayer(chunkBusterLocation, p) || main.getHookUtils().isWilderness(chunkBusterLocation)) {
                                if (main.getHookUtils().isWilderness(chunkBusterLocation) && !main.getConfigValues().canPlaceInWilderness()) {
                                    if (!main.getConfigValues().getOnlyClaimMessage().equals("")) {
                                        p.sendMessage(main.getConfigValues().getOnlyClaimMessage());
                                    }
                                } else {
                                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(main.getConfigValues().getConfirmName())) {
                                        playerCooldowns.put(p, System.currentTimeMillis() + (1000 * main.getConfigValues().getCooldown()));
                                        chunkBusterLocations.remove(e.getWhoClicked());
                                        e.getWhoClicked().closeInventory();
                                        if (main.getConfigValues().confirmSoundEnabled()) {
                                            p.playSound(p.getLocation(), main.getConfigValues().getConfirmSoundString(), main.getConfigValues().getConfirmSoundVolume(), main.getConfigValues().getConfirmSoundPitch());
                                        }
                                        if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)) {
                                            if (p.getItemInHand().getAmount() <= 1) {
                                                p.getInventory().setItemInHand(null);
                                            } else {
                                                ItemStack chunkBusterItem = p.getItemInHand();
                                                chunkBusterItem.setAmount(p.getItemInHand().getAmount() - 1);
                                                p.getInventory().setItemInHand(chunkBusterItem);
                                            }
                                        }
                                        if (main.getConfigValues().getChunkBusterWarmup() > 0) {
                                            int seconds = main.getConfigValues().getChunkBusterWarmup();
                                            new MessageTimer(seconds, p, main).runTaskTimer(main, 0L, 20L);
                                            if (main.getConfigValues().warmupSoundEnabled()) {
                                                new SoundTimer(main, p, (int)((double)seconds / main.getConfigValues().getWarmupSoundInterval())).runTaskTimer(main, 0L, 20L * main.getConfigValues().getWarmupSoundInterval());
                                            }
                                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                                if (main.getConfigValues().clearingSoundEnabled()) {
                                                    p.playSound(p.getLocation(), main.getConfigValues().getClearingSoundString(), main.getConfigValues().getClearingSoundVolume(), main.getConfigValues().getClearingSoundPitch());
                                                }
                                                main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                            }, 20L * seconds);
                                        } else {
                                            if (main.getConfigValues().clearingSoundEnabled()) {
                                                p.playSound(p.getLocation(), main.getConfigValues().getClearingSoundString(), main.getConfigValues().getClearingSoundVolume(), main.getConfigValues().getClearingSoundPitch());
                                            }
                                            main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                        }
                                    } else if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(main.getConfigValues().getCancelName())) {
                                        chunkBusterLocations.remove(e.getWhoClicked());
                                        e.getWhoClicked().closeInventory();
                                        if (main.getConfigValues().cancelSoundEnabled()) {
                                            p.playSound(p.getLocation(), main.getConfigValues().getCancelSoundString(), main.getConfigValues().getCancelSoundVolume(), main.getConfigValues().getCancelSoundPitch());
                                        }
                                        if (!main.getConfigValues().getGUICancelMessage().equals("")) {
                                            p.sendMessage(main.getConfigValues().getGUICancelMessage());
                                        }
                                    }
                                }
                            } else {
                                if (main.getConfigValues().canPlaceInWilderness()) {
                                    if (!main.getConfigValues().getOnlyWildernessClaimMessage().equals("")) {
                                        p.sendMessage(main.getConfigValues().getOnlyWildernessClaimMessage());
                                    }
                                } else {
                                    if (!main.getConfigValues().getOnlyClaimMessage().equals("")) {
                                        p.sendMessage(main.getConfigValues().getOnlyClaimMessage());
                                    }
                                }
                            }
                        } else {
                            if (!main.getConfigValues().getMinimumRoleMessage().equals("")) {
                                p.sendMessage(main.getConfigValues().getMinimumRoleMessage());
                            }
                        }
                    } else {
                        if (!main.getConfigValues().getNoFactionMessage().equals("")) {
                            p.sendMessage(main.getConfigValues().getNoFactionMessage());
                        }
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "Error, please re-place your chunk buster.");
            }
        }
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        if (e.getInventory().getName().contains(main.getConfigValues().getGUITitle())) {
            chunkBusterLocations.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (main.getConfigValues().showUpdateMessage() && e.getPlayer().isOp()) {
            main.getUtils().checkUpdates(e.getPlayer());
        }
    }
}
