package us.blockcade.core.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerQuitGameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player eventPlayer;
    private boolean cancelled = false;

    public PlayerQuitGameEvent(Player player) {
        eventPlayer = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return eventPlayer;
    }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
