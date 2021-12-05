/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison, September 2017
 */

package us.blockcade.core.commands.command.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class SpectatorCmd implements CommandExecutor {

    /**
     * Sets players game-mode to Spectator mode
     * Notifies player of game-mode update
     *
     * @param player the player to set to Spectator
     */
    public void giveGamemode(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ChatColor.GRAY + "Gamemode updated to " + player.getGameMode().name().toLowerCase() + " mode.");
    }

    /**
     * Set yourself or other players into Spectator mode
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
        if (cmd.getName().equalsIgnoreCase("spectator")) {

            // Checking for necessary rank
            if (s instanceof Player) {
                Player sender = (Player) s;
                if (!RanksHandler.hasPermissions(sender, Rank.MOD)) {
                    sender.sendMessage(ChatColor.RED + "Command access denied.");
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
                giveGamemode(player);

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
                giveGamemode(targetPlayer);
                s.sendMessage(ChatColor.GRAY + "Updated " + targetPlayer.getName() + " gamemode to creative mode.");
            } else {
                // Incorrect number of arguments specified
                s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " [Player]");
                s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");
            }
        }

        return false;
    }

}
