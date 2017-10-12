package us.blockcade.core.utility.gui;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIHandler implements Listener {

    private static Map<ItemStack, String> commandMap = new HashMap<ItemStack, String>();
    private static List<String> lockItems = new ArrayList<>();

    public static boolean runByConsole(ItemStack itemStack) {
        if (!commandMap.containsKey(itemStack)) return false;
        return commandMap.get(itemStack).startsWith("$");
    }

    public static String getCommand(ItemStack itemStack) {
        if (!commandMap.containsKey(itemStack)) return null;
        return commandMap.get(itemStack).replace("$", "");
    }

    public static void bindCommand(ItemStack itemStack, String command, boolean console) {
        if (getCommand(itemStack) != null) {
            commandMap.remove(itemStack);
        }
        if (console) command = "$" + command;

        commandMap.put(itemStack, command);
    }

    public static boolean areItemsLocked(Inventory inventory) {
        return lockItems.contains(inventory.getTitle());
    }

    public static void lockItems(Inventory inventory) {
        if (areItemsLocked(inventory)) lockItems.remove(inventory.getTitle());
        lockItems.add(inventory.getTitle());
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (areItemsLocked(event.getInventory())) {
            event.setCancelled(true);
        }

        ItemStack item = event.getCurrentItem();
        if (getCommand(item) == null) return;

        CommandSender sender;

        if (runByConsole(item)) sender = Bukkit.getConsoleSender();
        else sender = event.getWhoClicked();

        event.setCancelled(true);
        Bukkit.dispatchCommand(sender, getCommand(item));
        event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.NOTE_PIANO, 1F, 1F);
        event.getWhoClicked().closeInventory();
    }

}
