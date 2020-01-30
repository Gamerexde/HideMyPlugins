package com.gamerexde.hidemypluginsbungee.Database;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;

import java.util.logging.Level;

public class Error {
    public static void execute(HideMyPluginsBungee plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(HideMyPluginsBungee plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}