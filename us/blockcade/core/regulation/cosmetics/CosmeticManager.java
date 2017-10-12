package us.blockcade.core.regulation.cosmetics;

import org.bukkit.entity.Player;
import us.blockcade.core.regulation.cosmetics.joineffect.JoinEffect;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class CosmeticManager {

    public static boolean containsUser(Player player) {
        return containsUser(player.getUniqueId());
    }

    public static boolean containsUser(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT * FROM `hub_cosmetics` WHERE uuid=?;");
            sql.setString(1,uuid.toString());
            ResultSet resultSet = sql.executeQuery();
            boolean containsData = resultSet.next();

            sql.close();
            resultSet.close();

            return containsData;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void generateNew(UUID uuid) {
        try {
            if (containsUser(uuid)) return;

            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement(
                    "INSERT INTO `hub_cosmetics` VALUES (?,?,?,?,0)");
            sql.setString(1, null);
            sql.setString(2, uuid.toString());
            sql.setString(3, "*0:1,1:0,2:0,3:0,4:0,5:0,6:0,7:0,8:0,9:0,10:0,11:0,12:0,13:0,14:0");
            sql.setString(4, JoinEffect.getDefaultDatabaseData());

            sql.execute();
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
