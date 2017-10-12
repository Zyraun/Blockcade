package us.blockcade.core.commands.command.donor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.game.GameHandler;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class FlyCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {

            // Checking for necessary rank
            if (s instanceof Player) {
                Player sender = (Player) s;
                if (!RanksHandler.hasPermissions(sender, Rank.STEEL)) {
                    // TODO: Change access denied to shop link for donor
                    sender.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }

                if (GameHandler.started) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be used during a game.");
                    return false;
                }

                if (!sender.getAllowFlight()) {
                    sender.setAllowFlight(true);
                    sender.setFlying(true);
                    sender.teleport(sender.getLocation().add(0, 0.25, 0));
                    sender.sendMessage(ChatColor.GRAY + "Whoosh! You are now flying.");
                } else {
                    sender.setFlying(false);
                    sender.setAllowFlight(false);
                    sender.sendMessage(ChatColor.GRAY + "Great flight. You have now landed.");
                }

            } else {
                s.sendMessage(ChatColor.RED + "No! Bad!");
                return false;
            }

        }

        return false;
    }

}
