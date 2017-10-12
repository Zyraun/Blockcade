package us.blockcade.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.blockcade.core.Blockcade;
import us.blockcade.core.regulation.bans.BanManager;
import us.blockcade.core.regulation.cosmetics.CosmeticMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Testing implements Listener {

    public static void fancyDrop(Player player, Material material, int seconds) {
        int repeat = Bukkit.getScheduler().scheduleSyncRepeatingTask(Blockcade.getInstance(), new Runnable() {

            @Override
            public void run() {
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("!NOPICKUP");

                List<String> lore = new ArrayList<>();
                lore.add(new Random().nextInt(1000000) + "");
                meta.setLore(lore);

                item.setItemMeta(meta);
                player.getLocation().getWorld().dropItem(player.getLocation().add(0, 1.5, 0), item);
            }

        }, 10, 3);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Blockcade.getInstance(), new Runnable() {

            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(repeat);
            }

        }, 20 * seconds);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (!e.getItem().getItemStack().hasItemMeta()) return;
        if (e.getItem().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("!NOPICKUP")) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        Player p = event.getPlayer();
        p.sendMessage("Banned for: " + BanManager.getBanReason(p.getUniqueId()));
    }

}
