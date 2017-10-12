package us.blockcade.core.regulation.bans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.rewards.RewardManager;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class BanManager {

    public static boolean isBanned(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT `id` FROM `ban_data` WHERE `uuid`=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            int data = result.getInt("id");

            sql.close();
            return data != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBanned(Player player) {
        return isBanned(player.getUniqueId());
    }

    public static boolean isBanned(String name) {
        return isBanned(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    public static String getBanReason(UUID uuid) {
        if (!isBanned(uuid)) return null;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT `reason` FROM `ban_data` WHERE `uuid`=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            String reason = result.getString("reason");

            sql.close();
            return reason;
        } catch (Exception e) {
            e.printStackTrace();
            return "Banned by staff member.";
        }
    }

    public static String getBanReason(Player player) {
        return getBanReason(player.getUniqueId());
    }

    public static int getBanDuration(UUID uuid) {
        if (!isBanned(uuid)) return -1;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT `duration` FROM `ban_data` WHERE `uuid`=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            int reason = result.getInt("duration");

            sql.close();
            return reason;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getBanDuration(Player player) {
        return getBanDuration(player.getUniqueId());
    }

    public static void createBan(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        String reason = "You are banned from this server!";
        int duration = 0;

        if (isBanned(uuid)) {
            // TODO; update ban info - perm
        }

        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement(
                    "INSERT INTO `ban_data` VALUES (?,?,?,?,?)");
            sql.setString(1, null);
            sql.setString(2, name);
            sql.setString(3, uuid.toString());
            sql.setString(4, reason);
            sql.setInt(5, duration);

            sql.execute();
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
