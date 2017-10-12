package us.blockcade.core.commands.command.gamemode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) return false;
        Player player = (Player) s;

        // TODO: If player has rank ...

        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (args.length == 0) {

            }


        }

        return true;
    }

}
