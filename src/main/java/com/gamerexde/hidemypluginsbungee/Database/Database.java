package com.gamerexde.hidemypluginsbungee.Database;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;


public abstract class Database {
    static HideMyPluginsBungee plugin;
    Connection connection;

    public Database(HideMyPluginsBungee instance) {
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public Connection initialize() {
        connection = getSQLConnection();
        return connection;
    }

    public PreparedStatement executeCommand(String id, String uuid, String name, String executedCommand, String date) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + plugin.getConfig().getString("SQLite.table_name") + "(ID,UUID,USER,EXECUTED_COMMAND,DATE) VALUES (?,?,?,?,?)");
            ps.setString(1, id);
            ps.setString(2, uuid);
            ps.setString(3, name);
            ps.setString(4, executedCommand);
            ps.setString(5, date);

            ps.executeUpdate();
            return ps;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return ps;
    }

    public PreparedStatement executeCustomCommand(String command) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(command);

            ps.executeUpdate();
            return ps;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return ps;
    }


    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }

}
