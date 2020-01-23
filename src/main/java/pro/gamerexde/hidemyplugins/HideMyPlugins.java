package pro.gamerexde.hidemyplugins;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import pro.gamerexde.hidemyplugins.Events.Blocker;
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
import pro.gamerexde.hidemyplugins.Utils.Reflection;

import static pro.gamerexde.hidemyplugins.Utils.Reflection.getNMSClass;


public final class HideMyPlugins extends JavaPlugin implements Listener {
    ProtocolManager protocolManager;
    private FileConfiguration newConfig;
    private File configFile;

    public static final String version = "2.2.0-SNAPSHOT";

    public HideMyPlugins() {
        newConfig = null;
        configFile = new File(getDataFolder(), "messages.yml");
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

    public static HideMyPlugins getInstance() {
        return (HideMyPlugins)getPlugin((Class)HideMyPlugins.class);
    }
    public List<String> getBlockedCommands() {
        return new ArrayList<String>(getConfig().getStringList("blockedCommands"));
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)new Blocker(), (Plugin)this);

        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        console.sendMessage(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Ver: " + version + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Status: " + ChatColor.GREEN + "ON" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GREEN + "");
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7The plugin has loaded correctly!"));

        onTabEvent();
    }

    public void onTabEvent(){
        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();
        (this.protocolManager = ProtocolLibrary.getProtocolManager()).addPacketListener((PacketListener)new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    if (event.getPlayer().hasPermission("hidemyplugins.access")){
                        try {
                            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("titleGrantMessage"))) + "\"}");

                            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                            Object packetReflection = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                            Reflection.sendPacket(event.getPlayer(), packetReflection);
                        }
                        catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return;
                    } else
                        try {
                            final PacketContainer packet = event.getPacket();
                            final String message = ((String)packet.getSpecificModifier((Class)String.class).read(0)).toLowerCase();

                            if (message.startsWith("") && !message.contains("  ")) {
                                try {
                                    Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                                    Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("titleBlockMessage"))) + "\"}");

                                    Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                                    Object packetReflection = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                                    Reflection.sendPacket(event.getPlayer(), packetReflection);
                                }

                                catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                event.setCancelled(true);
                            }
                        }
                    catch (FieldAccessException e) {
                        HideMyPlugins.this.getLogger().log(Level.SEVERE, "Couldn't access field.", (Throwable)e);
                    }
                }
            }
        });
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("HideMyPlugins")) {
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    if (sender.hasPermission("hidemyplugins.reload")) {
                        doReloadConfiguration();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Configuration reloaded!"));
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You can't reload the plugin because you don't have permissions..."));
                    }
                    return false;
                }
            }
            if(args.length == 1){
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("(!) You need to be a player in order to execute that command.");
                return false;
            }

            Player player = (Player) sender;

            player.sendMessage("§8                       [§eHideMyPlugins§8]");
            player.sendMessage("                      §e" + HideMyPlugins.version +"");
            player.sendMessage("");
            player.sendMessage("§7 - §a/hidemyplugins reload §8-> §7Reload the configuration.");
            player.sendMessage("");

            return true;
        }

        return false;
    }

    public void doReloadConfiguration(){
        reloadMsgConfig();
        reloadConfig();
    }



    @Override
    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        console.sendMessage(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Ver: " + version + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Status: " + ChatColor.RED + "OFF" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GREEN + "");
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7The plugin will now shutdown..."));
    }

    @Override
    public void onLoad() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();


        console.sendMessage(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Ver: " + version + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GOLD + "       Status: " + ChatColor.YELLOW + "Loading..." + ChatColor.DARK_GRAY);
        console.sendMessage(ChatColor.GREEN + "");
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Starting up..."));
    }

}
