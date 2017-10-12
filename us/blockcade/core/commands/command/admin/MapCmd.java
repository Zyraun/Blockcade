package us.blockcade.core.commands.command.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.Blockcade;
import us.blockcade.core.game.events.MapAddSpawnEvent;
import us.blockcade.core.game.events.MapCreateEvent;
import us.blockcade.core.game.events.MapDeleteEvent;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

import java.util.ArrayList;
import java.util.List;

public class MapCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("map")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatColor.RED + "This is a muggle-only command.");
                return false;
            }
            Player player = (Player) s;

            if (!RanksHandler.hasPermissions(player, Rank.ADMIN)) {
                player.sendMessage(ChatColor.RED + "You must be an Administrator or higher to do this.");
            }

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + " <create/remove/MAP_NAME> [addspawn]");
            } else if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + " <create/remove/MAP_NAME> [addspawn]");
            } else if (args.length == 2) {
                String arg = args[0];
                String exe = args[1];

                if (arg.equalsIgnoreCase("create")) {
                    final Location location = new Location(player.getWorld(),
                            player.getLocation().getBlockX() + 0.5,
                            player.getLocation().getBlockY() + 1,
                            player.getLocation().getBlockZ() + 0.5,
                            roundAngle(player.getLocation().getYaw()), 0);

                    MapCreateEvent event = new MapCreateEvent(exe, location);
                    Blockcade.getInstance().getServer().getPluginManager().callEvent(event);
                    player.sendMessage(ChatColor.DARK_AQUA + "Your map request has been sent. [C]");
                } else if (arg.equalsIgnoreCase("remove")) {
                    MapDeleteEvent event = new MapDeleteEvent(exe);
                    Blockcade.getInstance().getServer().getPluginManager().callEvent(event);
                    player.sendMessage(ChatColor.DARK_AQUA + "Your map request has been sent. [R]");
                } else {
                    if (exe.equalsIgnoreCase("addspawn")) {
                        final Location location = new Location(player.getWorld(),
                                player.getLocation().getBlockX() + 0.5,
                                player.getLocation().getBlockY() + 1,
                                player.getLocation().getBlockZ() + 0.5,
                                roundAngle(player.getLocation().getYaw()), 0);

                        MapAddSpawnEvent event = new MapAddSpawnEvent(arg, location);
                        Blockcade.getInstance().getServer().getPluginManager().callEvent(event);
                        player.sendMessage(ChatColor.DARK_AQUA + "Your map request has been sent. [A]");
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /" + label + " <create/remove/MAP_NAME> [addspawn]");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + " <create/remove/MAP_NAME> [addspawn]");
            }
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
