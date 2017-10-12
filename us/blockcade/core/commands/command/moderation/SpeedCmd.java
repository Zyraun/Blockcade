package us.blockcade.core.commands.command.moderation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class SpeedCmd implements CommandExecutor {

    @EventHandler
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speed")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatColor.RED + "This is a muggle-only command.");
            }
            Player p = (Player) s;

            if (!RanksHandler.hasPermissions(p, Rank.MOD)) {
                p.sendMessage(ChatColor.RED + "Command access denied.");
                return false;
            }

            if (args.length == 0) {
                float speed;

                if (p.isFlying()) speed = p.getFlySpeed() * 5;
                else speed = p.getWalkSpeed() * 5;

                p.sendMessage(ChatColor.GRAY + "Your current speed is: " + ChatColor.GREEN + speed);
            } else if (args.length == 1) {
                String speedArg = args[0];
                float speed;

                if (speedArg.equalsIgnoreCase("reset") || speedArg.equalsIgnoreCase("r")) {
                    if (p.isFlying()) p.setFlySpeed(0.1F);
                    else p.setWalkSpeed(0.2F);

                    p.sendMessage(ChatColor.GRAY + "Your speed has been " + ChatColor.GREEN + "reset" + ChatColor.GRAY + ".");
                    return false;
                }

                try {
                    speed = Float.valueOf(speedArg);
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid number.");
                    p.sendMessage(ChatColor.RED + "Usage /" + label + " [speed]");
                    return false;
                }

                try {
                    if (p.isFlying()) p.setFlySpeed(speed / 5);
                    else p.setWalkSpeed(speed / 5);
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Number is too high! Use 5 or less.");
                    return false;
                }

                p.sendMessage(ChatColor.GRAY + "Set speed to " + ChatColor.GREEN + speed + ChatColor.GRAY + " successfully.");
            } else {
                p.sendMessage(ChatColor.RED + "Usage /" + label + " [speed]");
            }
        }

        return false;
    }

}
