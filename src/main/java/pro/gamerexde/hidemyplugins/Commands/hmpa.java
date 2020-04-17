package pro.gamerexde.hidemyplugins.Commands;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pro.gamerexde.hidemyplugins.HideMyPlugins;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class hmpa implements CommandExecutor {
    HideMyPlugins plugin;
    public hmpa(HideMyPlugins instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String noPerms = plugin.getMsgConfig().getString("noPerms");

        String historyUsageCommand = plugin.getMsgConfig().getString("commands.history_usage");

        String webInterfaceCommand = plugin.getMsgConfig().getString("admin.web_interface_command");
        String webInterfaceCommandNoPerm = plugin.getMsgConfig().getString("admin.web_interface_command_no_enabled");

        webInterfaceCommand = webInterfaceCommand.replace("{WEB}", Objects.requireNonNull(plugin.getConfig().getString("WebInterface")));

        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();

        if (label.equalsIgnoreCase("hmpa")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) You need to be a player in order to execute that command.");
                return false;
            }
            Player player = (Player) sender;

            if (plugin.getConfig().getBoolean("stealth-mode.enabled")) {
                if (!player.hasPermission("hidemyplugins.access")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stealth-mode.command-not-found")));
                    return false;
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("web")) {
                    if (sender.hasPermission("hidemyplugins.admin.webinterface")) {
                        if (plugin.getConfig().getBoolean("UsingWebInterface")){

                            sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', webInterfaceCommand));
                        } else {
                            sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', webInterfaceCommandNoPerm));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerms ));
                    }
                    return false;
                }
                if (args[0].equalsIgnoreCase("history")) {
                    if (sender.hasPermission("hidemyplugins.admin.history")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', historyUsageCommand));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerms));
                    }
                    return false;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("history")) {
                    if (args[1].equalsIgnoreCase(args[1])) {
                        if (sender.hasPermission("hidemyplugins.admin.history")) {
                            Connection con = null;
                            Statement stmt = null;
                            Statement stmtQueryCheck = null;
                            ResultSet rs = null;
                            ResultSet checkQuery = null;

                            try {
                                con = plugin.getRDatabase().getSQLConnection();

                                stmt = con.createStatement();
                                stmtQueryCheck = con.createStatement();

                                int pageNum = 0 ;
                                int pageLength = 10;

                                String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                        + " WHERE `USER` LIKE '"
                                        + args[1]
                                        + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                rs = stmt.executeQuery(query);
                                checkQuery = stmtQueryCheck.executeQuery(query);

                                String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                if (checkQuery.next()) {
                                    String name = checkQuery.getString("USER");

                                    String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                    adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                    stmtQueryCheck.close();
                                } else {
                                    String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                    adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                    stmtQueryCheck.close();
                                    return false;
                                }

                                List<String> header = HideMyPlugins.getInstance().getAdminTableHeaderArray();

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                for(String output: header) {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", "1")));
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
                                        message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getMsgConfig().getString("admin.history_result_hover")))).create()));
                                        player.spigot().sendMessage(message);

                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (con != null)
                                        con.close();
                                    if (rs != null)
                                        rs.close();
                                    if (checkQuery != null)
                                        checkQuery.close();
                                    if (stmt != null)
                                        stmt.close();
                                    if (stmtQueryCheck != null)
                                        stmtQueryCheck.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgconfig.getString("noPerms")));
                        }
                    }
                    return false;
                }

            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("history")) {
                    if (args[1].equalsIgnoreCase(args[1])) {
                        if (args[2].equalsIgnoreCase(args[2])) {
                            if (sender.hasPermission("hidemyplugins.admin.history")) {
                                try {
                                    Integer.parseInt(args[2]);
                                }catch(NumberFormatException e){
                                    String pageNotInt = plugin.getMsgConfig().getString("admin.history_page_not_number");
                                    pageNotInt = pageNotInt.replace("{PAGE}", args[2]);

                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', pageNotInt));

                                    return true;
                                }
                                Connection con = null;
                                Statement stmt = null;
                                Statement stmtCheckQuery = null;
                                ResultSet rs = null;
                                ResultSet checkQuery = null;
                                try {
                                    con = plugin.getRDatabase().getSQLConnection();

                                    stmt = con.createStatement();
                                    stmtCheckQuery = con.createStatement();

                                    int page = Integer.parseInt(args[2]);

                                    if (page < 2) {
                                        int pageNum = 0 ;
                                        int pageLength = 10;

                                        String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                                + " WHERE `USER` LIKE '"
                                                + args[1]
                                                + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                        rs = stmt.executeQuery(query);
                                        checkQuery = stmtCheckQuery.executeQuery(query);

                                        String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                        adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                        if (checkQuery.next()) {
                                            String name = checkQuery.getString("USER");

                                            String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                            adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                            checkQuery.close();
                                        } else {
                                            if (args[2] == args[2]) {
                                                String adminHistoryPageNotFound = plugin.getMsgConfig().getString("admin.history_page_not_found");
                                                adminHistoryPageNotFound = adminHistoryPageNotFound.replace("{PAGE}", args[2]);

                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryPageNotFound));
                                                checkQuery.close();
                                                return false;
                                            }
                                            String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                            adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                            checkQuery.close();
                                            return false;
                                        }

                                        List<String> header = HideMyPlugins.getInstance().getAdminTableHeaderArray();

                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                        for(String output: header) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", args[2])));
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
                                                player.spigot().sendMessage(message);

                                            } else {
                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

                                            }
                                        }
                                    } else {
                                        int pageNum = page;
                                        int pageLength = 5;

                                        String query = String.format("SELECT * FROM " + plugin.getConfig().getString("SQLite.table_name")
                                                + " WHERE `USER` LIKE '"
                                                + args[1]
                                                + "' ORDER BY `DATE` ASC LIMIT %d, %d", pageNum * pageLength, pageLength);

                                        rs = stmt.executeQuery(query);
                                        checkQuery = stmtCheckQuery.executeQuery(query);

                                        String adminHistorySearching = plugin.getMsgConfig().getString("admin.history_searching");
                                        adminHistorySearching = adminHistorySearching.replace("{USER}", args[1]);

                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistorySearching));

                                        if (checkQuery.next()) {
                                            String name = checkQuery.getString("USER");

                                            String adminHistoryFound = plugin.getMsgConfig().getString("admin.history_found");
                                            adminHistoryFound = adminHistoryFound.replace("{USER}", name);

                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryFound));
                                            checkQuery.close();
                                        } else {
                                            if (args[2] == args[2]) {
                                                String adminHistoryPageNotFound = plugin.getMsgConfig().getString("admin.history_page_not_found");
                                                adminHistoryPageNotFound = adminHistoryPageNotFound.replace("{PAGE}", args[2]);

                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryPageNotFound));
                                                checkQuery.close();
                                                return false;
                                            }
                                            String adminHistoryNotFound = plugin.getMsgConfig().getString("admin.history_not_found");
                                            adminHistoryNotFound = adminHistoryNotFound.replace("{USER}", args[1]);

                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', adminHistoryNotFound));
                                            checkQuery.close();
                                            return false;
                                        }

                                        List<String> header = HideMyPlugins.getInstance().getAdminTableHeaderArray();

                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n"));
                                        for(String output: header) {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', output.replace("{PAGE}", args[2])));
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
                                                player.spigot().sendMessage(message);

                                            } else {
                                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', historyResult));

                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (rs != null)
                                            rs.close();
                                        if (con != null)
                                            con.close();
                                        if (checkQuery != null)
                                            checkQuery.close();
                                        if (stmt != null)
                                            stmt.close();
                                        if (stmtCheckQuery != null)
                                            stmtCheckQuery.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msgconfig.getString("noPerms") ));
                            }
                        }
                    }
                    return false;
                }

            }
        }

        Player player = (Player) sender;

        player.sendMessage("§8                     [§dHideMyPlugins §cAdmin§8]");
        player.sendMessage("                      §d" + plugin.version +"");
        player.sendMessage("");
        player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
        player.sendMessage("§7 - §a/hmpa history <user> <page>§8-> §7Shows history of used.");
        player.sendMessage("§7 - §7blocked commands for that player.");
        player.sendMessage("§7 - §a/hmpa web §8-> §7Shows web interface commands.");
        player.sendMessage("");

        return true;
    }

}
