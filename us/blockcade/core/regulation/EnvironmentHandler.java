package us.blockcade.core.regulation;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.blockcade.core.commands.command.admin.AoCmd;
import us.blockcade.core.game.GameHandler;

public class EnvironmentHandler implements Listener {

    /**
     * Setting environmental factors such as weather, hunger, and block physics.
     * i.e. Set details using 'Blockcade.getInstance().getEnvironmentHandler().setDoWeather(true)'
     */

    public EnvironmentHandler() {}

    // Variables of environment control
    private boolean doWeather = false;
    private boolean doMelt = false;
    private boolean doDecay = false;
    private boolean doBlockPhysics = false;
    private boolean doPassiveSpawn = false;
    private boolean doHostileSpawn = false;
    private boolean doFallDamage = false;
    private boolean doHunger = false;
    private boolean doAllDamage = false;
    private boolean doPvp = false;
    private boolean doBreakBlocks = false;
    private boolean doPlaceBlocks = false;
    private boolean doExplosions = false;

    // Getter functions for variables
    public boolean canDoWeather() { return doWeather; }
    public boolean canDoMelt() { return doMelt; }
    public boolean canDoDecay() { return doDecay; }
    public boolean canDoBlockPhysics() { return doBlockPhysics; }
    public boolean canDoPassiveSpawn() { return doPassiveSpawn; }
    public boolean canDoHostileSpawn() { return doHostileSpawn; }
    public boolean canDoFallDamage() { return doFallDamage; }
    public boolean canDoHunger() { return doHunger; }
    public boolean canDoAllDamage() { return doAllDamage; }
    public boolean canDoPvP() { return doPvp; }
    public boolean canDoBreakBlocks() { return doBreakBlocks; }
    public boolean canDoPlaceBlocks() { return doPlaceBlocks; }
    public boolean canDoExplosions() { return doExplosions; }

    // Setter functions for variables
    public void setDoWeather(boolean value) { doWeather = value; }
    public void setDoMelt(boolean value) { doMelt = value; }
    public void setDoDecay(boolean value) { doDecay = value; }
    public void setDoBlockPhysics(boolean value) { doBlockPhysics = value; }
    public void setDoPassiveSpawn(boolean value) { doPassiveSpawn = value; }
    public void setDoHostileSpawn(boolean value) { doHostileSpawn = value; }
    public void setDoFallDamage(boolean value) { doFallDamage = value; }
    public void setDoHunger(boolean value) { doHunger = value; }
    public void setDoAllDamage(boolean value) { doAllDamage = value; }
    public void setDoPvp(boolean value) { doPvp = value; }
    public void setDoBreakBlocks(boolean value) { doBreakBlocks = value; }
    public void setDoPlaceBlocks(boolean value) { doPlaceBlocks = value; }
    public void setDoExplosions(boolean value) { doExplosions = value; }

    // canDoWeather()
    @EventHandler
    public void onChangeWeather(WeatherChangeEvent event) {
        if (canDoWeather()) return;
        event.setCancelled(true);
    }

    // canDoMelt()
    @EventHandler
    public void onMelt(BlockFadeEvent event) {
        if (canDoMelt()) return;
        Block block = event.getBlock();
        if (block.getType().equals(Material.ICE) || block.getType().equals(Material.SNOW_BLOCK)) {
            event.setCancelled(true);
        }
    }

    // canDoDecay() {
    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        if (canDoDecay()) return;
        event.setCancelled(true);
    }

    // canDoBlockPhysics()
    @EventHandler
    public void onBlockGravity(BlockPhysicsEvent event) {
        if (canDoBlockPhysics()) return;
        event.setCancelled(true);
    }

    // canDoPassiveSpawn()
    @EventHandler
    public void onPassiveSpawn(EntitySpawnEvent event) {
        if (canDoPassiveSpawn()) return;
        Entity entity = event.getEntity();
        if (entity instanceof Creature) {
            if (!(entity instanceof Monster))
                event.setCancelled(true);
        }
    }

    // canDoHostileSpawn()
    @EventHandler
    public void onHostileSpawn(EntitySpawnEvent event) {
        if (canDoHostileSpawn()) return;
        Entity entity = event.getEntity();
        if (entity instanceof Creature) {
            if (entity instanceof Monster)
                event.setCancelled(true);
        }
    }

    // canDoFallDamage()
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (canDoFallDamage()) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause.equals(EntityDamageEvent.DamageCause.FALL))
            event.setCancelled(true);
    }

    // canDoHunger()
    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (canDoHunger()) return;
        event.setCancelled(true);
        Player player = (Player) event.getEntity();
        player.setFoodLevel(20);
    }

    // canDoAllDamage()
    @EventHandler
    public void onAllDamage(EntityDamageEvent event) {
        if (canDoAllDamage()) return;
        if (GameHandler.started) return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        event.setCancelled(true);
    }

    // canDoPvP()
    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (canDoPvP()) {
            return;
        }
        if (GameHandler.started) return;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (AoCmd.hasOverride((Player) event.getDamager())) return;
            event.setCancelled(true);
        }
        if (event.getDamager().getType().equals(EntityType.ARROW)) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player && event.getEntity() instanceof Player)
                event.setCancelled(true);
        }
    }

    // canDoBreakBlocks()
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (canDoBreakBlocks()) return;
        if (AoCmd.hasOverride(event.getPlayer())) return;
        event.setCancelled(true);
    }

    // canDoBreakBlocks()
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (canDoPlaceBlocks()) return;
        if (AoCmd.hasOverride(event.getPlayer())) return;
        event.setCancelled(true);
    }

    // canDoExplosions()
    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (canDoExplosions()) return;
        event.setCancelled(true);
        event.getLocation().getWorld().playEffect(event.getLocation(), Effect.EXPLOSION_LARGE, 1F);
        event.getLocation().getWorld().playSound(event.getLocation(), Sound.EXPLODE, 10F, 10F);
    }

}
