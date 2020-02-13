package com.gamerexde.hidemypluginsbungee.Commands;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class hmpa extends Command {

    private HideMyPluginsBungee plugin;
    public hmpa(HideMyPluginsBungee instance) {
        super("hmpa");
        plugin = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("web")) {
                    if (player.hasPermission("hidemyplugins.admin.webinterface")) {
                        if (plugin.getConfig().getBoolean("UsingWebInterface")){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Access your Web Interface here: &e" + plugin.getConfig().getString("WebInterface")));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Web Interface is disabled in the &econfig.yml"));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("history")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Usage: &e/hmpa history <user> <page>"));
                }
                return;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase(args[1])) {
                    if (player.hasPermission("hidemyplugins.admin.history")) {
                        try {
                            if (plugin.getConfig().getBoolean("use-mysql")) {
                                Connection con = plugin.getMySQL().getConnection();

                                Statement stmt = con.createStatement();
                                Statement stmtCheckQuery = con.createStatement();

                                int pageNum = 0 ;
                                int pageLength = 10;

                                String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                        + " WHERE `USER` LIKE '"
                                        + args[1]
                                        + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                ResultSet rs = stmt.executeQuery(query);
                                ResultSet checkQuery = stmtCheckQuery.executeQuery(query);

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                if (checkQuery.next()) {
                                    String name = checkQuery.getString("USER");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Found commands for &e" + name + ""));
                                    stmtCheckQuery.close();
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &e" + args[1] + "&7 dosen't have used blocked commands. This user is clean!"));
                                    stmtCheckQuery.close();
                                    return;
                                }

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + "1" + "&8]            "));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                while (rs.next()) {
                                    String name = rs.getString("USER");
                                    String date = rs.getString("DATE");
                                    String executed_command = rs.getString("EXECUTED_COMMAND");
                                    String id = rs.getString("ID");
                                    if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date) );
                                        message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface") + "/profile.php?id=" + id));
                                        message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( ChatColor.translateAlternateColorCodes('&', "&eHideMyPlugins &bWeb Profile") ).create()));
                                        player.sendMessage(message);

                                    } else {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));

                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("noPerms") ));
                    }
                    return;
                }

            }
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase(args[2])) {
                    if (player.hasPermission("hidemyplugins.admin.history")) {
                        try {
                            if (plugin.getConfig().getBoolean("use-mysql")) {
                                Connection con = plugin.getMySQL().getConnection();

                                Statement stmt = con.createStatement();
                                Statement stmtCheckQuery = con.createStatement();

                                int page = Integer.parseInt(args[2]);

                                if (page < 2) {
                                    int pageNum = 0 ;
                                    int pageLength = 10;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    ResultSet checkQuery = stmtCheckQuery.executeQuery(query);

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    if (checkQuery.next()) {
                                        String name = checkQuery.getString("USER");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Found commands for &e" + name + ""));
                                        stmtCheckQuery.close();
                                    } else {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &e" + args[1] + "&7 dosen't have used blocked commands. This user is clean!"));
                                        stmtCheckQuery.close();
                                        return;
                                    }

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + "1" + "&8]            "));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        String id = rs.getString("ID");
                                        if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e"
                                                    + name + "   "
                                                    + executed_command
                                                    + "    " + date) );
                                            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface")
                                                    + "/profile.php?id=" + id));
                                            message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( ChatColor.translateAlternateColorCodes('&', "&eHideMyPlugins &bWeb Profile") ).create()));
                                            player.sendMessage(message);

                                        } else {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));

                                        }
                                    }
                                } else {
                                    int pageNum = page;
                                    int pageLength = 5;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    ResultSet checkQuery = stmtCheckQuery.executeQuery(query);

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    if (checkQuery.next()) {
                                        String name = checkQuery.getString("USER");
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Found commands for &e" + name + ""));
                                        stmtCheckQuery.close();
                                    } else {
                                        if (args[2] == args[2]) {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Page &e" + args[2] + "&7 dosen't exist..." ));
                                            stmtCheckQuery.close();
                                            return;
                                        }
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &e" + args[1] + "&7 dosen't have used blocked commands. This user is clean!"));
                                        stmtCheckQuery.close();
                                        return;
                                    }

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + args[2] + "&8]            "));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        String id = rs.getString("ID");
                                        if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date) );
                                            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface") + "/profile.php?id=" + id));
                                            message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( ChatColor.translateAlternateColorCodes('&', "&eHideMyPlugins &bWeb Profile") ).create()));
                                            player.sendMessage(message);

                                        } else {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));

                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("noPerms") ));
                    }
                    return;
                }

            }

            player.sendMessage("§8              [§eHideMyPlugins §bBungee §cAdmin§8]");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
            player.sendMessage("§7 - §a/hmpa history <user> <page>§8-> §7Shows history of used.");
            player.sendMessage("§7 - §7blocked commands for that player.");
            player.sendMessage("§7 - §a/hmpa web §8-> §7Shows web interface commands.");
            player.sendMessage("");

            return;
        }
    }
}
