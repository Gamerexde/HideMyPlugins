package pro.gamerexde.hidemyplugins.Listeners;

import pro.gamerexde.hidemyplugins.HideMyPlugins;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements Listener {
    @EventHandler
    public void onCommandSend(final PlayerCommandSendEvent event) {
        if (HideMyPlugins.getInstance().getConfig().getBoolean("blockCommands")) {
            final List<String> commands = new ArrayList<String>(event.getCommands());
            for (final String command : commands) {
            }
        }
    }

}
