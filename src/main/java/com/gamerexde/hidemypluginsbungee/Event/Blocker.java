package com.gamerexde.hidemypluginsbungee.Event;

import com.gamerexde.hidemypluginsbungee.Database.Database;
import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import com.gamerexde.hidemypluginsbungee.Utils.DateGenerator;
import com.gamerexde.hidemypluginsbungee.Utils.IDGenerator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Blocker implements Listener {
    private HideMyPluginsBungee plugin;

    public Blocker(HideMyPluginsBungee instance) {
        plugin = instance;
    }

    @EventHandler(priority = Byte.MAX_VALUE)
    public void onPlayerChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String[] msg = event.getMessage().split(" ");
        String server = ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName();

        if (event.isCancelled()) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        if (!event.isCommand()) {
            return;
        }
        for (final String command : plugin.getBlockedCommands()) {
            for (final String whitelistedCommands : plugin.getWhitelistedCommands()) {
                if (msg[0].toLowerCase().equals("/" + whitelistedCommands)) {
                    return;
                } else {
                    continue;
                }
            }
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.access")) {
                    return;
                } else {
                    event.setCancelled(true);
                }
            } else {
                if (!msg[0].toLowerCase().contains(command) || !msg[0].toLowerCase().contains(":")) {
                    continue;
                }
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("deny.block_message")));
                for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                    if (online.hasPermission("hidemyplugins.notify.message")) {

                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = this.plugin.getMsgConfig().getString("admin_message.block_bungee_admin_notify_message");
                            message = message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName());
                            message = message.replace("{SERVER}", ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName());

                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName())));
                        }
                    }
                }
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")){
                        Database con = plugin.getMySQL();

                        String name = ((ProxiedPlayer) event.getSender()).getName();
                        String uuid = ((ProxiedPlayer) event.getSender()).getUniqueId().toString();
                        String executedCommand = msg[0];

                        PreparedStatement send = con.executeCommand(IDGenerator.getAlphaNumericString(),uuid,name,executedCommand, DateGenerator.getDate());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        for (final String command : plugin.getBlockedCommands()) {
            for (final String whitelistedCommands : plugin.getWhitelistedCommands()) {
                if (msg[0].toLowerCase().equals("/" + whitelistedCommands)) {
                    return;
                } else {
                    continue;
                }
            }
            if (msg[0].toLowerCase().equals("/" + command)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("deny.block_message")));
                for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                    if (online.hasPermission("hidemyplugins.notify.message")) {
                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = this.plugin.getMsgConfig().getString("admin_message.block_bungee_admin_notify_message");
                            message = message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName());
                            message = message.replace("{SERVER}", ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName());

                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                }
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")) {
                        Database con = plugin.getMySQL();

                        String name = ((ProxiedPlayer) event.getSender()).getName();
                        String uuid = ((ProxiedPlayer) event.getSender()).getUniqueId().toString();
                        String executedCommand = msg[0];

                        PreparedStatement send = con.executeCommand(IDGenerator.getAlphaNumericString(),uuid,name,executedCommand, DateGenerator.getDate());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
