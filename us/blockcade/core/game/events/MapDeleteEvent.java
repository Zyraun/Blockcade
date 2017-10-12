package us.blockcade.core.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MapDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private String eventName;
    private boolean cancelled = false;

    public MapDeleteEvent(String name) {
        eventName = name;
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

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
