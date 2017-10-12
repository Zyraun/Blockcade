package us.blockcade.core.regulation.ranks;

import org.bukkit.ChatColor;

public enum Rank {

    DEFAULT(0, "", ChatColor.YELLOW, ChatColor.GRAY),
    STEEL(1, "Steel", ChatColor.WHITE, ChatColor.WHITE),
    GOLD(2, "Gold", ChatColor.GOLD, ChatColor.WHITE),
    GEM(3, "Gem", ChatColor.DARK_AQUA, ChatColor.WHITE),
    VIP(4, "VIP", ChatColor.DARK_PURPLE, ChatColor.WHITE),
    MOD(5, "Mod", ChatColor.DARK_GREEN, ChatColor.WHITE),
    SRMOD(6, "Sr.Mod", ChatColor.GREEN, ChatColor.WHITE),
    ADMIN(7, "Admin", ChatColor.RED, ChatColor.AQUA),
    OWNER(8, "Owner", ChatColor.DARK_RED, ChatColor.AQUA);

    public int rankId;
    public String rankName;
    public ChatColor rankColor;
    public ChatColor rankChatColor;

    private Rank(int id, String name, ChatColor color, ChatColor chatColor) {
        rankId = id;
        rankName = name;
        rankColor = color;
        rankChatColor = chatColor;
    }

    public int getId() { return rankId; }
    public String getName() { return rankName; }
    public ChatColor getColor() { return rankColor; }
    public ChatColor getChatColor() { return rankChatColor; }

}
