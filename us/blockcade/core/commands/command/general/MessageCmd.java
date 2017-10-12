package us.blockcade.core.commands.command.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.ranks.RanksHandler;

import java.util.HashMap;
import java.util.Map;

public class MessageCmd implements CommandExecutor {

    public static Map<String, String> lastMessage = new HashMap<>();

    public static void setLastMessage(String player, String other) {
        if (lastMessage.containsKey(player)) lastMessage.remove(player);
        if (lastMessage.containsKey(other)) lastMessage.remove(other);
        lastMessage.put(player, other);
        lastMessage.put(other, player);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (args.length == 0) {
                s.sendMessage(ChatColor.RED + "Usage: /msg <Player> <Message>");
                return false;
            } else if (args.length == 1) {
                s.sendMessage(ChatColor.RED + "Usage: /msg <Player> <Message>");
                return false;
            } else {
                String to = args[0];
                if (!Bukkit.getOfflinePlayer(to).isOnline()) {
                    s.sendMessage(ChatColor.RED + "That player is not online.");
                    return false;
                }

                String pm = "";
                for (int i = 1; i < args.length; i++) {
                    pm = pm + args[i] + " ";
                }

                String from;
                if (!(s instanceof Player)) {
                    from = ChatColor.AQUA + "" + ChatColor.BOLD + "Console";
                } else {
                    Player p = (Player) s;
                    from = p.getName();
                }

                Player toPlayer = Bukkit.getPlayer(to);

                if (s instanceof Player) {
                    toPlayer.sendMessage(ChatColor.GRAY + "From: " + RanksHandler.getLocalRank(Bukkit.getPlayer(from)).getColor() + from + ChatColor.GRAY + " » " + ChatColor.LIGHT_PURPLE + pm);
                } else {
                    toPlayer.sendMessage(ChatColor.GRAY + "From: " + from + ChatColor.GRAY + " » " + ChatColor.LIGHT_PURPLE + pm);
                }

                s.sendMessage(ChatColor.GRAY + "To: " + RanksHandler.getLocalRank(toPlayer).getColor() + toPlayer.getName() + ChatColor.GRAY + " » " + ChatColor.LIGHT_PURPLE + pm);
                setLastMessage(from, toPlayer.getName());
            }
        }

        return false;
    }

}
