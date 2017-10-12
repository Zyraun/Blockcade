package us.blockcade.core.game.teams;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockcadeTeam {

    private String teamName;
    private ChatColor teamChatColor;
    private Color teamColor;

    private int teamScore = 0;

    private List<UUID> players = new ArrayList<>();

    public BlockcadeTeam(String name, ChatColor chatColor, Color color) {
        teamName = name;
        teamChatColor = chatColor;
        teamColor = color;
    }

    public String getName() { return teamName; }

    public ChatColor getChatColor() { return teamChatColor; }

    public Color getColor() { return teamColor; }

    public int getScore() { return teamScore; }

    public List<UUID> getPlayers() { return players; }

    public void setName(String name) { teamName = name; }

    public void setChatColor(ChatColor color) {
        teamChatColor = color;
    }

    public void setColor(Color color) {
        teamColor = color;
    }

    public void setScore(int score) { teamScore = score; }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean hasPlayer(UUID uuid) {
        return players.contains(uuid);
    }

    public void addPlayer(Player player) {
        if (hasPlayer(player)) return;
        players.add(player.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        if (hasPlayer(uuid)) return;
        players.add(uuid);
    }

    public void removePlayer(Player player) {
        if (!hasPlayer(player)) return;
        players.remove(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        if (!hasPlayer(uuid)) return;
        players.remove(uuid);
    }

}
