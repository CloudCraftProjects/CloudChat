package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:26 08.05.22)

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

class JoinQuitListener implements Listener {

    private final Plugin plugin;
    private final CloudChatApi api;

    public JoinQuitListener(Plugin plugin, CloudChatApi api) {
        this.plugin = plugin;
        this.api = api;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerConnectionValidateLoginEvent event) {
        if (event.getKickMessage() != null) {
            return; // player will be kicked, don't create team
        }
        if (!(event.getConnection() instanceof PlayerConfigurationConnection connection)) {
            return; // player isn't exiting configuration phase, skip
        }
        // assume already on global tick thread
        PlayerProfile profile = connection.getProfile();
        this.api.createTeam(profile.getId(), profile.getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        // move to global tick thread
        Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> this.api.removeTeam(event.getPlayer()));
    }
}
