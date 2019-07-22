package com.gamerexde.HideMyPlugins;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.gamerexde.HideMyPlugins.Events.Blocker;
import com.google.common.base.Charsets;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import com.comphenix.protocol.reflect.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;


public final class Main extends JavaPlugin {
    ProtocolManager protocolManager;
    private FileConfiguration newConfig;
    private File configFile;

    public static final String version = "2.1.1-SNAPSHOT";

    public Main() {
        newConfig = null;
        configFile = new File(getDataFolder(), "messages.yml");
    }
    public static Main getInstance() {
        return (Main)getPlugin((Class)Main.class);
    }
    public List<String> getBlockedCommands() {
        return new ArrayList<String>(getConfig().getStringList("blockedCommands"));
    }

    public FileConfiguration getMsgConfig() {
        if (newConfig == null) {
            reloadMsgConfig();
        }
        return newConfig;
    }
    public void reloadMsgConfig() {
        newConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(configFile);
        final InputStream defConfigStream = getResource("messages.yml");
        if (defConfigStream == null) {
            return;
        }
        newConfig.setDefaults((Configuration)YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)new Blocker(), (Plugin)this);
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

        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }


        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.DARK_GREEN + "- HideMyPlugins -" + ChatColor.DARK_GRAY + version);
        console.sendMessage(ChatColor.GREEN + "Plugin Succesfully Loaded and Enabled!");



    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("HideMyPlugins")) {
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("hidemyplugins.reload")) {
                        sender.sendMessage("§e§lHideMyPlugins> §7Configuration reloaded.");
                        reloadConfig();
                    }
                    else {
                        sender.sendMessage("§e§lHideMyPlugins> §7You can't reload the configuration because you don't have permissions.");
                    }
                    return false;
                }
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("version")){
                    if (sender.hasPermission("hidemyplugins.version")) {
                        sender.sendMessage("§e§lHideMyPlugins> §7You are currently running HideMyPlugins " + version + "");
                        reloadConfig();
                    }
                    else {
                        sender.sendMessage("§e§lHideMyPlugins> §7You don't have permissions.");
                    }
                    return false;
                }
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) You need to be a player in order to execute that command.");
                return false;
            }

            Player player = (Player) sender;

            player.sendMessage("§8                       [§eHideMyPlugins§8]");
            player.sendMessage("                             §8[§e" + Main.version + "§8]     ");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hidemyplugins reload §8-> §7Reload the Config.yml");
            player.sendMessage("§7 - §a/hidemyplugins version §8-> §7Show actual version.");
            player.sendMessage("§8                 ------[§c Soon §8]-------------");
            player.sendMessage("§7 - §a/hidemyplugins historial §8-> §7Show users that have used cmd.");
            player.sendMessage("§7 - §a/hidemyplugins kicked §8-> §7Show users kick by using ilegal cmd.");


            return true;
        }

        return false;
    }



    @Override
    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.DARK_GREEN + "- HideMyPlugins -" + ChatColor.GRAY + version);
        console.sendMessage(ChatColor.RED + "Plugin will now Shutdown...");
    }

}
