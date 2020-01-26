package pro.gamerexde.hidemyplugins.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pro.gamerexde.hidemyplugins.HideMyPlugins;

import java.sql.*;

public class MySQL {
    private Connection connection;
    public String host, database, username, password;
    public int port;

    HideMyPlugins plugin;

    public MySQL(HideMyPlugins instance) {
        plugin = instance;
    }

    public static final String USER_ERROR = "&e&lHideMyPlugins> &7Error while creating tables, verify user permissions and then try again...";
    public static final String CONNECTION_USER_ERROR = "&e&lHideMyPlugins> &7The connection cannot be established due to a misconfiguration in the config.yml or the MySQL Server configuration. Fix the error and try again.";
    public static final String INTERNAL_ERROR = "&e&lHideMyPlugins> &7Internal Error. Please post this issue in &chttps://github.com/Gamerexde/HideMyPlugins/issues &7. This is a fatal error.";


    public void initMySQLDatabase() {
        host = plugin.getConfig().getString("MySQL.host");
        port = plugin.getConfig().getInt("MySQL.port");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.user");
        password = plugin.getConfig().getString("MySQL.password");
        try {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Trying to connect to MySQL server &e" + host + ":" + port + ""));
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                String dbDriver = plugin.getConfig().getString("database-driver");
                String dbClass = plugin.getConfig().getString("database-class");

                Class.forName(dbClass);
                setConnection(DriverManager.getConnection(dbDriver + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7The MySQL connection to the server &e" + host + ":" + port + "&7 has been successfully established!"));
                createTable();
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', CONNECTION_USER_ERROR));
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            Bukkit.shutdown();
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', INTERNAL_ERROR));
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Server will shutdown for security reasons..."));
            Bukkit.shutdown();
        }
    }


    public Connection getConnection() {
        return connection;
    }


    public void createTable() {
        try {
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("MySQL.table_name") + " ("
                    + "ID VARCHAR(45) NOT NULL,"
                    + "USER VARCHAR(45) NOT NULL,"
                    + "UUID VARCHAR(45) NOT NULL,"
                    + "EXECUTED_COMMAND VARCHAR(45) NOT NULL,"
                    + "DATE VARCHAR(45) NOT NULL,"
                    + "PRIMARY KEY (ID))");
            create.executeUpdate();
        }catch(SQLException e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', USER_ERROR));
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
