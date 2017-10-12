package us.blockcade.core.commands.command.moderation;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.ItemStackBuilder;
import us.blockcade.core.utility.gui.GUInventory;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleport")) {
            if (s instanceof Player) {
                Player p = (Player) s;
                if (!RanksHandler.hasPermissions(p, Rank.MOD))
                    p.sendMessage(ChatColor.RED + "Command access denied.");
            }

            if (args.length == 0) {
                // No arguments given, open GUI
                if (s instanceof Player) {
                    Player player = (Player) s;
                    s.sendMessage(ChatColor.RED + "Usage: /" + label + " <Player> [Player]");
                }

            } else if (args.length == 1) {
                // One argument, teleport sender to a player
                // Sender must be a player to teleport
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatColor.RED + "Sorry, this is a muggle-only command.");
                }
                Player player = (Player) s;

                String name = args[0];
                if (!Bukkit.getOfflinePlayer(name).isOnline()) {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                }
                Player tp = Bukkit.getPlayer(name);

                teleport(player, tp);

            } else if (args.length == 2) {
                // Two arguments given, teleport first player to second player
                String target = args[0];
                String dest = args[1];

                if (Bukkit.getOfflinePlayer(target).isOnline() && Bukkit.getOfflinePlayer(dest).isOnline()) {
                    Player p = Bukkit.getPlayer(target);
                    Player p2 = Bukkit.getPlayer(dest);

                    teleport(p, p2);
                } else {
                    s.sendMessage(ChatColor.RED + "One of the players is not online.");
                }

            } else if (args.length == 3) {
                // Three arguments given, teleport player to coordinates
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatColor.RED + "Sorry, this is a muggle-only command.");
                }
                Player player = (Player) s;

                String xText = args[0];
                String yText = args[1];
                String zText = args[2];

                if (representsInteger(xText) && representsInteger(yText) && representsInteger(zText)) {
                    player.teleport(new Location(player.getWorld(), Integer.valueOf(xText), Integer.valueOf(yText), Integer.valueOf(zText)));
                    player.sendMessage(ChatColor.GRAY + "Warped to " + ChatColor.GREEN + xText + ", " + yText + ", " + zText + " successfully.");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                } else {
                    player.sendMessage(ChatColor.RED + "Please enter valid coordinates to warp to.");
                }
            } else {
                // Invalid command, send usage
                s.sendMessage(ChatColor.RED + "Usage: /" + label + " <Player> [Player]");
            }
        }

        return false;
    }

    public boolean representsInteger(String value) {
        try {
            int num = Integer.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void teleport(Player p, Player p2) {
        p.teleport(p2);
        p.sendMessage(ChatColor.GRAY + "Warped to " + RanksHandler.getLocalRank(p2).getColor() + p2.getName() + ChatColor.GRAY + " successfully.");
        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);

        if (RanksHandler.hasPermissions(p2, Rank.MOD))
            p2.sendMessage(RanksHandler.getLocalRank(p).getColor() + p.getName() + ChatColor.GRAY + " warped to you.");
    }

    public void sendGUI(Player player) {
        GUInventory gui = new GUInventory(6, "Teleport to a Player");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getName().equalsIgnoreCase(player.getName())) {
                ItemStack item = new ItemStackBuilder(Material.SKULL_ITEM).withName(
                        RanksHandler.getLocalRank(p).getColor() + p.getName())
                        .withData(3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();

                meta.setOwner(p.getName());

                item.setItemMeta(meta);
                gui.addItem(item);
                gui.bindCommand(item, "tp " + player.getName(), false);
            }
        }
        gui.lockItems();
        gui.display(player);
    }

}
