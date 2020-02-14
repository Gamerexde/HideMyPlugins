package com.gamerexde.hidemypluginsbungee.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HideMyPlugins extends Command {

    public HideMyPlugins() {
        super("hidemyplugins");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (args.length == 1) {
            }
            player.sendMessage("§8                     [§dHideMyPlugins §bBungee§8]");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
            player.sendMessage("");

        }
    }
}
