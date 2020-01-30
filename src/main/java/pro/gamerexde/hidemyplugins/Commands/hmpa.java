package pro.gamerexde.hidemyplugins.Commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pro.gamerexde.hidemyplugins.Database.Database;
import pro.gamerexde.hidemyplugins.HideMyPlugins;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class hmpa implements CommandExecutor {
    HideMyPlugins plugin;
    public hmpa(HideMyPlugins instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();

        if (label.equalsIgnoreCase("hmpa")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("history")) {
                    if (sender.hasPermission("hidemyplugins.admin.history")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Usage: &e/hmpa history <user> <page>"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgconfig.getString("noPerms") ));
                    }
                    return false;
                }
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase(args[1])) {
                    if (sender.hasPermission("hidemyplugins.admin.history")) {
                        try {
                            if (plugin.getConfig().getBoolean("use-mysql")) {
                                Connection con = plugin.getMySQL().getConnection();

                                Statement stmt = con.createStatement();
                                int pageNum = 0 ;
                                int pageLength = 10;

                                String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                        + " WHERE `USER` LIKE '"
                                        + args[1]
                                        + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                ResultSet rs = stmt.executeQuery(query);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for " + args[1] + ""));

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + "1" + "&8]            "));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                while (rs.next()) {
                                    String name = rs.getString("USER");
                                    String date = rs.getString("DATE");
                                    String executed_command = rs.getString("EXECUTED_COMMAND");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                }

                            } else if (plugin.getConfig().getBoolean("use-sqlite")) {
                                Database con = plugin.getRDatabase();

                                Statement stmt = con.getSQLConnection().createStatement();
                                int pageNum = 0 ;
                                int pageLength = 10;

                                String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                        + " WHERE `USER` LIKE '"
                                        + args[1]
                                        + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                ResultSet rs = stmt.executeQuery(query);
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + "1" + "&8]            "));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                while (rs.next()) {
                                    String name = rs.getString("USER");
                                    String date = rs.getString("DATE");
                                    String executed_command = rs.getString("EXECUTED_COMMAND");
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgconfig.getString("noPerms")));
                    }
                    return false;
                }

            }
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase(args[2])) {
                    if (sender.hasPermission("hidemyplugins.admin.history")) {
                        try {
                            if (plugin.getConfig().getBoolean("use-mysql")) {
                                Connection con = plugin.getMySQL().getConnection();

                                Statement stmt = con.createStatement();

                                int page = Integer.parseInt(args[2]);

                                if (page < 2) {
                                    int pageNum = 0 ;
                                    int pageLength = 10;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + "1" + "&8]            "));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                    }
                                } else {
                                    int pageNum = page;
                                    int pageLength = 5;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("MySQL.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + args[2] + "&8]            "));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                    }
                                }

                            } else if (plugin.getConfig().getBoolean("use-sqlite")){
                                Database con = plugin.getRDatabase();

                                Statement stmt = con.getSQLConnection().createStatement();
                                int page = Integer.parseInt(args[2]);

                                if (page < 2) {
                                    int pageNum = 0 ;
                                    int pageLength = 10;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + args[2] + "&8]            "));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                    }
                                } else {
                                    int pageNum = page;
                                    int pageLength = 5;

                                    String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                            + " WHERE `USER` LIKE '"
                                            + args[1]
                                            + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                    ResultSet rs = stmt.executeQuery(query);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Searching history for &e" + args[1] + ""));

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7           &8[&7Page: &e" + args[2] + "&8]            "));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7User     Command        Date"));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8----------------------------"));

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + name + "   " + executed_command + "    " + date));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgconfig.getString("noPerms") ));
                    }
                    return false;
                }

            }
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("(!) You need to be a player in order to execute that command.");
            return false;
        }

        Player player = (Player) sender;

        player.sendMessage("§8                     [§eHideMyPlugins §cAdmin§8]");
        player.sendMessage("                      §e" + plugin.version +"");
        player.sendMessage("");
        player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
        player.sendMessage("§7 - §a/hmpa history <user> <page>§8-> §7Shows history of used.");
        player.sendMessage("§7 - §7blocked commands for that player.");
        player.sendMessage("");

        return true;
    }

}
