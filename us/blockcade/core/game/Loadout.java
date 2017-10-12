package us.blockcade.core.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class Loadout {

    private String loadoutName = "Default";

    private ItemStack helmet = new ItemStack(Material.AIR);
    private ItemStack chestplace = new ItemStack(Material.AIR);
    private ItemStack leggings = new ItemStack(Material.AIR);
    private ItemStack boots = new ItemStack(Material.AIR);

    private Map<Integer, ItemStack> slots = new HashMap<>();

    public Loadout(String name) {
        loadoutName = name;
    }

    public String getName() { return loadoutName; }

    public ItemStack getHelmet() { return helmet; }

    public ItemStack getChestplace() { return chestplace; }

    public ItemStack getLeggings() { return leggings; }

    public ItemStack getBoots() { return boots; }

    public Map<Integer, ItemStack> getItems() { return slots; }

    public ItemStack getItem(int slot) { return getItems().get(slot); }

    public boolean hasItem(int slot) { return getItems().containsKey(slot); }

    public void setName(String name) { loadoutName = name; }

    public void setHelmet(ItemStack item) { helmet = item; }

    public void setChestplace(ItemStack item) { chestplace = item; }

    public void setLeggings(ItemStack item) { leggings = item; }

    public void setBoots(ItemStack item) { boots = item; }

    public void clearSlot(int slot) { slots.remove(slot); }

    public void setItem(int slot, ItemStack item) {
        if (hasItem(slot)) clearSlot(slot);
        slots.put(slot, item);
    }

    public void give(Player player) {
        PlayerInventory p = player.getInventory();

        p.setHelmet(getHelmet());
        p.setChestplate(getChestplace());
        p.setLeggings(getLeggings());
        p.setBoots(getBoots());

        for (int i = 0; i < p.getSize(); i++) {
            if (hasItem(i)) p.setItem(i, getItem(i));
        }
    }

}
