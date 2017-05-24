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
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Configuration reloaded.");
			    		  this.reloadConfig();
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Access Denied.");
			    	  }
			        return false; 
			      }
			    }
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("version")){ 
			    	  if (sender.hasPermission("hmp.PLVersion")) {
			    		  sender.sendMessage("§8[§eHideMyPlugins§8] §72.0");
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Access Denied.");
			    	  }
			        return false; 
			      }
			    }
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("config")){ 
			    	  if (sender.hasPermission("hmp.PLConfig")) {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Config version its: §d1.0");
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Access Denied.");
			    	  }
			        return false; 
			      }
			    }	
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("historial")){ 
			    	  if (sender.hasPermission("hmp.historial")) {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Soon, Stay tuned on my website!");
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §ahttps://gamerexde-designs.ga/");
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Access Denied.");
			    	  }
			        return false; 
			      }
			    }	
		    if(args.length == 1){ 
			      if(args[0].equalsIgnoreCase("kicked")){ 
			    	  if (sender.hasPermission("hmp.kicked")) {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Soon, Stay tuned on my website!");
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §ahttps://gamerexde-designs.ga/");
			    	  }
			    	  else {
			    		  sender.sendMessage("§8[§dHideMyPlugins§8] §7Access Denied.");
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
			
			TTA_Methods.sendTitle(player, "§7- §d§lHideMyPlugins §7-", 10, 50, 10,"§8-[ §d2.0 §8]-" , 10, 50, 10);
			
		    return true;
		}

		return false;
	}

	private void reloadConfig() {
		// Deprecated
	}



}
