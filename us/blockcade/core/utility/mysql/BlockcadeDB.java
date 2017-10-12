package us.blockcade.core.utility.mysql;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class BlockcadeDB {

    private static Connection connection;

    public static Connection getConnection() { return connection; }

    public static synchronized void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/blockcade",
                    "root", "HeuuRO7DHqulzai1");
            System.out.println("CONNECTION ESTABLISHED to BLOCKCADE DATABASE. LISTENING.");
        } catch (Exception e) {
            System.out.println("FAILED TO ESTABLISH CONNETION to BLOCKCADE DATABASE...");
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean containsUserData(Player player) {
        return containsUserData(player.getUniqueId());
    }

    public static boolean containsUserData(UUID uuid) {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM `user_data` WHERE uuid=?;");
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

}
