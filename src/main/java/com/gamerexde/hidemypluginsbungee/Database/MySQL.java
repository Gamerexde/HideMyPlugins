package com.gamerexde.hidemypluginsbungee.Database;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL extends Database{
    private Connection connection;
    public String host, database, username, password;
    public int port;

    HideMyPluginsBungee plugin;

    public MySQL(HideMyPluginsBungee instance) {
        super(instance);
        plugin = instance;
    }

    @Override
    public void load() {
        initMySQLDatabase();
    }

    public void reconnect() {
        reconnectSQL();
    }

    public static final String USER_ERROR = "&d&lHideMyPlugins> &7Error while creating tables, verify user permissions and then try again...";
    public static final String CONNECTION_USER_ERROR = "&d&lHideMyPlugins> &7The connection cannot be established due to a misconfiguration in the config.yml or the MySQL Server configuration. Fix the error and try again.";
    public static final String INTERNAL_ERROR = "&d&lHideMyPlugins> &7Internal Error. Please post this issue in &chttps://github.com/Gamerexde/HideMyPlugins/issues &7. This is a fatal error.";


    public void initMySQLDatabase() {
        host = plugin.getConfig().getString("MySQL.host");
        port = plugin.getConfig().getInt("MySQL.port");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.user");
        password = plugin.getConfig().getString("MySQL.password");
        try {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Trying to connect to MySQL server &d" + host + ":" + port + ""));
            synchronized (this) {
                if (getSQLConnection() != null && !getSQLConnection().isClosed()) {
                    return;
                }
                String dbDriver = plugin.getConfig().getString("database-driver");
                String dbClass = plugin.getConfig().getString("database-class");

                Class.forName(dbClass);
                setConnection(DriverManager.getConnection(dbDriver + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));

                plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7The MySQL connection to the server &d" + host + ":" + port + "&7 has been successfully established!"));
                createTable();
            }
        } catch (SQLException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', CONNECTION_USER_ERROR));
            e.printStackTrace();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            ProxyServer.getInstance().stop();
        } catch (ClassNotFoundException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', INTERNAL_ERROR));
            e.printStackTrace();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            ProxyServer.getInstance().stop();
        }
    }


    public Connection getSQLConnection() {
        return connection;
    }

    public void reconnectSQL() {
        host = plugin.getConfig().getString("MySQL.host");
        port = plugin.getConfig().getInt("MySQL.port");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.user");
        password = plugin.getConfig().getString("MySQL.password");
        try {
            synchronized (this) {
                if (getSQLConnection() != null && !getSQLConnection().isClosed()) {
                    return;
                }
                String dbDriver = plugin.getConfig().getString("database-driver");
                String dbClass = plugin.getConfig().getString("database-class");

                Class.forName(dbClass);
                setConnection(DriverManager.getConnection(dbDriver + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));
            }
        } catch (SQLException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', CONNECTION_USER_ERROR));
            e.printStackTrace();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            ProxyServer.getInstance().stop();
        } catch (ClassNotFoundException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', INTERNAL_ERROR));
            e.printStackTrace();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            ProxyServer.getInstance().stop();
        }
    }


    public void createTable() {
        try {
            Connection con = getSQLConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("MySQL.table_name") + " ("
                    + "ID VARCHAR(45) NOT NULL,"
                    + "USER VARCHAR(45) NOT NULL,"
                    + "UUID VARCHAR(45) NOT NULL,"
                    + "EXECUTED_COMMAND VARCHAR(45) NOT NULL,"
                    + "DATE VARCHAR(45) NOT NULL,"
                    + "PRIMARY KEY (ID))");
            create.executeUpdate();
        }catch(SQLException e){
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', USER_ERROR));
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
