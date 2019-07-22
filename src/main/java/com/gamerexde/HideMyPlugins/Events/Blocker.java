package com.gamerexde.HideMyPlugins.Events;

import com.gamerexde.HideMyPlugins.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

public class Blocker implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final PlayerCommandPreprocessEvent event) {
        final String[] msg = event.getMessage().split(" ");
        final Player player = event.getPlayer();
        final FileConfiguration config = Main.getInstance().getConfig();
        final FileConfiguration msgconfig = Main.getInstance().getMsgConfig();
        for (final String command : Main.getInstance().getBlockedCommands()) {
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.notify.message")) {
                    if (Objects.requireNonNull(msgconfig.getString("grantMessage")).equalsIgnoreCase("none")) {
                        return;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("blockMessage"))));
                    return;
                }
                else {
                    event.setCancelled(true);
                    if (Objects.requireNonNull(msgconfig.getString("blockMessage")).equalsIgnoreCase("none")) {
                        return;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("blockMessage"))));
                }
            }
            else {
                if (!msg[0].toLowerCase().contains(command) || !msg[0].toLowerCase().contains(":")) {
                    continue;
                }
                event.setCancelled(true);
                if (Objects.requireNonNull(msgconfig.getString("blockMessage")).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("blockMessage"))));
            }
        }
        for (final String command : Main.getInstance().getBlockedCommands()) {
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.notify.message")) {
                    return;
                }
                event.setCancelled(true);
                if (Objects.requireNonNull(config.getString("blockedcommandsMessage")).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(config.getString("blockedcommandsMessage"))));
            }
        }
    }
}
