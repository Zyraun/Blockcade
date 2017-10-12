package us.blockcade.core.regulation.tiers;

import org.bukkit.ChatColor;

public enum Tier {

    SILVER3(ChatColor.GRAY, "▎▎▎", 500, 799),
    SILVER2(ChatColor.GRAY, "▎▎", 800, 999),
    SILVER1(ChatColor.GRAY, "▎", 1000, 1199),
    GOLD3(ChatColor.GOLD, "▎▎▎", 1200, 1399),
    GOLD2(ChatColor.GOLD, "▎▎", 1400, 1599),
    GOLD1(ChatColor.GOLD, "▎", 1600, 1799),
    DIAMOND3(ChatColor.AQUA, "▎▎▎", 1800, 1999),
    DIAMOND2(ChatColor.AQUA, "▎▎", 2000, 2199),
    DIAMOND1(ChatColor.AQUA, "▎", 2200, 2399),
    CRYSTAL3(ChatColor.DARK_PURPLE, "▎▎▎", 2400, 2599),
    CRYSTAL2(ChatColor.DARK_PURPLE, "▎▎", 2600, 2799),
    CRYSTAL1(ChatColor.DARK_PURPLE, "▎", 2800, 2999),
    MASTER(ChatColor.RED, "▎*▎", 3000, 100000);

    private ChatColor tierColor;
    private String tierDisplay;
    private int tierMinimum;
    private int tierMaximum;

    private Tier(ChatColor color, String display, int minimum, int maximum) {
        tierColor = color;
        tierDisplay = display;
        tierMinimum = minimum;
        tierMaximum = maximum;
    }

    public ChatColor getColor() { return tierColor; }
    public String getDisplay() { return tierDisplay; }
    public int getMinimum() { return tierMinimum; }
    public int getMaximum() { return tierMaximum; }

}
