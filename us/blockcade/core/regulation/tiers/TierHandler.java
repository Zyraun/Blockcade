package us.blockcade.core.regulation.tiers;

import org.bukkit.event.Listener;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class TierHandler implements Listener {

    public static Tier[] tierList = new Tier[] { Tier.SILVER3, Tier.SILVER2, Tier.SILVER1, Tier.GOLD3, Tier.GOLD2,
    Tier.GOLD1, Tier.DIAMOND3, Tier.DIAMOND2, Tier.DIAMOND1, Tier.CRYSTAL3, Tier.CRYSTAL2, Tier.CRYSTAL1, Tier.MASTER};

    public static int getElo(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT elo FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            return result.getInt("elo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }

    public static Tier getTierFromElo(int elo) {
        for (Tier t : tierList) {
            if (t.getMinimum() <= elo && t.getMaximum() >= elo) {
                return t;
            }
        }
        return Tier.SILVER3;
    }

    public static Tier getTier(UUID uuid) {
        return getTierFromElo(getElo(uuid));
    }

    public static boolean hasElo(UUID uuid, int elo) {
        return getElo(uuid) >= elo;
    }

    public static void setElo(UUID uuid, int elo) {
        if (elo < 500) elo = 500;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `user_data` SET elo=? WHERE uuid=?");
            sql.setInt(1, elo);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyElo(UUID uuid, int elo) {
        int currentElo = getElo(uuid);
        if ((currentElo + elo) < 500) setElo(uuid, 500);
        setElo(uuid, currentElo + elo);
    }

}
