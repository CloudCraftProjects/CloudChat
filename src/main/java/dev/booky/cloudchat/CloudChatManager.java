package dev.booky.cloudchat;
// Created by booky10 in CloudChat (22:28 21.04.23)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;

class CloudChatManager implements CloudChatApi {

    private static final DecimalFormat FORMAT = new DecimalFormat("0000");
    private static final Component SEPARATOR = Component.text(" \u25cf ", NamedTextColor.DARK_GRAY);

    @Override
    public boolean createTeam(Scoreboard scoreboard, Player player) {
        LuckPerms luckperms = LuckPermsProvider.get();

        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        Group group = luckperms.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
            return false;
        }

        String teamName = FORMAT.format(9999 - group.getWeight().orElse(0))
                + RandomStringUtils.randomAlphanumeric(16 - 4);

        Team team = scoreboard.getPlayerTeam(player);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.addPlayer(player);
        }
        this.updateTeam(user, team);
        return true;
    }

    @Override
    public boolean removeTeam(Scoreboard scoreboard, Player player) {
        Team team = scoreboard.getPlayerTeam(player);
        if (team == null) {
            return false;
        }
        team.unregister();
        return true;
    }

    @Override
    public boolean updateTeam(Scoreboard scoreboard, Player player) {
        if (!player.isOnline()) {
            return false;
        }

        LuckPerms luckperms = LuckPermsProvider.get();
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) {
            this.updateTeam(user, team);
            return true;
        }
        return false;
    }

    private void updateTeam(User user, Team team) {
        MiniMessage serializer = MiniMessage.miniMessage();
        CachedMetaData meta = user.getCachedData().getMetaData();

        String prefixStr = meta.getPrefix();
        if (prefixStr != null) {
            team.prefix(serializer.deserialize(prefixStr).colorIfAbsent(NamedTextColor.WHITE).append(SEPARATOR));
        } else {
            team.prefix(Component.empty());
        }

        String suffixStr = meta.getSuffix();
        if (suffixStr != null) {
            team.suffix(SEPARATOR.append(serializer.deserialize(suffixStr).colorIfAbsent(NamedTextColor.WHITE)));
        } else {
            team.suffix(Component.empty());
        }

        team.color(NamedTextColor.GRAY);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
    }
}
