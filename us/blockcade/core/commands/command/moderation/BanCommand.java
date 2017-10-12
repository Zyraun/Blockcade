package us.blockcade.core.commands.command.moderation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.bans.BanManager;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if ((s instanceof Player) && !RanksHandler.hasPermissions((Player)s, Rank.MOD)) {
                s.sendMessage(ChatColor.RED + "Command access denied.");
                return false;
            }

            if (args.length == 0) {
                // TODO: GUI of all players > Ban categories (preset times & reasons)

            } else if (args.length == 1) {
                // Ban a specific player forever - no given reason
                String name = args[0];

                OfflinePlayer offp = Bukkit.getOfflinePlayer(name);
                Rank rank = RanksHandler.getRank(offp.getUniqueId());
                BanManager.createBan(offp.getUniqueId());
                offp.setBanned(true);

                s.sendMessage(" ");
                s.sendMessage(ChatColor.GRAY + "The player: " + rank.getColor() + offp.getName() + ChatColor.GRAY + " has been banned.");
                s.sendMessage(ChatColor.GREEN + "This ban will take place once the user leaves the server.");
                s.sendMessage(" ");

            } else if (args.length > 1) {
                // Ban a specific player forever - reason defined

            }
        }

        return false;
    }

}
