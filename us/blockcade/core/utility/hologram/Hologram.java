package us.blockcade.core.utility.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import us.blockcade.core.Blockcade;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private String[] hologramLines;
    private Location hologramLocation;
    private boolean showing;

    private List<ArmorStand> stands = new ArrayList<>();

    /**
     * The best distance between hologram lines
     */
    private double distance = 0.27;

    /**
     * Create a Hologram object with the lines specified
     * Created at the main world spawn location by default
     *
     * @param lines The separated lines of the Hologram
     */
    public Hologram(String... lines) {
        hologramLines = lines;
        hologramLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        showing = false;
    }

    /**
     * Show one specific line of a Hologram object at
     * a specified location
     *
     * @param line The line of the Hologram to show
     * @param location The location at which to show the line
     */
    public void showLine(String line, Location location) {
        hologramLocation = location;
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        //stand.setInvulnerable(true);
        //stand.setCollidable(false);
        stand.setRemoveWhenFarAway(false);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName(line);

        if (!stands.contains(stand))
            stands.add(stand);
    }

    /**
     * Show the full Hologram with all the lines at a
     * specified location
     *
     * @param location The location at which to display the full Hologram
     */
    public void show(Location location) {
        // Start from bottom line and move up
        // each hologram moves up by (distance)
        List<String> lines = new ArrayList<>();
        for (String s : getLines())
            lines.add(s);

        int itterations = 0;
        for (int lineCount = lines.size() - 1; lineCount >= 0; lineCount--) {
            showLine(lines.get(lineCount), new Location(
                    location.getWorld(),
                    location.getX(),
                    location.getY() + (itterations * distance),
                    location.getZ()));
            itterations++;
        }
    }

    /**
     * Shows the full Hologram at a specified location only
     * for a duration is a certain number of ticks, then removes
     *
     * @param location The location at which to display the Hologram
     * @param ticks The duration in ticks to display for (seconds = 20 * ticks)
     */
    public void show(Location location, long ticks) {
        show(location);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Blockcade.getInstance(), new Runnable() {

            @Override
            public void run() {
                destroy();
            }

        }, ticks);
    }

    /**
     * Removes the Hologram, can still be displayed later
     */
    public void destroy() {
        for (ArmorStand as : getArmorStands())
            as.remove();
    }

    /**
     * @return The array of lines in the Hologram
     */
    public String[] getLines() { return hologramLines; }

    /**
     * @return The current location of the Hologram
     */
    public Location getLocation() { return hologramLocation; }

    /**
     * @return The armor stands used for spawning the Hologram
     */
    public List<ArmorStand> getArmorStands() { return stands; }

}