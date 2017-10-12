package us.blockcade.core.utility.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockcade.core.utility.ItemStackBuilder;

import java.util.ArrayList;
import java.util.List;

public class GUInventory {

    public static GUInventory createGUI(Integer inventoryLines, String inventoryTitle){
        GUInventory inv = new GUInventory(inventoryLines, inventoryTitle);
        return inv;
    }

    public static GUInventory createGUI(Integer inventoryLines, String inventoryTitle, ArrayList<ItemStack> inventoryItems){
        GUInventory inv = new GUInventory(inventoryLines, inventoryTitle, inventoryItems);
        return inv;
    }

    // Make an SInventory with a clone of another Inventory
    public GUInventory(Inventory inventory){
        this.inventoryLines = (inventory.getSize()+1)/9;
        this.inventoryTitle = inventory.getTitle();
        ArrayList<ItemStack> inventoryItems = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++){
            if (inventory.getItem(i) == null){
                inventoryItems.add(null);
            } else {
                inventoryItems.add(inventory.getItem(i));
            }
        }
        this.inventoryItems = inventoryItems;
    }

    // Make an SInventory with # of Lines, and Title.
    public GUInventory(Integer inventoryLines, String inventoryTitle){
        this.inventoryLines = inventoryLines;
        if (this.inventoryLines > 6){
            this.inventoryLines = 6;
        } else if (this.inventoryLines < 1){
            this.inventoryLines = 1;
        }
        this.inventoryTitle = inventoryTitle;
        if (this.inventoryTitle.length() > 32){
            System.out.println(ChatColor.RED + "Error, SInventory Inventory Name is too long for Inventory " + inventoryTitle);
        }
        ArrayList<ItemStack> inventoryItems = new ArrayList<>();
        for (int i = 0; i < (inventoryLines * 9)+1; i++){
            inventoryItems.add(null);
        }
        this.inventoryItems = inventoryItems;
    }

    // Make an SInventory with # of Lines, Title, and List of Items.
    public GUInventory(Integer inventoryLines, String inventoryTitle, ArrayList<ItemStack> inventoryItems){
        this.inventoryLines = inventoryLines;
        if (this.inventoryLines > 6){
            this.inventoryLines = 6;
        } else if (this.inventoryLines < 1){
            this.inventoryLines = 1;
        }
        this.inventoryTitle = inventoryTitle;
        if (this.inventoryTitle.length() > 32){
            System.out.println(ChatColor.RED + "Error, SInventory Inventory Name is too long for Inventory " + inventoryTitle);
        }
        this.inventoryItems = inventoryItems;
        setItems(inventoryItems);
        if (this.inventoryItems.size() > inventoryLines * 9){
            System.out.println(ChatColor.RED + "Error, SInventory has too many items for Inventory " + inventoryTitle);
        }
    }

    // Information About the Inventory
    private Integer inventoryLines = 1;
    private String inventoryTitle = "";
    private ArrayList<ItemStack> inventoryItems = new ArrayList<>();

    public int getFilledSlots() {
        List<ItemStack> contents = new ArrayList<>();
        for (ItemStack i : this.inventoryItems) {
            if (i != null) {
                if (!i.getType().equals(Material.AIR)) {
                    contents.add(i);
                }
            }
        } return contents.size();
    }

    // Sets the Item specified to said Item.
    // Offset of +1
    public void setItem(Integer itemNumber, ItemStack Item){
        this.inventoryItems.set(itemNumber - 1, Item);
    }

    // Sets the Item specified to said Item, relative to the lineNumber.
    // itemNumber needs to be 1-9;
    // Offset of +1
    public void setItem(Integer lineNumber, Integer itemNumber, ItemStack Item){
        this.inventoryItems.set(((lineNumber * 9)-10) + itemNumber, Item);
    }

    // Sets the Items of the Inventory to the list given.
    // Null = Empty Spot/Air
    public void setItems(ArrayList<ItemStack> inventoryItems){
        for (int i = 0; i < (inventoryLines * 9)-1; i++){
            if (inventoryItems.size() > i){
                if (inventoryItems.get(i) != null){
                    this.inventoryItems.set(i, inventoryItems.get(i));
                } else {
                    this.inventoryItems.set(i, null);
                }
            } else {
                this.inventoryItems.set(i, null);
            }
        }
    }

    // Sets the Items of the Inventory to the list given, starting at Specified Line.
    // Null = Empty Spot/Air
    public void setItemsAt(Integer lineNumber, ArrayList<ItemStack> inventoryItems){
        int i = 0;
        for (ItemStack item : inventoryItems){
            this.inventoryItems.set((lineNumber * 9)+i, item);
            i++;
        }
    }

    // Sets the Items of the Inventory to the list given, starting at Specified Line.
    // Null = Empty Spot/Air
    public void setItemsAt(Integer lineNumber, Integer itemNumber, ArrayList<ItemStack> inventoryItems){
        int i = 0;
        for (ItemStack item : inventoryItems){
            this.inventoryItems.set((lineNumber * 9)+itemNumber+i, item);
            i++;
        }
    }

    public void setLine(int lineNumber, ItemStack itemStack) {
        ArrayList<ItemStack> fillers = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            fillers.add(itemStack);
        setItemsAt(lineNumber, fillers);
    }

    // Adds an Item to the next open spot in the Inventory
    // Returns false if no open spots.
    public boolean addItem(ItemStack itemStack){
        if (!this.inventoryItems.contains(null)){
            return false;
        }

        int i = 0;
        for (ItemStack item : this.inventoryItems){
            if (item == null){
                this.inventoryItems.set(i, itemStack);
                return true;
            }
            i++;
        }
        return false;
    }

    // Adds an Item to the next open spot in the Inventory Line
    // Returns false if no open spots in the line specified
    public boolean addItem(Integer lineNumber, ItemStack itemStack){
        if (!this.inventoryItems.contains(null)){
            return false;
        }

        for (int i = (lineNumber-1)*9; i < (lineNumber-1)*9+1; i++){
            if (this.inventoryItems.get(i) == null){
                this.inventoryItems.set(i, itemStack);
                return true;
            }
            i++;
        }
        return false;
    }

    public String getCommand(ItemStack itemStack) {
        return GUIHandler.getCommand(itemStack);
    }

    public void bindCommand(ItemStack itemStack, String command, boolean console) {
        GUIHandler.bindCommand(itemStack, command, console);
    }

    public void lockItems() {
        GUIHandler.lockItems(getInventory());
    }

    public void display(Player player) {
        player.openInventory(getInventory());
    }

    public void fill(ItemStack item) {
        int repeat = (this.inventoryLines * 9) - getFilledSlots();
        for (int i = 0; i < repeat; i++) {
            addItem(item);
        }
    }

    public ItemStack getEmptyItem() {
        return new ItemStackBuilder(Material.STAINED_GLASS_PANE)
                .withName(" ").withData(15);
    }

    // Converts the Lines, Title, and Items into an Inventory to return.
    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(null, (inventoryLines * 9), inventoryTitle);
        int i = 0;
        for(ItemStack item : inventoryItems){
            if (item != null){
                inv.setItem(i, item);
            }
            i++;
        }
        return inv;
    }

}