package us.blockcade.core.commands.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.util.UUID;

public class VerifyCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("verify")) {

            // Checking for necessary rank
            if (s instanceof Player) {
                Player sender = (Player) s;
                if (!RanksHandler.hasPermissions(sender, Rank.ADMIN)) {
                    sender.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }
            }

            if (args.length == 2) {
                String name = args[0];
                String factor = args[1];

                UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

                if (BlockcadeDB.containsUserData(uuid)) {

                    if (factor.equalsIgnoreCase("donor") || factor.equalsIgnoreCase("donate")
                            || factor.equalsIgnoreCase("donater") || factor.equalsIgnoreCase("d")) {
                        //RanksHandler.verifiedDonor(uuid, true);
                        s.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName() + " is now a verified donor!");
                    } else if (factor.equalsIgnoreCase("tester") || factor.equalsIgnoreCase("beta")
                            || factor.equalsIgnoreCase("betatest") || factor.equalsIgnoreCase("b")) {
                        //RanksHandler.verifiedBeta(uuid, true);
                        s.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName() + " is now a verified beta tester!");
                    } else {
                        s.sendMessage(ChatColor.RED + "Unknown verification factor.");
                        return false;
                    }

                } else {
                    s.sendMessage(ChatColor.RED + "Task could not be completed. Unknown user.");
                    return false;
                }

            } else {
                // Incorrect number of arguments specified
                s.sendMessage(ChatColor.RED + "Proper usage: /" + label + " <Player> <Rank>");
                s.sendMessage(ChatColor.RED + "Key: <Required> [Optional]");
                return false;
            }

        }

        return false;
    }

}
