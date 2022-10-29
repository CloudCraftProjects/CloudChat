package dev.booky.cloudchat;
// Created by booky10 in CloudChat (05:11 08.05.22)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApiStatus.Internal
public class CloudChatMain extends JavaPlugin implements CloudChatApi {

    private static final DecimalFormat FORMAT = new DecimalFormat("0000");
    private static final Component SEPERATOR = Component.text(" \u25cf ", NamedTextColor.DARK_GRAY);

    private final Map<UUID, Team> teams = new HashMap<>();

    @Override
    public void onLoad() {
        PaperConfigChecker.ensurePaper();
        PaperConfigChecker.ensureVanillaColoring();

        Bukkit.getServicesManager().register(CloudChatApi.class, this, this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), this);
        Bukkit.getOnlinePlayers().forEach(this::createTeam);
    }

    @Override
    public void onDisable() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        teams.values().removeIf(team -> {
            if (scoreboard.getTeams().contains(team)) {
                team.unregister();
            }
            return true;
        });
    }

    @Override
    public boolean createTeam(Player player) {
        LuckPerms luckperms = LuckPermsProvider.get();

        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        Group group = luckperms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
            return false;
        }

        String teamPrefix = FORMAT.format(9999 - group.getWeight().orElse(0));
        if (player.getName().length() > 16 - 4) {
            teamPrefix += player.getName().substring(0, 16 - 4);
        } else {
            teamPrefix += player.getName();
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam(teamPrefix);
        if (team != null) {
            team.unregister();
        }
        team = scoreboard.registerNewTeam(teamPrefix);

        updateTeam(user, team);
        team.addPlayer(player);

        teams.put(player.getUniqueId(), team);
        return true;
    }

    @Override
    public boolean removeTeam(Player player) {
        Team team = teams.remove(player.getUniqueId());
        if (team == null) {
            return false;
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeams().contains(team)) {
            team.unregister();
        }

        return true;
    }

    @Override
    public boolean updateTeam(Player player) {
        LuckPerms luckperms = LuckPermsProvider.get();
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        Team team = teams.get(player.getUniqueId());
        if (team != null) {
            updateTeam(user, team);
            return true;
        }
        return false;
    }

    private void updateTeam(User user, Team team) {
        LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
        String prefixString = user.getCachedData().getMetaData().getPrefix();
        String suffixString = user.getCachedData().getMetaData().getSuffix();

        if (prefixString != null) {
            team.prefix(Component.text().color(NamedTextColor.WHITE)
                    .append(legacy.deserialize(prefixString))
                    .append(SEPERATOR)
                    .build());
        }
        if (suffixString != null) {
            team.suffix(Component.text().color(NamedTextColor.WHITE)
                    .append(SEPERATOR)
                    .append(legacy.deserialize(suffixString))
                    .build());
        }

        team.color(NamedTextColor.GRAY);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
    }
}
