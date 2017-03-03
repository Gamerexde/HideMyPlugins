package com.gamerexde.HideMyPlugins;


import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import de.Herbystar.TTA.*;
import com.comphenix.protocol.reflect.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;


public class Main extends JavaPlugin implements Listener {
    FileConfiguration config;
    ProtocolManager protocolManager;
    
    
    public void onEnable() {
        this.config = this.getConfig();
        this.saveDefaultConfig();
        getCommand("HideMyPlugins").setExecutor(new HideMyPlugins());
        (this.protocolManager = ProtocolLibrary.getProtocolManager()).addPacketListener((PacketListener)new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    try {
                        final PacketContainer packet = event.getPacket();
                        
                        final String message = ((String)packet.getSpecificModifier((Class)String.class).read(0)).toLowerCase();
                        if (message.startsWith("") && !message.contains("  ")) {
                            event.setCancelled(true);
                        }
                    }
                    catch (FieldAccessException e) {
                    	Main.this.getLogger().log(Level.SEVERE, "Couldn't access field.", (Throwable)e);
                    }
                }
            }
        });        
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(ChatColor.YELLOW + "- HideMyPlugins -" + ChatColor.DARK_GRAY + "v1.1");
		console.sendMessage(ChatColor.GRAY + "Version:"  + (ChatColor.LIGHT_PURPLE + " V1.1"));
		console.sendMessage(ChatColor.GRAY + "Minecraft:"  + (ChatColor.LIGHT_PURPLE + " 1.8.8"));
		console.sendMessage(ChatColor.GRAY + "");
		console.sendMessage(ChatColor.GREEN + "El plugin a sido cargado sin ningun");
		console.sendMessage(ChatColor.GREEN + "problema!");
		
		
		for(Player player : Bukkit.getOnlinePlayers()){
			TTA_Methods.sendTitle(player, "§e- HideMyPlugins -", 10, 50, 20,
					"§7Cargado!", 10, 50, 20);
		}
		
    }
    
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)  {
        if(event.getMessage().equals("/?") && !event.getPlayer().hasPermission("hmp./?")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/me") && !event.getPlayer().hasPermission("hmp.me")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/ver") && !event.getPlayer().hasPermission("hmp.ver")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/ver *") && !event.getPlayer().hasPermission("hmp.ver*")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/version") && !event.getPlayer().hasPermission("hmp.version")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/pl") && !event.getPlayer().hasPermission("hmp.pl")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/plugman") && !event.getPlayer().hasPermission("hmp.plugman")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/about") && !event.getPlayer().hasPermission("hmp.about")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/help") && !event.getPlayer().hasPermission("hmp.help")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:help") && !event.getPlayer().hasPermission("hmp.bukkit:help")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:ver") && !event.getPlayer().hasPermission("hmp.bukkit:ver")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:kill") && !event.getPlayer().hasPermission("hmp.bukkit:kill")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/kill") && !event.getPlayer().hasPermission("hmp.kill")) {
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/op") && !event.getPlayer().hasPermission("hmp.op")) {
            event.setCancelled(true);
        }
    }


	public void onDisable() {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(ChatColor.YELLOW + "- HideMyPlugins -" + ChatColor.DARK_GRAY + "v1.1");
		console.sendMessage(ChatColor.GRAY + "Version:"  + (ChatColor.LIGHT_PURPLE + " V1.1"));
		console.sendMessage(ChatColor.GRAY + "Minecraft:"  + (ChatColor.LIGHT_PURPLE + " 1.8.8"));
		console.sendMessage(ChatColor.GRAY + "");
		console.sendMessage(ChatColor.RED + "El plugin a sido apagado sin ningun");
		console.sendMessage(ChatColor.RED + "problema!");
		for(Player player : Bukkit.getOnlinePlayers()){
			TTA_Methods.sendTitle(player, "§e- HideMyPlugins -", 10, 50, 20,
					"§cApagando...", 10, 50, 20);
		}

    	
    }
	
	
	public void onLoad(){
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		console.sendMessage(ChatColor.YELLOW + "- HideMyPlugins -" + ChatColor.DARK_GRAY + "v1.1");
		console.sendMessage(ChatColor.GRAY + "Cargando Porfavor espere...");
		for(Player player : Bukkit.getOnlinePlayers()){
			TTA_Methods.sendTitle(player, "§e- HideMyPlugins -", 10, 50, 20,
					"§aPlugin detectado, iniciando...", 10, 50, 20);
		}
		
	}
	
}

	
	        

