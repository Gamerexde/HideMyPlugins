package com.gamerexde.hidemyplugins.hidemyplugins.Events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.gamerexde.hidemyplugins.*;

import de.Herbystar.TTA.TTA_Methods;

public class PluginListener implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("HideMyPlugins")) {
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("hmp.reload")) {
                        sender.sendMessage("§2§lHideMyPlugins> §7Configuration reloaded.");
                        this.reloadConfig();
                    }
                    else {
                        sender.sendMessage("§2§lHideMyPlugins> §7Access Denied.");
                    }
                    return false;
                }
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) No eres un jugador.");
                return false;
            }

            Player player = (Player) sender;

            player.sendMessage("§8                       [§dHideMyPlugins§8]");
            player.sendMessage("                             §8[§d2.0§8]     ");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hidemyplugins reload §8-> §7Reload the Config.yml");
            player.sendMessage("§7 - §a/hidemyplugins version §8-> §7Show actual version.");
            player.sendMessage("§7 - §a/hidemyplugins config §8-> §7Show actual version of config file.");
            player.sendMessage("§8                 ------[§c Soon §8]-------------");
            player.sendMessage("§7 - §a/hidemyplugins historial §8-> §7Show users that have used cmd.");
            player.sendMessage("§7 - §a/hidemyplugins kicked §8-> §7Show users kick by using ilegal cmd.");

            TTA_Methods.sendTitle(player, "§7- §2§lHideMyPlugins §7-", 10, 50, 10,"§8-[ §7NexoMC Edition §8]-" , 10, 50, 10);

            return true;
        }

        return false;
    }

    private void reloadConfig() {
        // Deprecated
    }



}
