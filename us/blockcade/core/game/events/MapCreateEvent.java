package us.blockcade.core.game.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MapCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private String eventName;
    private Location eventLocation;
    private boolean cancelled = false;

    public MapCreateEvent(String name, Location location) {
        eventName = name;
        eventLocation = location;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getName() {
        return eventName;
    }

    public Location getLocation() { return eventLocation; }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
