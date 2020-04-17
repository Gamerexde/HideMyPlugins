package com.gamerexde.hidemypluginsbungee.Commands;

import com.gamerexde.hidemypluginsbungee.HideMyPluginsBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HideMyPlugins extends Command {
    HideMyPluginsBungee plugin;
    public HideMyPlugins(HideMyPluginsBungee instance) {
        super("hidemyplugins");
        plugin = instance;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (plugin.getConfig().getBoolean("stealth-mode.enabled")) {
                if (!player.hasPermission("hidemyplugins.access")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stealth-mode.command-not-found")));
                    return;
                }
            }

            if (args.length == 1) {
            }
            player.sendMessage("§8                     [§dHideMyPlugins §bBungee§8]");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
            player.sendMessage("");

        }
    }
}
