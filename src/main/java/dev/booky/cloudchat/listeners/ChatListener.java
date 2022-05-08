package dev.booky.cloudchat.listeners;
// Created by booky10 in CloudChat (05:13 08.05.22)

import dev.booky.cloudchat.util.LpChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.renderer(LpChatRenderer.create());
    }
}
