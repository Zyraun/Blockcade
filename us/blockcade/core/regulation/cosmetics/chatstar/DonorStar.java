package us.blockcade.core.regulation.cosmetics.chatstar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.cosmetics.CosmeticManager;
import us.blockcade.core.utility.NametagUtil;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DonorStar {

    /**
     CHAT COLOR       NUMERIC ID      DEFAULT
     -------------------------------------------------------------------

     GRAY                 0          *(default)
     RED                  1
     DARK RED             2
     BLUE                 3
     LIGHT BLUE           4
     DARK BLUE            5
     PURPLE               6
     LIGHT PURPLE         7
     YELLOW               8
     GOLD                 9
     GREEN                10
     DARK GREEN           11
     CYAN                 12
     WHITE                13
     DARK GRAY            14
     */

    public enum StarColor {

        GRAY("Gray", ChatColor.GRAY, 0),
        RED("Red", ChatColor.RED, 1),
        DARK_RED("Dark Red", ChatColor.DARK_RED, 2),
        BLUE("Blue", ChatColor.BLUE, 3),
        AQUA("Light Blue", ChatColor.AQUA, 4),
        DARK_BLUE("Dark Blue", ChatColor.DARK_BLUE, 5),
        PURPLE("Purple", ChatColor.DARK_PURPLE, 6),
        LIGHT_PURPLE("Light Purple", ChatColor.LIGHT_PURPLE, 7),
        YELLOW("Yellow", ChatColor.YELLOW, 8),
        GOLD("Gold", ChatColor.GOLD, 9),
        GREEN("Green", ChatColor.GREEN, 10),
        DARK_GREEN("Dark Green", ChatColor.DARK_GREEN, 11),
        CYAN("Cyan", ChatColor.DARK_AQUA, 12),
        WHITE("White", ChatColor.WHITE, 13),
        DARK_GRAY("Dark Gray", ChatColor.DARK_GRAY, 14);

        private String name;
        private ChatColor color;
        private int id;

        private StarColor(String name, ChatColor color, int id) {
            this.name = name;
            this.color = color;
            this.id = id;
        }

        public String getName() { return name; }

        public ChatColor getColor() { return color; }

        public int getId() { return id; }

    }

    private static Map<UUID, String> localStarData = new HashMap<>();
    public static Map<UUID, String> getLocalStarData() { return localStarData; }

    private static StarColor[] starColors = new StarColor[] { StarColor.GRAY, StarColor.RED, StarColor.DARK_RED,
    StarColor.BLUE, StarColor.AQUA, StarColor.DARK_BLUE, StarColor.PURPLE, StarColor.LIGHT_PURPLE, StarColor.YELLOW,
    StarColor.GOLD, StarColor.GREEN, StarColor.DARK_GREEN, StarColor.CYAN, StarColor.WHITE, StarColor.DARK_GRAY};

    public static StarColor[] getStarColors() { return starColors; }

    public static String getName(int id) {
        for (StarColor c : getStarColors()) {
            if (c.getId() == id) return c.getName();
        } return "Black";
    }

    public static ChatColor getColor(int id) {
        for (StarColor c : getStarColors()) {
            if (c.getId() == id) return c.getColor();
        } return ChatColor.BLACK;
    }

    public static StarColor getStarColor(int id) {
        return getStarColors()[id];
    }

    public static int getId(ChatColor color) {
        for (StarColor c : getStarColors()) {
            if (c.getColor().equals(color)) return c.getId();
        } return -1;
    }

    public static String getStarData(UUID uuid) {
        if (!CosmeticManager.containsUser(uuid)) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                CosmeticManager.generateNew(uuid);
            }
            return "";
        }

        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT star_color FROM `hub_cosmetics` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            String data = result.getString("star_color");

            if (getLocalStarData().containsKey(uuid))
                localStarData.remove(uuid);

            localStarData.put(uuid, data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public static String getStarData(Player player) {
        return getStarData(player.getUniqueId());
    }

    public static boolean hasAccess(UUID uuid, int id) {
        if (!getLocalStarData().containsKey(uuid))
            localStarData.put(uuid, getStarData(uuid));

        String[] data = getLocalStarData().get(uuid).split(",");
        String entry = data[id];
        return entry.split(":", 1).equals(1);
    }

    public static boolean hasAccess(Player player, int id) {
        return hasAccess(player.getUniqueId(), id);
    }

    public static boolean hasAccess(UUID uuid, ChatColor color) {
        if (!getLocalStarData().containsKey(uuid))
            localStarData.put(uuid, getStarData(uuid));

        String[] data = getLocalStarData().get(uuid).split(",");
        String entry = data[getId(color)];
        return entry.split(":", 1).equals(1);
    }

    public static boolean hasAccess(Player player, ChatColor color) {
        return hasAccess(player.getUniqueId(), color);
    }

    public static boolean hasAccess(UUID uuid, StarColor star) {
        if (!getLocalStarData().containsKey(uuid))
            localStarData.put(uuid, getStarData(uuid));

        String[] data = getLocalStarData().get(uuid).split(",");
        String entry = data[star.getId()];
        return entry.split(":", 1).equals(1);
    }

    public static boolean hasAccess(Player player, StarColor star) {
        return hasAccess(player.getUniqueId(), star);
    }

    public static StarColor getActiveStar(UUID uuid) {
        if (!getLocalStarData().containsKey(uuid))
            localStarData.put(uuid, getStarData(uuid));

        String[] data = getLocalStarData().get(uuid).split(",");
        for (String d : data) {
            if (d.contains("*")) {
                int id = Integer.valueOf(d.split(":")[0].replace("*", ""));
                return getStarColor(id);
            }
        } return StarColor.GRAY;
    }

    public static StarColor getActiveStar(Player player) {
        return getActiveStar(player.getUniqueId());
    }

    public static void setAccess(UUID uuid, StarColor star, boolean value) {
        String finalData = "";
        String data = getStarData(uuid);
        String[] parse = data.split(",");
        for (int i = 0; i < parse.length; i++) {
            if (parse[i].equalsIgnoreCase(parse[star.getId()])) {
                int accessToken = 0;
                if (value == true) accessToken = 1;

                String prefix = parse[1].split(":")[0];
                String comma = ",";

                if (i == parse.length - 1) comma = "";
                finalData = finalData + prefix + ":" + accessToken + comma;
            } else {
                if (i != parse.length - 1) finalData = finalData + parse[i] + ",";
                else finalData = finalData + parse[i];
            }
        }

        if (data.equals(finalData)) return;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `hub_cosmetics` SET star_color=? WHERE uuid=?");
            sql.setString(1, finalData);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setActiveStar(UUID uuid, StarColor star) {
        String data = getStarData(uuid);
        data = data.replace("*", "");

        String finalData = "";

        String[] parse = data.split(",");
        for (int i = 0; i < parse.length; i++) {
            if (i == star.getId()) {
                String packet = parse[i];

                String comma = ",";
                if (i == parse.length - 1) comma = "";

                finalData = finalData + "*" + packet + comma;
            } else {
                if (i != parse.length - 1) finalData = finalData + parse[i] + ",";
                else finalData = finalData + parse[i];
            }
        }

        if (data.equals(finalData)) return;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `hub_cosmetics` SET star_color=? WHERE uuid=?");
            sql.setString(1, finalData);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
