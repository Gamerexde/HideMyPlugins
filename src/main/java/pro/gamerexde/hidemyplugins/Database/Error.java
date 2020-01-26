package pro.gamerexde.hidemyplugins.Database;

import pro.gamerexde.hidemyplugins.HideMyPlugins;

import java.util.logging.Level;

public class Error {
    public static void execute(HideMyPlugins plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(HideMyPlugins plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}