package com.gamerexde.hidemyplugins.hidemyplugins;

import java.util.logging.Level;

import com.gamerexde.hidemyplugins.hidemyplugins.Events.PluginListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import de.Herbystar.TTA.*;
import com.comphenix.protocol.reflect.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;


public class HideMyPlugins extends JavaPlugin implements Listener {
    FileConfiguration config;
    ProtocolManager protocolManager;



    public void onEnable() {
        this.config = this.getConfig();
        this.saveDefaultConfig();
        getCommand("HideMyPlugins").setExecutor(new PluginListener());
        // Primary Method
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
                        HideMyPlugins.this.getLogger().log(Level.SEVERE, "Couldn't access field.", (Throwable)e);
                    }
                }
            }
        });

        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.DARK_GREEN + "- HideMyPlugins -" + ChatColor.DARK_GRAY + "v2.0");
        console.sendMessage(ChatColor.GREEN + "Plugin Succesfully Loaded and Enabled!");


    }


    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)  {
        Player player = event.getPlayer();
        if(event.getMessage().equals("/?") && !event.getPlayer().hasPermission("hmp./?")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/me") && !event.getPlayer().hasPermission("hmp.me")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/ver") && !event.getPlayer().hasPermission("hmp.ver")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/ver *") && !event.getPlayer().hasPermission("hmp.ver*")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/version") && !event.getPlayer().hasPermission("hmp.version")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/pl") && !event.getPlayer().hasPermission("hmp.pl")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/plugins") && !event.getPlayer().hasPermission("hmp.plugins")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/plugman") && !event.getPlayer().hasPermission("hmp.plugman")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/about") && !event.getPlayer().hasPermission("hmp.about")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/help") && !event.getPlayer().hasPermission("hmp.help")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:help") && !event.getPlayer().hasPermission("hmp.bukkit:help")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:ver") && !event.getPlayer().hasPermission("hmp.bukkit:ver")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:kill") && !event.getPlayer().hasPermission("hmp.bukkit:kill")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:?") && !event.getPlayer().hasPermission("hmp.bukkit:?")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/kill") && !event.getPlayer().hasPermission("hmp.kill")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/op") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:pl") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/minecraft:op") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/minecraft:me") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/bukkit:me") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/a") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/minecraft:version") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/icanhasbukkit") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/minecraft:?") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/essentials:help") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/minecraft:op") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
        if(event.getMessage().equals("/timings") && !event.getPlayer().hasPermission("hmp.op")) {
            TTA_Methods.sendTitle(player, "§2§lGuard", 10, 50, 10,"§7Sorry, Access Denied." , 10, 50, 10);
            event.setCancelled(true);
        }
    }


    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.DARK_GREEN + "- HideMyPlugins -" + ChatColor.GRAY + "v2.0");
        console.sendMessage(ChatColor.RED + "Plugin will now Shutdown...");
    }



    public void onLoad(){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.DARK_GREEN + "- HideMyPlugins -" + ChatColor.GRAY + "v2.0");
        console.sendMessage(ChatColor.GRAY + "Loading...");

    }

}
