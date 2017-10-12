package us.blockcade.core.game.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.blockcade.core.game.BlockcadeGame;

public class GameEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private BlockcadeGame eventGame;
    private boolean cancelled = false;

    public GameEndEvent(BlockcadeGame game) {
        eventGame = game;
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

    public boolean isCancelled() { return cancelled; }

    public void setCancelled(boolean value) { cancelled = true; }

}