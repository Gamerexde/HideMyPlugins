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
import java.util.List;
import java.util.Objects;

public class hmpa extends Command {

    private HideMyPluginsBungee plugin;
    public hmpa(HideMyPluginsBungee instance) {
        super("hmpa");
        plugin = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String noPerms = plugin.getMsgConfig().getString("noPerms");

        String historyUsageCommand = plugin.getMsgConfig().getString("commands.history_usage");

        String webInterfaceCommand = plugin.getMsgConfig().getString("admin.web_interface_command");
        String webInterfaceCommandNoPerm = plugin.getMsgConfig().getString("admin.web_interface_command_no_enabled");

        webInterfaceCommand = webInterfaceCommand.replace("{WEB}", Objects.requireNonNull(plugin.getConfig().getString("WebInterface")));

        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("web")) {
                    if (player.hasPermission("hidemyplugins.admin.webinterface")) {
                        if (plugin.getConfig().getBoolean("UsingWebInterface")){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', webInterfaceCommand));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', webInterfaceCommandNoPerm));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerms));
                        return;
                    }
                }
                if (args[0].equalsIgnoreCase("history")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyUsageCommand));
                }
                return;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("history")) {
                    if(args[1].equalsIgnoreCase(args[1])) {
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

                                    String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                    adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                    if (checkQuery.next()) {
                                        String name = checkQuery.getString("USER");

                                        String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                        adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                        checkQuery.close();
                                    } else {
                                        String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                        adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                        checkQuery.close();
                                        return;
                                    }

                                    List<String> header = plugin.getAdminTableHeaderArray();

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                    for(String output: header) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", "1")));
                                    }

                                    while (rs.next()) {
                                        String name = rs.getString("USER");
                                        String date = rs.getString("DATE");
                                        String executed_command = rs.getString("EXECUTED_COMMAND");
                                        String id = rs.getString("ID");

                                        String historyResult = plugin.getMsgConfig().getString("admin.history_result");
                                        historyResult = historyResult.replace("{USER}", name);
                                        historyResult = historyResult.replace("{DATE}", date);
                                        historyResult = historyResult.replace("{COMMAND}", executed_command);

                                        if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                            TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', historyResult));
                                            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface")
                                                    + "/profile.php?id=" + id));
                                            message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getMsgConfig().getString("admin.history_result_hover")))).create()));
                                            player.sendMessage(message);
                                        } else {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerms));
                        }
                    }
                    return;
                }

            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("history")) {
                    if (args[1].equalsIgnoreCase(args[1])) {
                        if (args[2].equalsIgnoreCase(args[2])) {
                            if (player.hasPermission("hidemyplugins.admin.history")) {
                                try {
                                    Integer.parseInt(args[2]);
                                }catch(NumberFormatException e){
                                    String pageNotInt = plugin.getMsgConfig().getString("admin.history_page_not_number");
                                    pageNotInt = pageNotInt.replace("{PAGE}", args[2]);

                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pageNotInt));

                                    return;
                                }

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

                                            String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                            adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                            if (checkQuery.next()) {
                                                String name = checkQuery.getString("USER");

                                                String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                                adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                                checkQuery.close();
                                            } else {
                                                String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                                adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                                checkQuery.close();
                                                return;
                                            }

                                            List<String> header = plugin.getAdminTableHeaderArray();

                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                            for(String output: header) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", "1")));
                                            }

                                            while (rs.next()) {
                                                String name = rs.getString("USER");
                                                String date = rs.getString("DATE");
                                                String executed_command = rs.getString("EXECUTED_COMMAND");
                                                String id = rs.getString("ID");

                                                String historyResult = plugin.getMsgConfig().getString("admin.history_result");
                                                historyResult = historyResult.replace("{USER}", name);
                                                historyResult = historyResult.replace("{DATE}", date);
                                                historyResult = historyResult.replace("{COMMAND}", executed_command);

                                                if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                                    TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', historyResult));
                                                    message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface")
                                                            + "/profile.php?id=" + id));
                                                    message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getMsgConfig().getString("admin.history_result_hover")))).create()));
                                                    player.sendMessage(message);
                                                } else {
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

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

                                            String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                            adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                            if (checkQuery.next()) {
                                                String name = checkQuery.getString("USER");

                                                String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                                adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                                checkQuery.close();
                                            } else {
                                                if (args[2] == args[2]) {
                                                    String adminHistoryPageNotFound = plugin.getMsgConfig().getString("admin.history_page_not_found");
                                                    adminHistoryPageNotFound = adminHistoryPageNotFound.replace("{PAGE}", args[2]);

                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryPageNotFound));
                                                    checkQuery.close();
                                                    return;
                                                }
                                                String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                                adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                                checkQuery.close();
                                                return;
                                            }

                                            List<String> header = plugin.getAdminTableHeaderArray();

                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                            for(String output: header) {
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", args[2])));
                                            }

                                            while (rs.next()) {
                                                String name = rs.getString("USER");
                                                String date = rs.getString("DATE");
                                                String executed_command = rs.getString("EXECUTED_COMMAND");
                                                String id = rs.getString("ID");

                                                String historyResult = plugin.getMsgConfig().getString("admin.history_result");
                                                historyResult = historyResult.replace("{USER}", name);
                                                historyResult = historyResult.replace("{DATE}", date);
                                                historyResult = historyResult.replace("{COMMAND}", executed_command);

                                                if (plugin.getConfig().getBoolean("UsingWebInterface")){
                                                    TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', historyResult));
                                                    message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, plugin.getConfig().getString("WebInterface")
                                                            + "/profile.php?id=" + id));
                                                    message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getMsgConfig().getString("admin.history_result_hover")))).create()));
                                                    player.sendMessage(message);

                                                } else {
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerms));
                            }
                        }
                    }
                    return;
                }

            }

            player.sendMessage("§8              [§dHideMyPlugins §bBungee §cAdmin§8]");
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
