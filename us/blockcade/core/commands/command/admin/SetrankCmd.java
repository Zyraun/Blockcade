/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison, September 2017
 */

package us.blockcade.core.commands.command.admin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.blockcade.core.Blockcade;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.ItemStackBuilder;
import us.blockcade.core.utility.Testing;
import us.blockcade.core.utility.Title;
import us.blockcade.core.utility.gui.GUInventory;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetrankCmd implements CommandExecutor {

    /**
     * Vanish yourself from players to more effectively moderate the server
     * Staff will still be able to view your status
     * No arguments: applies to self
     * Single argument: applies to others
     * More arguments: incorrect usage
     *
     * @param s The sender of the command (player, console, command block, etc.)
     * @param cmd The command object itself, from SpigotAPI
     * @param label The name of the command itself
     * @param args Additional command parameters after the name
     * @return
     */
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setrank")) {

            if ((s instanceof Player)) {
                Player player = (Player) s;
                if (!RanksHandler.hasPermissions(player, Rank.ADMIN)) {
                    player.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }
            }

            if (args.length == 0) {

                // Incorrect number of arguments specified
                s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " <Player> <Rank>");
                s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");

            } else if (args.length == 1) {
                String arg = args[0];
                if (!BlockcadeDB.containsUserData(Bukkit.getOfflinePlayer(arg).getUniqueId())) {
                    s.sendMessage(ChatColor.RED + "That player has not been registered.");
                }

                if (s instanceof Player) {
                    Player player = (Player) s;
                    player.openInventory(getGUI(Bukkit.getOfflinePlayer(arg).getUniqueId()));
                } else {
                    // Incorrect number of arguments specified
                    s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " <Player> <Rank>");
                    s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");
                }

            } else if (args.length == 2) {
                String name = args[0];
                String rank = args[1];

                if (getRankFromString(rank) == null) {
                    s.sendMessage(ChatColor.RED + "Invalid rank argument. Try numeric ID's (0-8).");
                }

                Rank newrank = getRankFromString(rank);

                if (s instanceof Player) {
                    Player sender = (Player) s;
                    if (RanksHandler.getLocalRank(sender).getId() < newrank.getId()) {
                        sender.sendMessage(ChatColor.RED + "You cannot set players to that rank, it is above your own.");
                        return false;
                    }
                    if (sender.getName().equalsIgnoreCase(name)) {
                        sender.sendMessage(ChatColor.RED + "You are not permitted to set your own rank.");
                        return false;
                    }
                }

                UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

                RanksHandler.setRank(uuid, newrank);
                s.sendMessage(ChatColor.GOLD + "Updated " + newrank.getColor() + "" + ChatColor.BOLD
                        + newrank.getName() + " " + ChatColor.RESET + newrank.getColor() + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GOLD + " rank successfully.");

                if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    Player player = Bukkit.getPlayer(uuid);

                    RanksHandler.updateLocalRank(player);

                    Title title = new Title(newrank.getColor() + newrank.getName(), ChatColor.YELLOW + "Your rank has been updated.", 5, 40, 5);
                    title.send(player);

                    newrank = RanksHandler.getLocalRank(player);
                    if (newrank.getId() == Rank.STEEL.getId() || newrank.getId() == Rank.GOLD.getId()
                            || newrank.getId() == Rank.GEM.getId() || newrank.getId() == Rank.VIP.getId()) {
                        Material material = Material.EMERALD;
                        if (newrank.getId() == Rank.STEEL.getId()) material = Material.IRON_INGOT;
                        if (newrank.getId() == Rank.GOLD.getId()) material = Material.GOLD_INGOT;
                        if (newrank.getId() == Rank.GEM.getId()) material = Material.DIAMOND;
                        if (newrank.getId() == Rank.VIP.getId()) material = Material.NETHER_STAR;
                        Testing.fancyDrop(player, material, 30);
                    }
                }
            } else {
                // Incorrect number of arguments specified
                s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " <Player> <Rank>");
                s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");
            }

        }

        return false;
    }

    /**
     * Converts a string to the corresponding Rank object
     * i.e Rank.ADMIN or Rank.VIP
     *
     * @param rank String name of the desired rank
     * @return
     */
    private Rank getRankFromString(String rank) {
        if (rank == "0" || rank.equalsIgnoreCase("default")) return Rank.DEFAULT;
        if (rank == "1" || rank.equalsIgnoreCase("steel")) return Rank.STEEL;
        if (rank == "2" || rank.equalsIgnoreCase("gold")) return Rank.GOLD;
        if (rank == "3" || rank.equalsIgnoreCase("gem")) return Rank.GEM;
        if (rank == "4" || rank.equalsIgnoreCase("vip")) return Rank.VIP;
        if (rank == "5" || rank.equalsIgnoreCase("mod")) return Rank.MOD;
        if (rank == "6" || rank.equalsIgnoreCase("srmod")) return Rank.SRMOD;
        if (rank == "7" || rank.equalsIgnoreCase("admin")) return Rank.ADMIN;
        if (rank == "8" || rank.equalsIgnoreCase("owner")) return Rank.OWNER;
        return null;
    }

    public Inventory getGUI(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        Rank rank = RanksHandler.getRank(uuid);

        GUInventory gui = new GUInventory(1, "Set " + name + "'s Rank");

        ItemStack empty = new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName(" ").withData(15);

        Integer[] woolData = new Integer[] { 4, 0, 1, 9, 10, 13, 5, 14 };

        ItemStack def = new ItemStackBuilder(Material.WOOL).withName(Rank.DEFAULT.getColor() + name).withData(4);
        gui.addItem(def);
        gui.bindCommand(def, "setrank " + name + " default", true);

        ItemStack head = new ItemStackBuilder(Material.SKULL_ITEM).withName(rank.getColor() + "" + ChatColor.BOLD + rank.getName() + " " + ChatColor.RESET + rank.getColor() + name).withData(3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta(meta);

        for (int i = 1; i < woolData.length; i++) {
            Rank r = RanksHandler.ranksList[i];
            ItemStack item = new ItemStackBuilder(Material.WOOL).withName(r.getColor() + "" + ChatColor.BOLD + "" + r.getName() + " " + ChatColor.RESET + r.getColor() + name).withData(woolData[i]);
            gui.addItem(item);
            gui.bindCommand(item, "setrank " + name + " " + r.getName(), true);
        }

        gui.addItem(head);

        gui.lockItems();

        return gui.getInventory();
    }

}
