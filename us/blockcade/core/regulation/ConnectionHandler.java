package us.blockcade.core.regulation;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import us.blockcade.core.Blockcade;
import us.blockcade.core.regulation.bans.BanManager;
import us.blockcade.core.regulation.cosmetics.joineffect.JoinEffect;

public class ConnectionHandler implements Listener {

    @EventHandler
    public void onDisabled(PlayerLoginEvent event) {
        if (Blockcade.enableNetwork) return;

        event.disallow(Result.KICK_OTHER,
                ChatColor.AQUA + "" + ChatColor.BOLD + "\n\nBlockcade Network Inc.\n" +
                        ChatColor.RED + "This server has been manually disabled by a developer.\n\n" +
                        ChatColor.GRAY + "Check for updates on Twitter " + ChatColor.YELLOW + "@BlockcadeNetwork\n" +
                        ChatColor.GRAY + "or contact us at support@blockcade.us");
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Result result = event.getResult();
        Player player = event.getPlayer();
        if (result.equals(Result.KICK_WHITELIST)) {
            event.disallow(Result.KICK_OTHER,
                    ChatColor.AQUA + "" + ChatColor.BOLD + "\n\nBlockcade Network Inc.\n" +
                            ChatColor.RED + "This server is currently down for maintenance.\n\n" +
                            ChatColor.GRAY + "Check for updates on Twitter " + ChatColor.YELLOW + "@Blockcade\n" +
                            ChatColor.GRAY + "or contact us at support@blockcade.us");
        }
        if (result.equals(Result.KICK_BANNED)) {
            event.disallow(Result.KICK_OTHER,
                    ChatColor.AQUA + "" + ChatColor.BOLD + "\n\nBlockcade Network Inc.\n\n" +
                            ChatColor.GRAY + "Ban expires in: " + ChatColor.AQUA + BanManager.getBanDuration(player) + " seconds\n\n" +
                            ChatColor.DARK_RED + "You are banned from this server!\n" +
                            ChatColor.GRAY + BanManager.getBanReason(player) + "\n\n" +
                            ChatColor.RED + "If you feel this was a mistake, please submit\n" +
                            ChatColor.RED + "an appeal to " + ChatColor.GOLD + "www.blockcade.us/appeal");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

}
