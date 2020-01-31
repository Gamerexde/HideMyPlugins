package com.gamerexde.hidemypluginsbungee.Event;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        command = command.substring(1);
        if (this.plugin.equalsIgnoreCase(this.plugin.getBlockedCommands(), command)) {
            event.setCancelled(true);
            if (plugin.getConfig().getBoolean("tabCompletionLoggin")) {
                try {
                    if (plugin.getConfig().getBoolean("use-mysql")) {
                        Connection con = plugin.getMySQL().getConnection();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        String name = player.getName();
                        String uuid = player.getUniqueId().toString();
                        String date = dtf.format(now);
                        String executedCommand = "TAB COMPLETION";

                        String id = createIDString();

                        PreparedStatement create = con.prepareStatement("INSERT INTO `"
                                + plugin.getConfig().getString("MySQL.table_name")
                                + "` (`ID`, `UUID`, `USER`, `EXECUTED_COMMAND`, `DATE`) VALUES ('"
                                + id + "', '"
                                + uuid + "', '"
                                + name + "', '" + executedCommand
                                + "', '" + date
                                + "');");

                        create.executeUpdate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String createIDString() {
        int n = 6;

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
