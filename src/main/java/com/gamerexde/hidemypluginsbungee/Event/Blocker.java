package com.gamerexde.hidemypluginsbungee.Event;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import com.gamerexde.hidemypluginsbungee.Utils.DateGenerator;
import com.gamerexde.hidemypluginsbungee.Utils.IDGenerator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("blockMessage")));
                for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                    if (online.hasPermission("hidemyplugins.notify.message")) {

                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = this.plugin.getMsgConfig().getString("blockBungeeAdminNotifyMessage");
                            message = message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName());
                            message = message.replace("{SERVER}", ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName());

                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName())));
                        }
                    }
                }
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")){
                        Connection con = plugin.getMySQL().getConnection();

                        String name = ((ProxiedPlayer) event.getSender()).getName();
                        String uuid = ((ProxiedPlayer) event.getSender()).getUniqueId().toString();
                        String executedCommand = msg[0];

                        PreparedStatement create = con.prepareStatement("INSERT INTO `" + plugin.getConfig().getString("MySQL.table_name")
                                + "` (`ID`, `UUID`, `USER`, `EXECUTED_COMMAND`, `DATE`) VALUES ('"
                                + IDGenerator.getAlphaNumericString()
                                + "', '" + uuid + "', '"
                                + name
                                + "', '"
                                + executedCommand
                                + "', '"
                                + DateGenerator.getDate()
                                + "');");

                        create.executeUpdate();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (final String command : plugin.getBlockedCommands()) {
            if (msg[0].toLowerCase().equals("/" + command)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("blockMessage")));
                for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                    if (online.hasPermission("hidemyplugins.notify.message")) {
                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = this.plugin.getMsgConfig().getString("blockBungeeAdminNotifyMessage");
                            message = message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName());
                            message = message.replace("{SERVER}", ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName());

                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{USER}", ((ProxiedPlayer) event.getSender()).getName())));
                        }
                    }
                }
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")){
                        Connection con = plugin.getMySQL().getConnection();

                        String name = ((ProxiedPlayer) event.getSender()).getName();
                        String uuid = ((ProxiedPlayer) event.getSender()).getUniqueId().toString();
                        String executedCommand = msg[0];


                        PreparedStatement create = con.prepareStatement("INSERT INTO `" + plugin.getConfig().getString("MySQL.table_name")
                                + "` (`ID`, `UUID`, `USER`, `EXECUTED_COMMAND`, `DATE`) VALUES ('"
                                + IDGenerator.getAlphaNumericString()
                                + "', '" + uuid + "', '"
                                + name
                                + "', '"
                                + executedCommand
                                + "', '"
                                + DateGenerator.getDate()
                                + "');");

                        create.executeUpdate();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
