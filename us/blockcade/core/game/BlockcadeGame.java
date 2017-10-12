package us.blockcade.core.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BlockcadeGame {

    private String gameName = "Prototype";
    private String[] gameDescription = new String[] { "An original game by Blockcade Inc.", "Available on mc.blockcade.us" };
    private int gameMaxPlayers = 8;
    private int gameMinPlayers = 2;

    private String winner;
    private String mvp;

    public BlockcadeGame(String name, String[] description, int maxPlayers, int minPlayers) {
        gameName = name;
        gameDescription = description;
        gameMaxPlayers = maxPlayers;
        gameMinPlayers = minPlayers;
    }

    public String getName() {
        return gameName;
    }

    public String[] getDescription() {
        return gameDescription;
    }

    public int getMaxPlayers() {
        return gameMaxPlayers;
    }

    public int getMinPlayers() {
        return gameMinPlayers;
    }

    public void setName(String name) {
        gameName = name;
    }

    public void setDescription(String[] description) {
        gameDescription = description;
    }

    public void setMaxPlayers(int value) {
        gameMaxPlayers = value;
    }

    public void setMinPlayers(int value) {
        gameMinPlayers = value;
    }

    public void displayInfo(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "            " + getName());
        player.sendMessage("");
        for (String s : getDescription())
            player.sendMessage(ChatColor.YELLOW + " " + s);
        player.sendMessage("");
    }

    public String getWinner() { return winner; }

    public String getMVP() { return mvp; }

    public void setWinner(String value) { winner = value; }

    public void setMVP(String value) { mvp = value; }

}
