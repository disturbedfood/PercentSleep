package cc.crypticcraft.percentsleep;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.kitteh.vanish.VanishPlugin;

public class PercentSleepListener implements Listener {
    private final PercentSleep plugin;
    private final VanishPlugin vanish = (VanishPlugin) (VanishPlugin) Bukkit.getServer().getPluginManager().getPlugin("VanishNoPacket");

    public PercentSleepListener(PercentSleep plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        PercentSleepWorld world = plugin.getWorlds().get(event.getPlayer().getWorld().getName());
        if (world != null) {
            Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + ChatColor.GOLD + " has gone to sleep in " + world.getDisplayName() + ".");
            world.setPlayersSleeping(world.getPlayersSleeping() + 1);
            world.skipNightIfPossible(true);
        }
    }

    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        PercentSleepWorld world = plugin.getWorlds().get(event.getPlayer().getWorld().getName());
        if (world != null) {
            boolean skipped = world.skipNightIfPossible(false);
            if (world.isNight() && !skipped) {
                Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + ChatColor.GOLD + " is no longer sleeping.");
                world.setPlayersSleeping(world.getPlayersSleeping() - 1);
            }
            else if (!world.isNight() && !skipped) {
                world.setPlayersSleeping(0);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        PercentSleepWorld world = plugin.getWorlds().get(event.getPlayer().getWorld().getName());
        if (world != null) {
            world.skipNightIfPossible(false);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        PercentSleepWorld world = plugin.getWorlds().get(event.getPlayer().getWorld().getName());
        if (world != null) {
            if (event.getPlayer().isSleeping()) world.setPlayersSleeping(world.getPlayersSleeping() - 1);
            world.skipNightIfPossible(false);
        }
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        PercentSleepWorld worldTo = plugin.getWorlds().get(event.getPlayer().getWorld().getName());
        PercentSleepWorld worldFrom = plugin.getWorlds().get(event.getFrom().getName());

        if (worldTo != null) {
            worldTo.skipNightIfPossible(false);
        }
        if (worldFrom != null) {
            worldFrom.skipNightIfPossible(false);
        }
    }
}
