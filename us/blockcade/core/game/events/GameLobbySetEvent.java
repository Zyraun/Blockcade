package us.blockcade.core.game.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameLobbySetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Location eventLocation;
    private boolean cancelled = false;

    public GameLobbySetEvent(Location location) {
        eventLocation = location;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Location getLocation() {
        return eventLocation;   
    }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}