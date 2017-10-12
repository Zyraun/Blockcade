package us.blockcade.core.commands.command.moderation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class UuidCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("uuid")) {

            boolean permission = false;
            if (s instanceof Player) {
                Player player = (Player) s;
                permission = RanksHandler.hasPermissions(player, Rank.MOD);
            } else permission = true;

            if (args.length == 0) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatColor.RED + "Usage: /" + label + " <Player>");
                    return false;
                }
                Player player = (Player) s;
                player.sendMessage(ChatColor.AQUA + "Your UUID: " + ChatColor.YELLOW + player.getUniqueId());
            } else if (args.length == 1) {
                if (!permission) {
                    s.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }

                String name = Bukkit.getOfflinePlayer(args[0]).getName();
                s.sendMessage(ChatColor.AQUA + name + "'s UUID: " + ChatColor.YELLOW + Bukkit.getOfflinePlayer(name).getUniqueId());
            } else {
                s.sendMessage(ChatColor.RED + "Usage: /" + label + " [Player]");
                return false;
            }
        }
        return false;
    }

}
