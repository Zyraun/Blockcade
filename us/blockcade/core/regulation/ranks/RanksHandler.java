/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison Hellum <addisonhellum@gmail.com>, September 2017
 */

package us.blockcade.core.regulation.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import us.blockcade.core.game.events.PlayerJoinGameEvent;
import us.blockcade.core.game.events.PlayerQuitGameEvent;
import us.blockcade.core.regulation.cosmetics.chatstar.DonorStar;
import us.blockcade.core.regulation.rewards.RewardManager;
import us.blockcade.core.regulation.tiers.Tier;
import us.blockcade.core.regulation.tiers.TierHandler;
import us.blockcade.core.utility.*;
import us.blockcade.core.utility.mysql.BlockcadeDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RanksHandler implements Listener {

    public static Rank[] ranksList = new Rank[] { Rank.DEFAULT, Rank.STEEL, Rank.GOLD, Rank.GEM, Rank.VIP,
    Rank.MOD, Rank.SRMOD, Rank.ADMIN, Rank.OWNER};

    private static HashMap<String, Integer> localPlayerRanks = new HashMap<>();
    public static HashMap<String, Integer> getLocalPlayerRanks() { return localPlayerRanks; }

    private static List<String> localDonors = new ArrayList<>();

    public static void initializeLocalRanks() {
        localPlayerRanks.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!BlockcadeDB.containsUserData(p)) {
                generateNew(p);
            }
            localPlayerRanks.put(p.getName(), getRank(p.getUniqueId()).getId());
            NametagUtil.updateNameTag(p);
        }
    }

    public static void updateLocalRank(Player player) {
        if (localRankRegistered(player)) {
            localPlayerRanks.remove(player.getName());
        }
        localPlayerRanks.put(player.getName(), getRank(player.getUniqueId()).getId());
        NametagUtil.updateNameTag(player);
    }

    public static Rank getRankFromID(int id) {
        return ranksList[id];
    }

    public static Rank getRank(UUID uuid) {
        if (!BlockcadeDB.containsUserData(uuid)) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                generateNew(Bukkit.getPlayer(uuid));
            }
            return Rank.DEFAULT;
        }

        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT rank FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            return getRankFromID(result.getInt("rank"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Rank getLocalRank(Player player) {
        if (!localRankRegistered(player)) {
            return null;
        }
        return getRankFromID(localPlayerRanks.get(player.getName()));
    }

    public static boolean localRankRegistered(Player player) {
        return localPlayerRanks.containsKey(player.getName());
    }

    public static boolean hasPermissions(Player player, Rank rank) {
        return getLocalRank(player).getId() >= rank.getId();
    }

    public static boolean hasPermissions(UUID uuid, Rank rank) {
        return getRank(uuid).getId() >= rank.getId();
    }

    public static void setRank(UUID uuid, Rank rank) {
        if (!BlockcadeDB.containsUserData(uuid)) return;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("UPDATE `user_data` SET rank=? WHERE uuid=?");
            sql.setInt(1, rank.getId());
            sql.setString(2, uuid.toString());

            sql.executeUpdate();

            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                Player player = Bukkit.getPlayer(uuid);
                NametagUtil.updateNameTag(player);
            }
        }
    }

    public static void generateNew(Player player) {
        try {
            if (BlockcadeDB.containsUserData(player)) return;

            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement(
                    "INSERT INTO `user_data` VALUES (?,?,?,0,500,5000,100,false,false)");
            sql.setString(1, null);
            sql.setString(2, player.getUniqueId().toString());
            sql.setString(3, player.getName());

            sql.execute();
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " to the " +
                    ChatColor.AQUA + "" + ChatColor.BOLD + "Blockcade Network" + ChatColor.GRAY + " [#" + getId(player.getUniqueId()) + "]");
            RewardManager.rewardExperience(player, 1000);
        }
    }

    public static boolean isDonor(UUID uuid) {
        if (localDonors.contains(uuid.toString())) return true;
        if (hasPermissions(uuid, Rank.ADMIN)) return true;
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT donor FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            boolean donor = result.getBoolean("donor");

            if (donor && !localDonors.contains(uuid.toString())) {
                localDonors.add(uuid.toString());
            }

            return result.getBoolean("donor");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getId(UUID uuid) {
        try {
            PreparedStatement sql = BlockcadeDB.getConnection().prepareStatement("SELECT id FROM `user_data` WHERE uuid=?");
            sql.setString(1, uuid.toString());

            ResultSet result = sql.executeQuery();
            result.next();

            int id = result.getInt("id");

            return result.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!BlockcadeDB.containsUserData(player)) {
            generateNew(player);
        }
        updateLocalRank(player);
        NametagUtil.updateNameTag(player);

        TabTitleManager.sendPlayerListTab(player, "&bYou are playing on &lBlockcade.us", "&6Donate for ranks! &cstore.blockcade.us");
    }

    @EventHandler
    public void onRankedChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().replaceAll("%", "％");
        Rank rank = getLocalRank(player);

        if (getRank(player.getUniqueId()) != rank) {
            updateLocalRank(player);
            NametagUtil.updateNameTag(player);
        }

        if (hasPermissions(player, Rank.ADMIN))
            message = message.replaceAll("&", "§");

        String space = " ";
        if (getLocalRank(player).getId() == 0) space = "";
        String donorSymb = "" + space;
        if (isDonor(player.getUniqueId())) donorSymb = DonorStar.getActiveStar(player).getColor() + "◆" + space;
        Tier tier = TierHandler.getTier(player.getUniqueId());
        //String tierDisplay = ChatColor.WHITE + "(" + tier.getColor() + tier.getDisplay() + ChatColor.WHITE + ") ";
        String tierDisplay = "";
        event.setFormat(tierDisplay + rank.getColor() + "" + ChatColor.BOLD + rank.getName() + ChatColor.RESET + "" + donorSymb + rank.getColor() + player.getName() + ChatColor.GRAY + ": " + rank.getChatColor() + message);
    }

    @EventHandler
    public void onJoinGame(PlayerJoinGameEvent event) {
        Player player = event.getPlayer();

        if (!localRankRegistered(player))
            updateLocalRank(player);

        Rank rank = getLocalRank(player);

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[+] " + rank.getColor() + player.getName() + ChatColor.YELLOW + " joined.");
    }

    @EventHandler
    public void onQuitGame(PlayerQuitGameEvent event) {
        Player player = event.getPlayer();

        if (!localRankRegistered(player))
            updateLocalRank(player);

        Rank rank = getLocalRank(player);

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[-] " + rank.getColor() + player.getName() + ChatColor.YELLOW + " left.");
    }

}
