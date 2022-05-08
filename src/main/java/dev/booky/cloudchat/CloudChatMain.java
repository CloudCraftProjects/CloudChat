package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:11 08.05.22)

import dev.booky.cloudchat.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudChatMain extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }
}
