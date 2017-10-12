package us.blockcade.core.regulation.cosmetics.joineffect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.blockcade.core.regulation.cosmetics.CosmeticManager;
import us.blockcade.core.regulation.cosmetics.chatstar.DonorStar;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinEffect {

    public enum JoinEffectType {

        DEFAULT(0, "None", ""),
        MAGIC(1, "Magic!", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:1,Trail:0,Colors:[10551551],FadeColors:[16711927]},{Type:0,Flicker:0,Trail:0,Colors:[16777215],FadeColors:[9437439]}]}}}}"),
        LOVE_FIRST_SIGHT(2, "Love at First Sight", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:1,Trail:0,Colors:[16405845],FadeColors:[14039112,16777215]},{Type:4,Flicker:1,Trail:0,Colors:[16749823],FadeColors:[14966015,16777215]}]}}}}"),
        TWINKLE_TWINKLE(3, "Twinkle-Twinkle Little Star", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[10066329,15268909],FadeColors:[16777215]}]}}}}"),
        SOLID_GREEN(4, "Solid Green", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[65314],FadeColors:[31240]}]}}}}"),
        CONFETTI(5, "Confetti", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:10,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[16725558,65322,2188543],FadeColors:[16777215,16777215,16777215]},{Type:4,Flicker:0,Trail:0,Colors:[10310399,16772608,15734783],FadeColors:[16777215,16777215,16777215]}]}}}}"),
        CONFETTI_2(6, "Confetti 2.0", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[16718105,3473172,1186559],FadeColors:[16777215]},{Type:4,Flicker:0,Trail:0,Colors:[13554718,16717761,9383679],FadeColors:[16777215]},{Type:4,Flicker:0,Trail:0,Colors:[16736746,98309,2981867],FadeColors:[16777215]},{Type:4,Flicker:0,Trail:0,Colors:[16727357,5252234,15925058],FadeColors:[16777215]}]}}}}"),
        USA(7, "USA! USA! USA!", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:2,Flicker:1,Trail:1,Colors:[16715021,16777215,3356007],FadeColors:[16777215]}]}}}}"),
        RED_WHITE_BOOM(8, "Red, White, and Boom!", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[16777215,16777215],FadeColors:[16777215]},{Type:0,Flicker:0,Trail:0,Colors:[13893632],FadeColors:[13701913]},{Type:1,Flicker:0,Trail:0,Colors:[1187251],FadeColors:[1316095]}]}}}}"),
        DRAGON_TEARS(9, "Dragon Tears", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:1,Trail:0,Colors:[5057023,5647359],FadeColors:[10066329]},{Type:0,Flicker:1,Trail:0,Colors:[3167175,2770352],FadeColors:[10724259]}]}}}}"),
        GOLDEN_SHOWERS(10, "Golden Showers", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:3,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:0,Trail:0,Colors:[16770829,16770831,16773156],FadeColors:[16755740]},{Type:4,Flicker:1,Trail:0,Colors:[16757012,16762900,16765240],FadeColors:[0]}]}}}}"),
        ACID_RAIN(11, "Acid Rain", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:2,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:1,Trail:0,Colors:[1048351,2675746,3276562],FadeColors:[5373765,3724313,6029122]},{Type:4,Flicker:1,Trail:0,Colors:[10092382,2424596,8060789],FadeColors:[6094711,8847117,4587382]}]}}}}"),
        JAMES_BOND(12, "James Bond", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:1,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:0,Trail:0,Colors:[2500134,5197647,1313547],FadeColors:[3026478]},{Type:0,Flicker:0,Trail:0,Colors:[3552822,5855577,3355443],FadeColors:[3158064]},{Type:2,Flicker:0,Trail:0,Colors:[13385021],FadeColors:[13714243]}]}}}}"),
        DOUBLE_O(13, "Double-O Seven", "summon FireworksRocketEntity {X} {Y} {Z} {LifeTime:1,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:0,Flicker:0,Trail:0,Colors:[2500134,5197647,1313547],FadeColors:[3026478]},{Type:0,Flicker:0,Trail:0,Colors:[3552822,5855577,3355443],FadeColors:[3158064]},{Type:2,Flicker:0,Trail:0,Colors:[13385021],FadeColors:[13714243]}]}}}}");

        public int id;
        public String name;
        public String command;

        private JoinEffectType(int id, String name, String command) {
            this.id = id;
            this.name = name;
            this.command = command;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCommand() {
            return command;
        }

        public void display(Location location) {
            CommandSender s = Bukkit.getConsoleSender();
            Bukkit.dispatchCommand(s, getCommand()
                    .replace("{X}", location.getBlockX() + "")
                    .replace("{Y}", (location.getBlockY()) + "")
                    .replace("{Z}", location.getBlockZ() + ""));
        }

    }

    private static JoinEffectType[] allEffectTypes = new JoinEffectType[] {
            JoinEffectType.DEFAULT, JoinEffectType.MAGIC, JoinEffectType.LOVE_FIRST_SIGHT, JoinEffectType.TWINKLE_TWINKLE,
            JoinEffectType.SOLID_GREEN, JoinEffectType.CONFETTI, JoinEffectType.CONFETTI_2, JoinEffectType.USA,
            JoinEffectType.RED_WHITE_BOOM, JoinEffectType.DRAGON_TEARS, JoinEffectType.GOLDEN_SHOWERS, JoinEffectType.ACID_RAIN,
            JoinEffectType.JAMES_BOND, JoinEffectType.DOUBLE_O
    };

    private static Map<UUID, String> localEffectData = new HashMap<>();
    public static Map<UUID, String> getLocalEffectData() { return localEffectData; }

    public static JoinEffectType getTypeFromId(int id) {
        for (JoinEffectType type : allEffectTypes) {
            if (type.getId() == id) return type;
        } return null;
    }

    public static String getName(int id) {
        return getTypeFromId(id).getName();
    }

    public static String getCommand(int id) {
        return getTypeFromId(id).getCommand();
    }

    public static String getDefaultDatabaseData() {
        String query = "*";
        for (JoinEffectType type : allEffectTypes) {

            int access = 0;
            if (type.equals(JoinEffectType.DEFAULT)) access = 1;

            String comma = ",";
            if (type.getId() == allEffectTypes.length - 1) comma = "";

            query = query + type.getId() + ":" + access + comma;
        } return query;
    }

    public static String getEffectData(UUID uuid) {
        if (!CosmeticManager.containsUser(uuid)) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                CosmeticManager.generateNew(uuid);
            }
            return "";
        }

        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT join_effect FROM `hub_cosmetics` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            String data = result.getString("join_effect");

            if (getLocalEffectData().containsKey(uuid))
                localEffectData.remove(uuid);

            localEffectData.put(uuid, data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    public static String getEffectData(Player player) {
        return getEffectData(player.getUniqueId());
    }

    public static boolean hasAccess(UUID uuid, int id) {
        if (!getLocalEffectData().containsKey(uuid))
            localEffectData.put(uuid, getEffectData(uuid));

        String[] data = getLocalEffectData().get(uuid).split(",");
        String entry = data[id];
        return entry.split(":", 1).equals(1);
    }

    public static boolean hasAccess(Player player, int id) {
        return hasAccess(player.getUniqueId(), id);
    }

    public static boolean hasAccess(UUID uuid, JoinEffectType effectType) {
        if (!getLocalEffectData().containsKey(uuid))
            localEffectData.put(uuid, getEffectData(uuid));

        String[] data = getLocalEffectData().get(uuid).split(",");
        String entry = data[effectType.getId()];
        return entry.split(":", 1).equals(1);
    }

    public static boolean hasAccess(Player player, JoinEffectType effectType) {
        return hasAccess(player.getUniqueId(), effectType);
    }

    public static JoinEffectType getActiveEffect(UUID uuid) {
        if (!getLocalEffectData().containsKey(uuid))
            localEffectData.put(uuid, getEffectData(uuid));

        String[] data = getLocalEffectData().get(uuid).split(",");
        for (String d : data) {
            if (d.contains("*")) {
                int id = Integer.valueOf(d.split(":")[0].replace("*", ""));
                return getTypeFromId(id);
            }
        } return JoinEffectType.DEFAULT;
    }

    public static JoinEffectType getActiveEffect(Player player) {
        return getActiveEffect(player.getUniqueId());
    }

    public static void setAccess(UUID uuid, DonorStar.StarColor star, boolean value) {
        String finalData = "";
        String data = getEffectData(uuid);
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
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `hub_cosmetics` SET join_effect=? WHERE uuid=?");
            sql.setString(1, finalData);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setActiveEffect(UUID uuid, DonorStar.StarColor star) {
        String data = getEffectData(uuid);
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
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `hub_cosmetics` SET join_effect=? WHERE uuid=?");
            sql.setString(1, finalData);
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
