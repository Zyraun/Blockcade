package us.blockcade.core.regulation.chat;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.blockcade.core.regulation.ranks.Rank;
import us.blockcade.core.regulation.ranks.RanksHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatHandler implements Listener {

    private static Map<String, String> lastMessages = new HashMap<>();

    @EventHandler
    public void onDoubleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (RanksHandler.hasPermissions(player, Rank.VIP)) return;

        if (!lastMessages.containsKey(player.getName())) {
            lastMessages.put(player.getName(), message);
            return;
        }
        String lastMessage = lastMessages.get(player.getName());

        if (lastMessage.equalsIgnoreCase(message)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot send the same message twice!");
            return;
        }

        lastMessages.remove(player.getName());
        lastMessages.put(player.getName(), message);
    }

    // TODO: Very very very basic word detection, make fancy later!!
    private static String[] blacklistedWords = new String[] {
            "fuck", "shit", "bitch", "cunt", "nigger", "dick", "faggot", "bastard", "fucker", "ass",
            "asshole", "motherfucker", "cuck", "fucking", "shitting"
    };

    public static boolean isWordBlacklisted(String word) {
        for (String s : blacklistedWords)
            if (s.equalsIgnoreCase(word)) return true;
        return false;
    }

    public static boolean containsBlacklistedWord(String text) {
        for (String s : text.split(" ")) {
            if (isWordBlacklisted(s)) return true;
        } return false;
    }

    public static String replaceBlacklistedWords(String text) {
        if (!containsBlacklistedWord(text)) return text;
        String finalText = "";
        for (String s : text.split(" ")) {
            if (isWordBlacklisted(s)) {
                finalText = finalText +  StringUtils.repeat("*", s.length()) + " ";
            } else finalText = finalText + s + " ";
        } return finalText;
    }

    @EventHandler
    public void onBlacklistChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!containsBlacklistedWord(message)) return;

        event.setCancelled(true);
        player.chat(replaceBlacklistedWords(message));

        if (RanksHandler.hasPermissions(player, Rank.MOD)) return;

        player.sendMessage(" ");
        player.sendMessage(ChatColor.RED + "Let's keep the chat appropriate.");
        player.sendMessage(ChatColor.RED + "Please refrain from using strong/blacklisted language.");
        player.sendMessage(" ");
    }

}
