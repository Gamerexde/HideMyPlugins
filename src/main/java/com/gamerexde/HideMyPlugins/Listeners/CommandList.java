package com.gamerexde.HideMyPlugins.Listeners;

import com.gamerexde.HideMyPlugins.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements Listener {
    @EventHandler
    public void onCommandSend(final PlayerCommandSendEvent event) {
        if (Main.getInstance().getConfig().getBoolean("blockTabCompletions")) {
            final List<String> commands = new ArrayList<String>(event.getCommands());
            for (final String command : commands) {
            }
        }
    }

}
