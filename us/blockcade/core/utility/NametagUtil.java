package us.blockcade.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

public class NametagUtil {

    private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public static Scoreboard getScoreboard() { return scoreboard; }

    public static void registerNameTag(Player player) {
        // TODO: Include clans name suffix [NAME]
        Rank rank = RanksHandler.getRank(player.getUniqueId());
        Team team = scoreboard.registerNewTeam(player.getName());
        team.setPrefix(rank.getColor() + "");

        scoreboard.getTeam(player.getName()).addPlayer(player);
    }

    public static void unregisterNameTag(Player player) {
        if (scoreboard.getTeam(player.getName()) != null) {
            scoreboard.getTeam(player.getName()).unregister();
        }
    }

    public static void updateNameTag(Player player) {
        if (scoreboard.getTeam(player.getName()) == null) {
            registerNameTag(player);
        }
        unregisterNameTag(player);
        registerNameTag(player);
    }

}