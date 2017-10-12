package us.blockcade.core.commands.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.modules.report.ReportHandler;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.effect.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AoCmd implements CommandExecutor {

    private static List<UUID> usingOverride = new ArrayList<>();

    public static boolean hasOverride(Player player) {
        return usingOverride.contains(player.getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ao")) {

            // Checking for necessary rank
            if (s instanceof Player) {
                Player player = (Player) s;
                if (!RanksHandler.hasPermissions(player, Rank.ADMIN) && !player.getName().equalsIgnoreCase("litdoggo")) {
                    player.sendMessage(ChatColor.RED + "Command access denied.");
                    return false;
                }

                String border = ChatColor.GOLD + "-----------------------------------------------------";
                if (hasOverride(player)) {
                    usingOverride.remove(player.getUniqueId());

                    player.sendMessage(border);
                    player.sendMessage(ChatColor.AQUA + "Admin override mode has been disabled.");
                    player.sendMessage(border);
                } else {
                    usingOverride.add(player.getUniqueId());

                    player.sendMessage(border);
                    player.sendMessage(ChatColor.GREEN + "Admin override mode has been enabled.");
                    player.sendMessage(border);

                }
            }
        }

        return false;
    }

}
