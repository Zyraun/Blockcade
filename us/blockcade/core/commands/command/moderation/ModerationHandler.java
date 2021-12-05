/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison, September 2017
 */

package us.blockcade.core.commands.command.moderation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModerationHandler implements Listener {

    public ModerationHandler() {}

    /**
     * Storing vanished players in a list for handling use
     * Will be cleared upon server reload/restart
     */
    private List<UUID> vanishedPlayers = new ArrayList<>();
    public List<UUID> getVanishedPlayers() { return vanishedPlayers; }

    public boolean isPlayerVanished(Player player) { return getVanishedPlayers().contains(player.getUniqueId()); }

    public void addVanishedPlayer(Player player) {
        if (isPlayerVanished(player)) return;
        vanishedPlayers.add(player.getUniqueId());
    }

    public void removeVanishedPlayer(Player player) {
        if (!isPlayerVanished(player)) return;
        vanishedPlayers.remove(player.getUniqueId());
    }

    /**
     * Hiding join messages of vanished players
     * Will still display to staff members
     *
     * @param event Called when a player joins the server
     */
    @EventHandler
    public void onVanishedJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerVanished(player)) return;
        event.setJoinMessage("");

        // TODO: Announce to online staff over Bungee
    }

    /**
     * Hiding quit messages of vanished players
     * Will still display to staff members
     *
     * @param event Called when a player quits the server
     */
    @EventHandler
    public void onVanishedLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerVanished(player)) return;
        event.setQuitMessage("");

        // TODO: Announce to online staff over Bungee
    }

}
