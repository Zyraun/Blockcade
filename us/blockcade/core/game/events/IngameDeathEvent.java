package us.blockcade.core.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IngameDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player eventKiller;
    private Player eventSlain;
    private boolean cancelled = false;

    public IngameDeathEvent(Player killer, Player slain) {
        eventKiller = killer;
        eventSlain = slain;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getKiller() {
        return eventKiller;
    }

    public Player getSlain() { return eventSlain; }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
