/*
 * Copyright (C) Blockcade, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Addison, September 2017
 */

package us.blockcade.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.blockcade.core.commands.command.admin.*;
import us.blockcade.core.commands.command.donor.FlyCmd;
import us.blockcade.core.commands.command.gamemode.AdventureCmd;
import us.blockcade.core.commands.command.gamemode.CreativeCmd;
import us.blockcade.core.commands.command.gamemode.SpectatorCmd;
import us.blockcade.core.commands.command.gamemode.SurvivalCmd;
import us.blockcade.core.commands.command.general.MessageCmd;
import us.blockcade.core.commands.command.general.ReplyCmd;
import us.blockcade.core.commands.command.general.WalletCommand;
import us.blockcade.core.commands.command.moderation.*;
import us.blockcade.core.game.GameHandler;
import us.blockcade.core.game.GameSpectator;
import us.blockcade.core.game.events.ServerLoadEvent;
import us.blockcade.core.game.events.ServerUnloadEvent;
import us.blockcade.core.regulation.ConnectionHandler;
import us.blockcade.core.regulation.bans.BanType;
import us.blockcade.core.regulation.chat.ChatHandler;
import us.blockcade.core.regulation.ranks.RanksHandler;
import us.blockcade.core.regulation.EnvironmentHandler;
import us.blockcade.core.regulation.tiers.TierHandler;
import us.blockcade.core.utility.Testing;
import us.blockcade.core.utility.TimeUtil;
import us.blockcade.core.utility.gui.GUIHandler;
import us.blockcade.core.utility.mysql.BlockcadeDB;

public class Blockcade extends JavaPlugin implements Listener {

    public static boolean enableNetwork = true;

    private static Plugin main;
    public static Plugin getInstance() { return main; }

    private static EnvironmentHandler environmentHandler;
    private static ModerationHandler moderationHandler;

    @Override
    public void onEnable() {
        main = this;

        BlockcadeDB.openConnection();

        environmentHandler = new EnvironmentHandler();
        moderationHandler = new ModerationHandler();

        // Testing purposes
        getServer().getPluginManager().registerEvents(this, this);

        getServer().getPluginManager().registerEvents(new EnvironmentHandler(), this);
        getServer().getPluginManager().registerEvents(new ModerationHandler(), this);
        getServer().getPluginManager().registerEvents(new RanksHandler(), this);
        getServer().getPluginManager().registerEvents(new TierHandler(), this);
        getServer().getPluginManager().registerEvents(new GameHandler(), this);
        getServer().getPluginManager().registerEvents(new GUIHandler(), this);
        getServer().getPluginManager().registerEvents(new GameSpectator(), this);
        getServer().getPluginManager().registerEvents(new ConnectionHandler(), this);
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new Testing(), this);

        getCommand("survival").setExecutor(new SurvivalCmd());
        getCommand("creative").setExecutor(new CreativeCmd());
        getCommand("adventure").setExecutor(new AdventureCmd());
        getCommand("spectator").setExecutor(new SpectatorCmd());
        getCommand("vanish").setExecutor(new VanishCmd());
        getCommand("setrank").setExecutor(new SetrankCmd());
        getCommand("ao").setExecutor(new AoCmd());
        getCommand("fly").setExecutor(new FlyCmd());
        getCommand("verify").setExecutor(new VerifyCmd());
        getCommand("setlobby").setExecutor(new SetlobbyCmd());
        getCommand("reload").setExecutor(new ReloadCommand());
        getCommand("uuid").setExecutor(new UuidCmd());
        getCommand("map").setExecutor(new MapCmd());
        getCommand("message").setExecutor(new MessageCmd());
        getCommand("reply").setExecutor(new ReplyCmd());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("speed").setExecutor(new SpeedCmd());
        getCommand("skull").setExecutor(new SkullCommand());
        getCommand("wallet").setExecutor(new WalletCommand());
        getCommand("ban").setExecutor(new BanCommand());

        RanksHandler.initializeLocalRanks();

        ServerLoadEvent loadEvent = new ServerLoadEvent(this);
        Bukkit.getPluginManager().callEvent(loadEvent);
    }

    @Override
    public void onDisable() {
        ServerUnloadEvent unloadEvent = new ServerUnloadEvent(this);
        Bukkit.getPluginManager().callEvent(unloadEvent);

        BlockcadeDB.closeConnection();
    }

    public static EnvironmentHandler getEnvironmentHandler() { return environmentHandler; }
    public static ModerationHandler getModerationHandler() { return moderationHandler; }

}
