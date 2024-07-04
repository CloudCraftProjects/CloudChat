package dev.booky.cloudchat;
// Created by booky10 in CloudChat (12:51 04.08.22)

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
final class PaperConfigChecker {

    private PaperConfigChecker() {
    }

    public static void checkVanillaColoring(Logger logger) throws IllegalStateException {
        List<World> worlds = Bukkit.getWorlds();
        List<Key> invalidWorlds = new ArrayList<>(worlds.size());
        for (World world : worlds) {
            if (!hasVanillaScoreboardColoring(world)) {
                invalidWorlds.add(world.key());
            }
        }

        if (invalidWorlds.isEmpty()) {
            return;
        }

        logger.warn("'scoreboards.use-vanilla-world-scoreboard-name-coloring' is not enabled in {}",
                worlds.size() == invalidWorlds.size() ? "all worlds" : invalidWorlds.toString());
        logger.warn("This plugin doesn't change the chat format, this option is required to be enabled for chat formatting");
    }

    private static boolean hasVanillaScoreboardColoring(World world) {
        try {
            Object serverLevel = world.getClass().getMethod("getHandle").invoke(world);
            Object paperWorldCfg = serverLevel.getClass().getSuperclass().getMethod("paperConfig").invoke(serverLevel);
            Object scoreboardsCfg = paperWorldCfg.getClass().getField("scoreboards").get(paperWorldCfg);
            return scoreboardsCfg.getClass().getField("useVanillaWorldScoreboardNameColoring").getBoolean(scoreboardsCfg);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
