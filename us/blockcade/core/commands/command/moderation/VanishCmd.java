/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison Hellum <addisonhellum@gmail.com>, September 2017
 */

package us.blockcade.core.commands.command.moderation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.blockcade.core.Blockcade;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class VanishCmd implements CommandExecutor {

    /**
     * Hides vanished player from online players on the server
     * Adds invisibility effect to the vanished player
     * Adds vanished player to a list for later handling - ModerationHandler
     * Toggles on/off with usage.
     *
     * @param player Player to vanish from users
     */
    public void vanish(Player player) {
        if (Blockcade.getModerationHandler().isPlayerVanished(player)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(player);
            }
            player.setGameMode(GameMode.CREATIVE);
            Blockcade.getModerationHandler().removeVanishedPlayer(player);
            player.sendMessage(ChatColor.GRAY + "You are no longer vanished.");
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            // TODO: Check if not staff
            p.hidePlayer(player);
        }
        player.setGameMode(GameMode.SPECTATOR);
        Blockcade.getModerationHandler().addVanishedPlayer(player);
        player.sendMessage(ChatColor.GRAY + "You have been vanished.");
    }

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
        if (cmd.getName().equalsIgnoreCase("vanish")) {

            if ((s instanceof Player)) {
                Player player = (Player) s;
                if (!RanksHandler.hasPermissions(player, Rank.MOD)) {
                    player.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }
            }

            if (args.length == 0) {

                // Command sender is console/command block. Disallow self game-mode.
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatColor.RED + "This is a player-only command, silly.");
                    return false;
                }
                Player player = (Player)s;
                vanish(player);

            } else if (args.length == 1) {
                String arg = args[0];
                OfflinePlayer argPlayer = Bukkit.getOfflinePlayer(arg);

                // Check to see if argument given is an online player
                if (!argPlayer.isOnline()) {
                    s.sendMessage(ChatColor.RED + "That player is not currently online.");
                    return false;
                }

                // Updating target player's game-mode
                // Notifying players of game-mode updates
                Player targetPlayer = Bukkit.getPlayer(argPlayer.getUniqueId());
                vanish(targetPlayer);
                if (Blockcade.getModerationHandler().isPlayerVanished(targetPlayer)) {
                    s.sendMessage(ChatColor.GRAY + targetPlayer.getName() + " has vanished successfully.");
                } else {
                    s.sendMessage(ChatColor.GRAY + targetPlayer.getName() + " has been successfully unvanished.");
                }
            } else {
                // Incorrect number of arguments specified
                s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " [Player]");
                s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");
            }

        }

        return false;
    }

}
