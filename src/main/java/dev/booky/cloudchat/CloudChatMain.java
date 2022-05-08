package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:11 08.05.22)

import dev.booky.cloudchat.listeners.ChatListener;
import dev.booky.cloudchat.listeners.JoinQuitListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudChatMain extends JavaPlugin {

    private static final DecimalFormat FORMAT = new DecimalFormat("0000");
    private final Map<UUID, Team> teams = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), this);

        Bukkit.getOnlinePlayers().forEach(this::createTeam);
    }

    @Override
    public void onDisable() {
        teams.values().removeIf(team -> {
            team.unregister();
            return true;
        });
    }

    public void createTeam(Player player) {
        LuckPerms luckperms = LuckPermsProvider.get();

        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        Group group = luckperms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return;

        String teamPrefix = FORMAT.format(9999 - group.getWeight().orElse(0));
        if (player.getName().length() > 16 - 4) {
            teamPrefix += player.getName().substring(0, 16 - 4);
        } else {
            teamPrefix += player.getName();
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam(teamPrefix);
        if (team != null) team.unregister();
        team = scoreboard.registerNewTeam(teamPrefix);

        String prefixString = user.getCachedData().getMetaData().getPrefix();
        if (prefixString != null) {
            LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
            team.prefix(legacy.deserialize(prefixString).append(Component.text(" \u25cf ", NamedTextColor.DARK_GRAY)));
        }

        team.color(NamedTextColor.GRAY);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        team.addPlayer(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player) {
        Team team = teams.get(player.getUniqueId());
        if (team != null) team.unregister();
    }
}
