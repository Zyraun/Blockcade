/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison Hellum <addisonhellum@gmail.com>, September 2017
 */

package us.blockcade.core.commands.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reload")) {

            if (s instanceof Player) {
                Player player = (Player) s;

                if (!RanksHandler.hasPermissions(player, Rank.ADMIN)) {
                    player.sendMessage(ChatColor.RED + "Command access denied. Administrators only.");
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bukkit:reload");
                player.sendMessage(ChatColor.AQUA + "[Blockcade] The server has been reloaded successfully.");

            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bukkit:reload");
            }

        }

        return false;
    }

}
