package us.blockcade.core.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.ItemStackBuilder;
import us.blockcade.core.utility.ReflectionUtil;
import us.blockcade.core.utility.gui.GUInventory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class GameSpectator implements Listener {

    private static List<UUID> spectators = new ArrayList<>();

    public static List<UUID> getSpectators() { return spectators; }

    public static boolean isSpectating(Player player) {
        return getSpectators().contains(player.getUniqueId());
    }

    public static boolean isSpectating(UUID uuid) {
        return getSpectators().contains(uuid);
    }

    public static void registerSpectator(Player player) {
        if (isSpectating(player)) return;
        spectators.add(player.getUniqueId());
    }

    public static void registerSpectator(UUID uuid) {
        if (isSpectating(uuid)) return;
        spectators.add(uuid);
    }

    public static void unregisterSpectator(Player player) {
        if (!isSpectating(player)) return;
        spectators.remove(player.getUniqueId());
    }

    public static void unregisterSpectator(UUID uuid) {
        if (!isSpectating(uuid)) return;
        spectators.remove(uuid);
    }

    public static void makeSpectator(Player player) {
        if (isSpectating(player)) return;
        registerSpectator(player);

        for (Player p : Bukkit.getOnlinePlayers())
            p.hidePlayer(player);

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        ItemStack teleport = new ItemStackBuilder(Material.EYE_OF_ENDER)
                .withName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Teleport to a Player")
                .withLore(new String[] { ChatColor.AQUA + "Right Click: " + ChatColor.GRAY + "Open a GUI of",
                ChatColor.GRAY + "players to teleport to.", "", ChatColor.AQUA + "Left Click: " + ChatColor.GRAY + "Teleport to a",
                ChatColor.GRAY + "random player."}).build();

        ItemStack fps = new ItemStackBuilder(Material.BLAZE_ROD)
                .withName(ChatColor.YELLOW + "" + ChatColor.BOLD + "First Person View")
                .withLore(new String[] { ChatColor.AQUA + "Right Click: " + ChatColor.GRAY + "Open a GUI of",
                ChatColor.GRAY + "available players to view.", "", ChatColor.AQUA + "Left Click: " + ChatColor.GRAY + "Punch a player",
                ChatColor.GRAY + "using this stick to see their view."}).build();

        ItemStack settings = new ItemStackBuilder(Material.REDSTONE_COMPARATOR)
                .withName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Settings")
                .withLore(new String[] { ChatColor.AQUA + "Click: " + ChatColor.GRAY + "Open a GUI to modify",
                ChatColor.GRAY + "your global spectator settings.", "", ChatColor.RED + "In development." }).build();

        ItemStack hub = new ItemStackBuilder(Material.BARRIER)
                .withName(ChatColor.RED + "" + ChatColor.BOLD + "Leave Lobby")
                .withLore(new String[] { ChatColor.AQUA + "Click: " + ChatColor.GRAY + "Quit your game and",
                ChatColor.GRAY + "return to the hub."}).build();

        ItemStack play = new ItemStackBuilder(Material.PAPER)
                .withName(ChatColor.AQUA + "" + ChatColor.BOLD + "Play Again")
                .withLore(new String[] { ChatColor.AQUA + "Click: " + ChatColor.GRAY + "Join another game of",
                ChatColor.GRAY + "your same current game-type.", "", ChatColor.RED + "In development."}).build();

        player.getInventory().setItem(0, play);
        player.getInventory().setItem(3, fps);
        player.getInventory().setItem(4, teleport);
        player.getInventory().setItem(5, settings);
        player.getInventory().setItem(8, hub);
    }

    public static void releaseSpectator(Player player) {
        if (!isSpectating(player)) return;
        unregisterSpectator(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setAllowFlight(false);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    /**
     * Sets a player as a spectator of an entity
     *
     * Note: This method utilizes reflection to get a Players NMS handle
     * to put them into spectate mode of an entity. The method may break
     * when a major server release comes out due to the fact that
     * certain method names inside NMS may change.
     *
     *
     * @param player The player who will be spectating
     * @param target The target that will be spectated by player
     */
    public static void makePlayerSpectateEntity(Player player, Entity target) {
        player.setGameMode(GameMode.SPECTATOR);
        try {
            Method playerHandle = ReflectionUtil.getMethod(player.getClass(), "getHandle");
            Object nmsPlayer = playerHandle.invoke(player);

            Method targetHandle = ReflectionUtil.getMethod(target.getClass(), "getHandle");
            Object nmsTargetEntity = targetHandle.invoke(target);

            Method setEntityAsPassengerMethod = ReflectionUtil.getMethod(nmsPlayer.getClass(), "e", ReflectionUtil.getNMSClass("Entity"));
            setEntityAsPassengerMethod.invoke(nmsPlayer, nmsTargetEntity);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to make player spectate entity", e);
        }
    }

    @EventHandler
    public void onSpecInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (!isSpectating(player)) return;
        event.setCancelled(true);

        if (item == null) return;

        if (item.getType().equals(Material.EYE_OF_ENDER) &&
                item.getItemMeta().getDisplayName().contains("Teleport to a Player")) {
            // Clicked the teleportation item
            if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                GUInventory gui = new GUInventory(4,"Spectator Menu - Teleport");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    ItemStack head = new ItemStackBuilder(Material.SKULL_ITEM)
                            .withName(RanksHandler.getLocalRank(p).getColor() + p.getName()).withData(3);
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(p.getName());

                    head.setItemMeta(meta);

                    gui.addItem(head);
                }
                gui.lockItems();
                player.openInventory(gui.getInventory());

            } else if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                Random random = new Random();
                List<UUID> players = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != player && !isSpectating(p))
                        players.add(p.getUniqueId());
                }

                if (players.size() == 0) {
                    player.sendMessage(ChatColor.GRAY + "There are no players to spectate.");
                    return;
                }
                Player p = Bukkit.getPlayer(players.get(random.nextInt(players.size())));
                player.teleport(p);
                player.sendMessage(ChatColor.GRAY + "You are now spectating " + RanksHandler.getLocalRank(p).getColor() + p.getName());

            }
        }
        if (item.getType().equals(Material.BLAZE_ROD) &&
                item.getItemMeta().getDisplayName().contains("First Person View")) {
            if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                GUInventory gui = new GUInventory(4,"Spectator Menu - First Person");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    ItemStack head = new ItemStackBuilder(Material.SKULL_ITEM)
                            .withName(RanksHandler.getLocalRank(p).getColor() + p.getName()).withData(3);
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(p.getName());

                    head.setItemMeta(meta);

                    gui.addItem(head);
                }
                gui.lockItems();
                player.openInventory(gui.getInventory());

            } else if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }

        }
        if (item.getType().equals(Material.BARRIER) &&
                item.getItemMeta().getDisplayName().contains("Leave Lobby")) {
            player.sendMessage(ChatColor.GREEN + "Transporting you to server hub01");
            player.performCommand("server hub01");

        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        if (!isSpectating(player)) return;

        if (inv.getTitle().contains("Spectator Menu - Teleport")) {
            if (!item.getType().equals(Material.SKULL_ITEM)) return;
            //Bukkit.getOfflinePlayer(item.getItemMeta().getDisplayName().trim());
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName().trim());
            if (Bukkit.getOfflinePlayer(name).isOnline()) {
                Player p = Bukkit.getPlayer(name);
                player.teleport(p);
                player.sendMessage(ChatColor.GRAY + "You are now spectating " + RanksHandler.getLocalRank(p).getColor() + p.getName());
            } else {
                player.sendMessage(ChatColor.GRAY + "That player has since left.");
            }
        } else if (inv.getTitle().contains("Spectator Menu - First Person")) {
            if (!item.getType().equals(Material.SKULL_ITEM)) return;
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName().trim());
            if (Bukkit.getOfflinePlayer(name).isOnline()) {
                Player p = Bukkit.getPlayer(name);
                makePlayerSpectateEntity(player, p);
                player.sendMessage(ChatColor.GRAY + "You are viewing as " + RanksHandler.getLocalRank(p).getColor() + p.getName());
            } else {
                player.sendMessage(ChatColor.GRAY + "That player has since left.");
            }
        }
    }

    @EventHandler
    public void onFirstPerson(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        if (!isSpectating(player)) return;
        ItemStack item = player.getItemInHand();

        if (item.getType().equals(Material.BLAZE_ROD) &&
                item.getItemMeta().getDisplayName().contains("First Person View")) {
            makePlayerSpectateEntity(player, event.getEntity());

            if (event.getEntity() instanceof Player) {
                Player p = (Player) event.getEntity();
                player.sendMessage(ChatColor.GRAY + "You are viewing as " + RanksHandler.getLocalRank(p).getColor() + p.getName());
            } else {
                player.sendMessage(ChatColor.GRAY + "You are viewing as " + ChatColor.AQUA + event.getEntity().getType());
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isSpectating(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (isSpectating(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (isSpectating(player))
            event.setCancelled(true);
    }

}
