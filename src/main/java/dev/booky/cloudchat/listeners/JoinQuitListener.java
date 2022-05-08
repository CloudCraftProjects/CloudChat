package dev.booky.cloudchat.listeners;
// Created by booky10 in CloudChat (05:26 08.05.22)

import dev.booky.cloudchat.CloudChatMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record JoinQuitListener(CloudChatMain main) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        main.createTeam(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        main.removeTeam(event.getPlayer());
    }
}
