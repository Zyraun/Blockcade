package us.blockcade.core.regulation.cosmetics;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.commands.command.moderation.SkullCommand;
import us.blockcade.core.utility.ItemStackBuilder;
import us.blockcade.core.utility.gui.GUInventory;

public class CosmeticMenu implements Listener {

    public static void display(Player player) {
        GUInventory gui = new GUInventory(5, "Cosmetics");

        ItemStack color = new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName(" ").withData(2);
        Integer[] colorSlots = new Integer[] { 1, 2, 8, 9, 10, 18, 28, 36, 37, 38, 44, 45 };

        for (int i : colorSlots) {
            gui.setItem(i, color);
        }

        gui.setItem(2, 5, new ItemStack(Material.TNT));

        gui.setItem(3, 3, new ItemStackBuilder(Material.NETHER_STAR).withName(ChatColor.GREEN + "Chat Star Color").build());
        gui.setItem(3, 7, new ItemStackBuilder(Material.BLAZE_POWDER).withName(ChatColor.GREEN + "Trail").build());
        gui.setItem(4, 5, new ItemStackBuilder(Material.LEATHER_CHESTPLATE).withName(ChatColor.GREEN + "Costume").build());
        gui.setItem(4, 3, new ItemStackBuilder(Material.FIREWORK).withName(ChatColor.GREEN + "Join Animation").build());
        gui.setItem(4, 7, new ItemStackBuilder(Material.EGG).withName(ChatColor.GREEN + "Pet").build());
        gui.setItem(3, 5, new ItemStackBuilder(Material.GOLD_HELMET).withName(ChatColor.GREEN + "Hat").build());


        gui.fill(gui.getEmptyItem());
        gui.lockItems();

        gui.display(player);
    }

}
