package com.gamerexde.HideMyPlugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.Herbystar.TTA.TTA_Methods;

public class HideMyPlugins implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, 
			String[] args) {
		if (label.equalsIgnoreCase("HideMyPlugins")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("(!) No eres un jugador.");
				return false;
			}
			
			Player player = (Player) sender;
			player.sendMessage("§8[§eHideMyPlugins§8]");
			player.sendMessage("     §8[§e1.0§8]     ");
			player.sendMessage("");
			player.sendMessage("§e/hmpReload §8-> §eRecarga la Config.yml");
			
			TTA_Methods.sendTitle(player, "§e- HideMyPlugins -", 10, 50, 10,"§8[§e1.0§8]" , 10, 50, 10);
			
		    return true;
		}
  

		return false;
	}

}