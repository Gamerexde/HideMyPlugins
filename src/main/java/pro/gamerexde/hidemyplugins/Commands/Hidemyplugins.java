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
            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) You need to be a player in order to execute that command.");
                return false;
            }
            Player player = (Player) sender;

            if (plugin.getConfig().getBoolean("stealth-mode.enabled")) {
                if (!player.hasPermission("hidemyplugins.access")) {
                    player.sendMessage(plugin.getConfig().getString("stealth-mode.command-not-found"));
                    return false;
                }
            }

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("hidemyplugins.reload")) {
                        plugin.reloadConfig();
                        plugin.reloadMsgConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("admin.reload")));
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMsgConfig().getString("noPerms")));
                    }
                    return false;
                }
            }
            if(args.length == 1) {
            }

            player.sendMessage("§8                       [§dHideMyPlugins§8]");
            player.sendMessage("                      §d" + plugin.version +"");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hidemyplugins reload §8-> §7Reload the configuration.");
            player.sendMessage("§7 - §a/hmpa §8-> §7Shows useful admin commands.");
            player.sendMessage("");

            return true;
        }

        return false;
    }

}
