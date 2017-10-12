package us.blockcade.core.modules.report;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import us.blockcade.core.utility.ItemStackBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportHandler implements Listener {

    private static Date now = new Date();
    private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static ItemStack getReportBook(Player player, String reported, String reason) {
        return new ItemStackBuilder(Material.BOOK_AND_QUILL)
                .withName(ChatColor.RED + "Reporting: " + reported)
                .withLore(new String[] {
                        ChatColor.AQUA + "Filed by " + player.getName(),
                        ChatColor.DARK_AQUA + format.format(now),
                        "", ChatColor.GRAY + "False reports are punishable by",
                        ChatColor.GRAY + "a permanent global ban.",
                        "", ChatColor.YELLOW + "mc.blockcade.us"
                }).build();
    }

    public static ItemStack writeBook(Player player, String reported, String reason) {
        ItemStack item = getReportBook(player, reported, reason);
        BookMeta meta = (BookMeta) item.getItemMeta();

        meta.setAuthor("Blockcade Inc.");

        List<String> pages = new ArrayList<>();
        pages.add(ChatColor.DARK_AQUA + "What is the reason that " + reported + " is being reported for?\n\n" + ChatColor.DARK_GRAY + reason);

        meta.setPages(pages);
        item.setItemMeta(meta);
        return item;
    }

}
