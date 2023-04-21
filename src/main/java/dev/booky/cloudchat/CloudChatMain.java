package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:11 08.05.22)

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CloudChatMain extends JavaPlugin {

    private CloudChatManager manager;

    public CloudChatMain() {
        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Please use paper for this plugin to function! Download it at https://papermc.io/.");
        }
    }

    @Override
    public void onLoad() {
        this.manager = new CloudChatManager();
        Bukkit.getServicesManager().register(CloudChatApi.class, this.manager, this, ServicePriority.Normal);

        new Metrics(this, 18256);
    }

    @Override
    public void onEnable() {
        PaperConfigChecker.checkVanillaColoring(this.getSLF4JLogger());

        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this.manager), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.manager.createTeam(player);
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.manager.removeTeam(player);
        }
    }
}
