package us.blockcade.core.regulation.rewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class RewardManager {

    public static int getCurrency(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT currency FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            return result.getInt("currency");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }

    public static void setCurrency(UUID uuid, int amount) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `user_data` SET currency=? WHERE uuid=?");
            sql.setInt(1, amount);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyCurrency(UUID uuid, int amount) {
        setCurrency(uuid, getCurrency(uuid) + amount);
    }

    public static void rewardCurrency(Player player, int amount) {
        modifyCurrency(player.getUniqueId(), amount);
        player.sendMessage(ChatColor.GOLD + "You have collected " + ChatColor.YELLOW + amount + ChatColor.GOLD + " coins!");
    }

    public static void spendCurrency(Player player, int amount) {
        if (!hasCurrency(player.getUniqueId(), amount)) {
            player.sendMessage(ChatColor.RED + "You do not have enough to complete the transaction.");
        }
        modifyCurrency(player.getUniqueId(), -amount);
        player.sendMessage(ChatColor.GOLD + "You spent " + ChatColor.YELLOW + amount + ChatColor.GOLD + " coins!");
    }

    public static boolean hasCurrency(UUID uuid, int amount) {
        return getCurrency(uuid) >= amount;
    }

    public static int getExperience(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT xp FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            return result.getInt("xp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }

    public static void setExperience(UUID uuid, int amount) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `user_data` SET xp=? WHERE uuid=?");
            sql.setInt(1, amount);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyExperience(UUID uuid, int amount) {
        setExperience(uuid, getExperience(uuid) + amount);
    }

    public static void rewardExperience(Player player, int amount) {
        modifyExperience(player.getUniqueId(), amount);
        player.sendMessage(ChatColor.DARK_GREEN + "You have gained " + ChatColor.GREEN + amount + ChatColor.DARK_GREEN + " XP!");
    }

    public static boolean hasExperience(UUID uuid, int amount) {
        return getExperience(uuid) >= amount;
    }

}
