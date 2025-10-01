package dev.booky.cloudchat;
// Created by booky10 in CloudChat (11:55 29.10.22)

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public interface CloudChatApi {

    default Scoreboard getVanillaScoreboard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    // creation

    default boolean createTeam(Player player) {
        return this.createTeam(this.getVanillaScoreboard(), player);
    }

    default boolean createTeam(Scoreboard scoreboard, Player player) {
        return this.createTeam(scoreboard, player.getUniqueId(), player.getName());
    }

    default boolean createTeam(UUID playerId, String username) {
        return this.createTeam(this.getVanillaScoreboard(), playerId, username);
    }

    boolean createTeam(Scoreboard scoreboard, UUID playerId, String username);

    // removal

    default boolean removeTeam(Player player) {
        return this.removeTeam(this.getVanillaScoreboard(), player);
    }

    default boolean removeTeam(Scoreboard scoreboard, Player player) {
        return this.removeTeam(scoreboard, player.getUniqueId(), player.getName());
    }

    default boolean removeTeam(UUID playerId, String username) {
        return this.removeTeam(this.getVanillaScoreboard(), playerId, username);
    }

    boolean removeTeam(Scoreboard scoreboard, UUID playerId, String username);

    // updates

    default boolean updateTeam(Player player) {
        return this.updateTeam(this.getVanillaScoreboard(), player);
    }

    default boolean updateTeam(Scoreboard scoreboard, Player player) {
        return this.updateTeam(scoreboard, player.getUniqueId(), player.getName());
    }

    default boolean updateTeam(UUID playerId, String username) {
        return this.updateTeam(this.getVanillaScoreboard(), playerId, username);
    }

    boolean updateTeam(Scoreboard scoreboard, UUID playerId, String username);
}
