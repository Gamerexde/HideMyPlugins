package pro.gamerexde.hidemyplugins.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pro.gamerexde.hidemyplugins.HideMyPlugins;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database{
    String dbname;
    public SQLite(HideMyPlugins instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.filename", "database");
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname +".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Connecting to SQLite Database..."));
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("SQLite.table_name") + "("
                    + "ID VARCHAR(45) NOT NULL,"
                    + "USER VARCHAR(45) NOT NULL,"
                    + "UUID VARCHAR(45) NOT NULL,"
                    + "EXECUTED_COMMAND VARCHAR(45) NOT NULL,"
                    + "DATE VARCHAR(45) NOT NULL,"
                    + "PRIMARY KEY (ID));");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lHideMyPlugins> &7Connected to SQLite successfully!"));
        initialize();
    }
}