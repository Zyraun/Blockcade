/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison Hellum <addisonhellum@gmail.com>, September 2017
 */

package us.blockcade.core.commands.command.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.Blockcade;
import us.blockcade.core.game.events.GameLobbySetEvent;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

import java.util.ArrayList;
import java.util.List;

public class SetlobbyCmd implements CommandExecutor {

    /**
     * Set the lobby location locally to be handled by a callback event in an external game plugin
     * Calls the event GameLobbySetEvent with the location specified
     * To use, listen on game plugin for the event and manipulate the location data sent
     * Designed to be stored in a configuration file
     *
     * @param sender The sender of the command (player, console, command block, etc.)
     * @param cmd The command object itself, from SpigotAPI
     * @param label The name of the command itself
     * @param args Additional command parameters after the name
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setlobby")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This is a player-only command bro.");
                return false;
            }
            Player player = (Player) sender;

            if (!RanksHandler.hasPermissions(player, Rank.ADMIN)) {
                player.sendMessage(ChatColor.RED + "Command access denied.");
                return false;
            }

            final Location location = new Location(player.getWorld(),
                    player.getLocation().getBlockX() + 0.5,
                    player.getLocation().getBlockY() + 1,
                    player.getLocation().getBlockZ() + 0.5,
                    roundAngle(player.getLocation().getYaw()), 0);

            GameLobbySetEvent event = new GameLobbySetEvent(location);
            Blockcade.getInstance().getServer().getPluginManager().callEvent(event);

            player.sendMessage(ChatColor.AQUA + "Lobby SET command has been requested to be handled.\n" +
            ChatColor.DARK_AQUA + "This will only take effect if your current server has a game installed.");
        }

        return false;
    }

    /**
     * Rounds the player location's yaw property to a neater integer value.
     * Add more values to the roundAngles array to round to those as well
     *
     * @param angle Designed to round the player yaw to the nearest neat number
     * @return
     */
    private float roundAngle(float angle) {
        Float[] roundAngles = new Float[] { -360F -315F, -270F, -225F, -180F, -135F, -90F, -45F, 0F, 45F, 90F, 135F, 180F };
        List<Float> anglesList = new ArrayList<>();

        // add possible sorting (least to greatest)
        for (Float f : roundAngles) {
            anglesList.add(f);
        }

        for (int i = 0; i < anglesList.size(); i++) {
            if (i == 0) {
                // only compare to index + 1
                float midpoint = (anglesList.get(i) + anglesList.get(i + 1)) / 2;
                if (angle >= anglesList.get(i) && angle < midpoint) return anglesList.get(i);

            } else if (i == anglesList.size()) {
                // only compare to index - 1
                float midpoint = (anglesList.get(i) + anglesList.get(i - 1)) / 2;
                if (angle <= anglesList.get(i) && angle > midpoint) return anglesList.get(i);
            } else {
                // compare to both index +1 and -1
                float midpoint1 = (anglesList.get(i) + anglesList.get(i - 1)) / 2;
                float midpoint2 = (anglesList.get(i) + anglesList.get(i + 1)) / 2;
                if (angle >= midpoint1 && angle < midpoint2) return anglesList.get(i);

            }
        }
        return 90F;
    }

}
