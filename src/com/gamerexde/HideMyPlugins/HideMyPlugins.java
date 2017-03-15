package com.gamerexde.HideMyPlugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.gamerexde.HideMyPlugins.HideMyPlugins;

import de.Herbystar.TTA.TTA_Methods;

public class HideMyPlugins implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, 
			String[] args) {
		if (label.equalsIgnoreCase("HideMyPlugins")) {
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("reload")){ 
			    	  if (sender.hasPermission("hmp.reload")) {
			    		  sender.sendMessage("§8[§eHideMyPlugins§8] §7Configuracion Recargada.");
			    		  this.reloadConfig();
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§eHideMyPlugins§8] §cNo tienes permisos!");
			    	  }
			        return false; 
			      }
			    }
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("version")){ 
			    	  if (sender.hasPermission("hmp.PLVersion")) {
			    		  sender.sendMessage("§8[§eHideMyPlugins§8] §6V1.2");
			    		  this.reloadConfig();
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§eHideMyPlugins§8] §cNo tienes permisos!");
			    	  }
			        return false; 
			      }
			    }
		    
		    
			if (!(sender instanceof Player)) {
				
				sender.sendMessage("(!) No eres un jugador.");
				return false;
			}
			
			Player player = (Player) sender;
			player.sendMessage("§8[§eHideMyPlugins§8]");
			player.sendMessage("     §8[§e1.2§8]     ");
			player.sendMessage("");
			player.sendMessage("§e/hidemyplugins Reload §8-> §eRecarga la Config.yml");
			player.sendMessage("§e/hidemyplugins Version §8-> §eMuestra la version actual.");
			
			TTA_Methods.sendTitle(player, "§e- HideMyPlugins -", 10, 50, 10,"§8[§e1.2§8]" , 10, 50, 10);
			
		    return true;
		}

		return false;
	}

	private void reloadConfig() {
	}



}
