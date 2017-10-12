package us.blockcade.core.commands.command.general;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.rewards.RewardManager;

public class WalletCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("wallet")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatColor.RED + "You're console, you're infinitely rich.");
                return false;
            }
            Player p = (Player) s;

            p.sendMessage(ChatColor.GRAY + "Balance: " + ChatColor.GOLD + RewardManager.getCurrency(p.getUniqueId()));
        }

        return false;
    }

}
