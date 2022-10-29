package dev.booky.cloudchat;
// Created by booky10 in CloudChat (12:51 04.08.22)

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
class PaperConfigChecker {

    public static void ensurePaper() throws IllegalStateException {
        try {
            Class.forName("io.papermc.paper.configuration.WorldConfiguration");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Please install paper for this plugin to function");
        }
    }

    public static void ensureVanillaColoring() throws IllegalStateException {
        for (World world : Bukkit.getWorlds()) {
            ensureVanillaColoring(world);
        }
    }

    public static void ensureVanillaColoring(World world) throws IllegalStateException {
        try {
            Object serverLevel = world.getClass().getMethod("getHandle").invoke(world);
            Object paperWorldCfg = serverLevel.getClass().getSuperclass().getMethod("paperConfig").invoke(serverLevel);
            Object scoreboardsCfg = paperWorldCfg.getClass().getField("scoreboards").get(paperWorldCfg);
            boolean useVanillaWorldScoreboardNameColoring = scoreboardsCfg.getClass().getField("useVanillaWorldScoreboardNameColoring").getBoolean(scoreboardsCfg);
            if (!useVanillaWorldScoreboardNameColoring) {
                throw new IllegalStateException("'use-vanilla-world-scoreboard-name-coloring' is not enabled in world " + world.getKey());
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Exception occurred while ensuring 'use-vanilla-world-scoreboard-name-coloring' is enabled", exception);
        }
    }
}
