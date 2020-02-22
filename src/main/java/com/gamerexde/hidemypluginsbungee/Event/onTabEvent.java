package com.gamerexde.hidemypluginsbungee.Event;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import com.gamerexde.hidemypluginsbungee.Utils.DateGenerator;
import com.gamerexde.hidemypluginsbungee.Utils.IDGenerator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class onTabEvent implements Listener {

    private HideMyPluginsBungee plugin;

    public onTabEvent(final HideMyPluginsBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = Byte.MAX_VALUE)
    public void onPlayerTab(final TabCompleteEvent event) {
        final ProxiedPlayer player = (ProxiedPlayer)event.getSender();
        String command = event.getCursor().split(" ")[0].toLowerCase();

        if (event.isCancelled()) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer)) {
        return;
        }
        if (player.hasPermission("hidemyplugins.access")) {
            return;
        }
        if (command.length() < 1) {
            return;
        }
        if (command.equals("/")) {
            event.setCancelled(true);
            return;
        }
        if (command.contains(":")) {
            event.setCancelled(true);
            return;
        }
        command = command.substring(1);
        if (this.plugin.equalsIgnoreCase(this.plugin.getBlockedCommands(), command)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("deny.block_message")));
            if (plugin.getConfig().getBoolean("tabCompletionLoggin")) {
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")) {
                        Connection con = plugin.getMySQL().getConnection();

                        String name = player.getName();
                        String uuid = player.getUniqueId().toString();
                        String executedCommand = "TAB COMPLETION";

                        PreparedStatement create = con.prepareStatement("INSERT INTO `"
                                + plugin.getConfig().getString("MySQL.table_name")
                                + "` (`ID`, `UUID`, `USER`, `EXECUTED_COMMAND`, `DATE`) VALUES ('"
                                + IDGenerator.getAlphaNumericString() + "', '"
                                + uuid + "', '"
                                + name + "', '" + executedCommand
                                + "', '" + DateGenerator.getDate()
                                + "');");

                        create.executeUpdate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
