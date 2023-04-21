package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:26 08.05.22)

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class JoinQuitListener implements Listener {

    private final CloudChatApi api;

    public JoinQuitListener(CloudChatApi api) {
        this.api = api;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        this.api.createTeam(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        this.api.removeTeam(event.getPlayer());
    }
}
