package dev.booky.cloudchat;
// Created by booky10 in CloudChat (11:55 29.10.22)

import org.bukkit.entity.Player;

public interface CloudChatApi {

    boolean createTeam(Player player);

    boolean removeTeam(Player player);

    boolean updateTeam(Player player);
}
