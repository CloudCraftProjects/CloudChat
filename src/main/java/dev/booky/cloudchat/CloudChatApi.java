package dev.booky.cloudchat;
// Created by booky10 in CloudChat (11:55 29.10.22)

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface CloudChatApi {

    default Scoreboard getVanillaScoreboard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    default boolean createTeam(Player player) {
        return this.createTeam(this.getVanillaScoreboard(), player);
    }

    boolean createTeam(Scoreboard scoreboard, Player player);

    default boolean removeTeam(Player player) {
        return this.removeTeam(this.getVanillaScoreboard(), player);
    }

    boolean removeTeam(Scoreboard scoreboard, Player player);

    default boolean updateTeam(Player player) {
        return this.updateTeam(this.getVanillaScoreboard(), player);
    }

    boolean updateTeam(Scoreboard scoreboard, Player player);
}
