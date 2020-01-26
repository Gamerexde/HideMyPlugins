package pro.gamerexde.hidemyplugins.Commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pro.gamerexde.hidemyplugins.HideMyPlugins;

public class Hidemyplugins implements CommandExecutor {
    HideMyPlugins plugin;
    public Hidemyplugins(HideMyPlugins instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("HideMyPlugins")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("hidemyplugins.reload")) {
                        plugin.reloadConfig();
                        plugin.reloadMsgConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Configuration reloaded!"));
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You can't reload the plugin because you don't have permissions..."));
                    }
                    return false;
                }
            }
            if(args.length == 1) {
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) You need to be a player in order to execute that command.");
                return false;
            }

            Player player = (Player) sender;

            player.sendMessage("§8                       [§eHideMyPlugins§8]");
            player.sendMessage("                      §e" + plugin.version +"");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hidemyplugins reload §8-> §7Reload the configuration.");
            player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
            player.sendMessage("");

            return true;
        }

        return false;
    }

}
