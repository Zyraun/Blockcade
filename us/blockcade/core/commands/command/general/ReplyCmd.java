package us.blockcade.core.commands.command.general;

import com.avaje.ebeaninternal.server.core.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class ReplyCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reply")) {
            String from;
            if (!(s instanceof Player)) {
                from = ChatColor.AQUA + "" + ChatColor.BOLD + "Console";
            } else {
                Player p = (Player) s;
                from = p.getName();
            }

            if (!MessageCmd.lastMessage.containsKey(from)) {
                s.sendMessage(ChatColor.RED + "There is nobody whom you can reply to. [#]");
                return false;
            }

            if (!Bukkit.getOfflinePlayer(MessageCmd.lastMessage.get(from)).isOnline()) {
                s.sendMessage(ChatColor.RED + "There is nobody whom you can reply to. [@]");
                return false;
            }

            if (args.length == 0) {
                s.sendMessage(ChatColor.RED + "Usage: /" + label + " <Message>");
                return false;
            } else {
                String pm = "";
                for (int i = 0; i < args.length; i++) {
                    pm = pm + args[i] + " ";
                }

                Player reply = Bukkit.getPlayer(MessageCmd.lastMessage.get(from));
                s.sendMessage(ChatColor.GRAY + "To: " + RanksHandler.getLocalRank(reply).getColor() + reply.getName() + ChatColor.GRAY + " » " + ChatColor.LIGHT_PURPLE + pm);
                reply.sendMessage(ChatColor.GRAY + "From: " + RanksHandler.getLocalRank(Bukkit.getPlayer(from)).getColor() + from + ChatColor.GRAY + " » " + ChatColor.LIGHT_PURPLE + pm);
            }
        }

        return false;
    }

}
