package us.blockcade.core.commands.command.moderation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.utility.ItemStackBuilder;
import us.blockcade.core.utility.gui.GUInventory;
import us.blockcade.core.utility.mysql.BlockcadeDB;

public class SkullCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("skull")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatColor.RED + "This command is only for mere mortals.");
                return false;
            }
            Player p = (Player) s;

            if (!RanksHandler.hasPermissions(p, Rank.MOD)) {
                p.sendMessage(ChatColor.RED + "Command access denied.");
                return false;
            }

            if (args.length == 0) {
                p.getInventory().addItem(getSkull(p.getName()));
                p.sendMessage(ChatColor.GRAY + "Added " + ChatColor.GREEN + "your own" + ChatColor.GRAY + " skull to your inventory.");

            } else if (args.length == 1) {
                String name = args[0];
                OfflinePlayer offp = Bukkit.getOfflinePlayer(name);
                ChatColor color = ChatColor.YELLOW;

                if (BlockcadeDB.containsUserData(offp.getUniqueId())) {
                    if (offp.isOnline())
                        color = RanksHandler.getLocalRank(Bukkit.getPlayer(name)).getColor();
                    else color = RanksHandler.getRank(offp.getUniqueId()).getColor();
                }

                ItemStack skull = new ItemStackBuilder(Material.SKULL_ITEM)
                        .withName(color + offp.getName() + ChatColor.GRAY + "'s Head").withData(3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();

                meta.setOwner(name);
                skull.setItemMeta(meta);

                p.getInventory().addItem(skull);
                p.sendMessage(ChatColor.GRAY + "Added " + color + offp.getName() + ChatColor.GRAY + "'s head to your inventory.");
            } else if (args.length == 2) {
                String name = args[0];
                String player = args[1];

                if (!Bukkit.getOfflinePlayer(player).isOnline()) {
                    p.sendMessage(ChatColor.RED + "That player is not online.");
                    return false;
                }
                Player target = Bukkit.getPlayer(player);

                target.getInventory().addItem(getSkull(name));

                p.sendMessage(ChatColor.GRAY + "Gave skull to " + ChatColor.GREEN + target.getName() + ChatColor.GRAY + ".");
                target.sendMessage(ChatColor.GRAY + "You received a player skull!");
            } else {
                s.sendMessage(ChatColor.RED + "Usage: /" + label + " <Name> [Player]");
            }
        }

        return false;
    }

    public static ItemStack getSkull(String name) {
        OfflinePlayer offp = Bukkit.getOfflinePlayer(name);
        ChatColor color = ChatColor.YELLOW;

        if (BlockcadeDB.containsUserData(offp.getUniqueId())) {
            if (offp.isOnline())
                color = RanksHandler.getLocalRank(Bukkit.getPlayer(name)).getColor();
            else color = RanksHandler.getRank(offp.getUniqueId()).getColor();
        }

        ItemStack skull = new ItemStackBuilder(Material.SKULL_ITEM)
                .withName(color + offp.getName() + ChatColor.GRAY + "'s Head").withData(3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(name);
        skull.setItemMeta(meta);

        return skull;
    }

}
