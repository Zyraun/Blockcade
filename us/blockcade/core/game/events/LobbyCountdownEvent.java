package us.blockcade.core.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.blockcade.core.game.BlockcadeGame;

public class LobbyCountdownEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private BlockcadeGame eventGame;
    private int eventDuration;
    private boolean cancelled = false;

    public LobbyCountdownEvent(BlockcadeGame game, int duration) {
        eventGame = game;
        eventDuration = duration;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BlockcadeGame getGame() {
        return eventGame;
    }

    public int getDuration() { return eventDuration; }

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}
