package codes.biscuit.chunkbuster.events;

import codes.biscuit.chunkbuster.ChunkBuster;
import codes.biscuit.chunkbuster.nbt.NBTItem;
import codes.biscuit.chunkbuster.timers.MessageTimer;
import codes.biscuit.chunkbuster.timers.SoundTimer;
import codes.biscuit.chunkbuster.utils.ConfigValues;
import codes.biscuit.chunkbuster.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvents implements Listener {

    private ChunkBuster main;
    private Map<OfflinePlayer, Location> chunkBusterLocations = new HashMap<>();
    private Map<OfflinePlayer, Long> playerCooldowns = new HashMap<>();
    private Map<OfflinePlayer, Long> noFallDamage = new HashMap<>();

    public PlayerEvents(ChunkBuster main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkBusterPlace(BlockPlaceEvent e) {
        NBTItem nbtItem = new NBTItem(e.getItemInHand());
        if (e.getItemInHand().getType().equals(main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius")) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            if (p.hasPermission("chunkbuster.use")) {
                if (main.getHookUtils().hasFaction(p)) {
                    if (main.getHookUtils().checkRole(p)) {
                        if (main.getHookUtils().compareLocToPlayer(e.getBlock().getLocation(), p)) {
                            if (main.getConfigValues().getCooldown() > 0) {
                                if (playerCooldowns.containsKey(p)) {
                                    if (System.currentTimeMillis() < playerCooldowns.get(p)) {
                                        long longDifference = playerCooldowns.get(e.getPlayer()) - System.currentTimeMillis();
                                        int secondsDifference = (int) (longDifference / 1000);
                                        int seconds = secondsDifference % 60;
                                        int minutes = secondsDifference / 60;
                                        main.getUtils().sendMessage(p, ConfigValues.Message.COOLDOWN, minutes, seconds);
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
                        } else {
                            main.getUtils().sendMessage(p, ConfigValues.Message.CANNOT_PLACE);
                        }
                    } else {
                        main.getUtils().sendMessage(p, ConfigValues.Message.NOT_MINIMUM_ROLE);
                    }
                } else {
                    main.getUtils().sendMessage(p, ConfigValues.Message.NO_FACTION);
                }
            } else {
                main.getUtils().sendMessage(p, ConfigValues.Message.NO_PERMISSION_PLACE);
            }
        }
    }

    @EventHandler
    public void onConfirmClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getView().getTitle() != null &&
                e.getView().getTitle().equals(main.getConfigValues().getGUITitle())) {
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            Location chunkBusterLocation = chunkBusterLocations.get(p);
            if (chunkBusterLocation != null) {
                if (main.getHookUtils().hasFaction(p)) {
                    if (main.getHookUtils().checkRole(p)) {
                        if (main.getHookUtils().compareLocToPlayer(chunkBusterLocation, p) || main.getHookUtils().isWilderness(chunkBusterLocation)) {
                            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(main.getConfigValues().getConfirmName())) {
                                int itemSlot = -1;
                                NBTItem nbtItem = new NBTItem(p.getItemInHand());
                                if (p.getItemInHand() != null && (p.getItemInHand().getType().equals(main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius"))) {
                                    itemSlot = p.getInventory().getHeldItemSlot();
                                } else {
                                    for (int i = 0; i <= 40; i++) { // 40 should fix the offhand issue.
                                        ItemStack currentItem;
                                        try {
                                            currentItem = p.getInventory().getItem(i);
                                        } catch (IndexOutOfBoundsException ex) {
                                            continue;
                                        }
                                        nbtItem = new NBTItem(currentItem);
                                        if (currentItem != null && currentItem.getType().equals(main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius")) {
                                            itemSlot = i;
                                            break;
                                        }
                                    }
                                    if (itemSlot == -1) {
                                        chunkBusterLocations.remove(p);
                                        p.closeInventory();
                                        if (main.getConfigValues().cancelSoundEnabled()) {
                                            p.playSound(p.getLocation(), main.getConfigValues().getCancelSoundString(), main.getConfigValues().getCancelSoundVolume(), main.getConfigValues().getCancelSoundPitch());
                                        }
                                        main.getUtils().sendMessage(p, ConfigValues.Message.NO_ITEM);
                                        return;
                                    }
                                }
                                ItemStack checkItem = p.getInventory().getItem(itemSlot);
                                nbtItem = new NBTItem(checkItem);
                                int chunkBusterDiameter = nbtItem.getInteger("chunkbuster.radius");
                                playerCooldowns.put(p, System.currentTimeMillis() + (1000 * main.getConfigValues().getCooldown()));
                                chunkBusterLocations.remove(p);
                                p.closeInventory();
                                if (main.getConfigValues().confirmSoundEnabled()) {
                                    p.playSound(p.getLocation(), main.getConfigValues().getConfirmSoundString(), main.getConfigValues().getConfirmSoundVolume(), main.getConfigValues().getConfirmSoundPitch());
                                }
                                if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    if (checkItem.getAmount() <= 1) {
                                        p.getInventory().setItem(itemSlot, null);
                                    } else {
                                        checkItem.setAmount(checkItem.getAmount() - 1);
                                        p.getInventory().setItem(itemSlot, checkItem);
                                    }
                                }
                                if (main.getConfigValues().getChunkBusterWarmup() > 0) {
                                    int seconds = main.getConfigValues().getChunkBusterWarmup();
                                    new MessageTimer(seconds, p.getUniqueId(), main).runTaskTimer(main, 0L, 20L);
                                    if (main.getConfigValues().warmupSoundEnabled()) {
                                        new SoundTimer(main, p, (int)((double)seconds / main.getConfigValues().getWarmupSoundInterval())).runTaskTimer(main, 0L, 20L * main.getConfigValues().getWarmupSoundInterval());
                                    }
                                    Bukkit.getScheduler().runTaskLater(main, () -> {
                                        if (main.getConfigValues().clearingSoundEnabled()) {
                                            p.playSound(p.getLocation(), main.getConfigValues().getClearingSoundString(), main.getConfigValues().getClearingSoundVolume(), main.getConfigValues().getClearingSoundPitch());
                                        }
                                        main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                        if (main.getConfigValues().getNoFallMillis() > 0) {
                                            noFallDamage.put(p, System.currentTimeMillis() + main.getConfigValues().getNoFallMillis());
                                        }
                                    }, 20L * seconds);
                                } else {
                                    if (main.getConfigValues().clearingSoundEnabled()) {
                                        p.playSound(p.getLocation(), main.getConfigValues().getClearingSoundString(), main.getConfigValues().getClearingSoundVolume(), main.getConfigValues().getClearingSoundPitch());
                                    }
                                    main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                    if (main.getConfigValues().getNoFallMillis() > 0) {
                                        noFallDamage.put(p, System.currentTimeMillis() + main.getConfigValues().getNoFallMillis());
                                    }
                                }
                            } else if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(main.getConfigValues().getCancelName())) {
                                chunkBusterLocations.remove(p);
                                p.closeInventory();
                                if (main.getConfigValues().cancelSoundEnabled()) {
                                    p.playSound(p.getLocation(), main.getConfigValues().getCancelSoundString(), main.getConfigValues().getCancelSoundVolume(), main.getConfigValues().getCancelSoundPitch());
                                }
                                main.getUtils().sendMessage(p, ConfigValues.Message.GUI_CANCEL);
                            }
                        } else {
                            main.getUtils().sendMessage(p, ConfigValues.Message.CANNOT_PLACE);
                        }
                    } else {
                        main.getUtils().sendMessage(p, ConfigValues.Message.NOT_MINIMUM_ROLE);
                    }
                } else {
                    main.getUtils().sendMessage(p, ConfigValues.Message.NO_FACTION);
                }
            } else {
                p.sendMessage(Utils.color("&cError, please re-place your chunk buster."));
            }
        }
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().contains(main.getConfigValues().getGUITitle()) && e.getPlayer() instanceof Player) {
            chunkBusterLocations.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (main.getConfigValues().showUpdateMessage() && e.getPlayer().isOp()) {
            main.getUtils().checkUpdates(e.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (noFallDamage.containsKey(p)) {
                if (noFallDamage.get(p) >= System.currentTimeMillis()) {
                    e.setCancelled(true);
                } else {
                    noFallDamage.remove(p);
                }
            }
        }
    }
}
