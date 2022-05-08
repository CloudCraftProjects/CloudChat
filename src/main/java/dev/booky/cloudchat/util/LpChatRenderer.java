package dev.booky.cloudchat.util;
// Created by booky10 in CloudChat (05:13 08.05.22)

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LpChatRenderer implements ChatRenderer.ViewerUnaware {

    private static final LuckPerms LP = LuckPermsProvider.get();
    private static final LpChatRenderer INSTANCE = new LpChatRenderer();

    private LpChatRenderer() {
    }

    public static ChatRenderer create() {
        return ChatRenderer.viewerUnaware(INSTANCE);
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message) {
        TextComponent.Builder builder = Component.text().color(NamedTextColor.GRAY);

        User user = LP.getUserManager().getUser(source.getUniqueId());
        if (user != null) {
            String prefixString = user.getCachedData().getMetaData().getPrefix();
            if (prefixString != null) {
                LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
                builder.append(legacy.deserialize(prefixString)).append(Component.text(" \u25cf ", NamedTextColor.DARK_GRAY));
            }
        }

        builder.append(sourceDisplayName);
        return Component.translatable("chat.type.text", builder.build(), message);
    }
}
