package pro.gamerexde.hidemyplugins;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pro.gamerexde.hidemyplugins.Commands.Hidemyplugins;
import pro.gamerexde.hidemyplugins.Commands.hmpa;
import pro.gamerexde.hidemyplugins.Database.Database;
import pro.gamerexde.hidemyplugins.Database.MySQL;
import pro.gamerexde.hidemyplugins.Database.SQLite;
import pro.gamerexde.hidemyplugins.Events.Blocker;
import pro.gamerexde.hidemyplugins.Utils.IDGenerator;
import pro.gamerexde.hidemyplugins.Utils.Reflection;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pro.gamerexde.hidemyplugins.Utils.Reflection.getNMSClass;


public final class HideMyPlugins extends JavaPlugin implements Listener {
    private Database db;
    private MySQL msql;

    private FileConfiguration newConfig;
    private File configFile;

    public static final String version = "2.3.2-SNAPSHOT";

    private ProtocolManager protocolManager;

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
        this.getServer().getPluginManager().registerEvents((Listener)new Blocker(this), (Plugin)this);

        loadConfig();

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

        initDatabase();

        this.onTabEvent();

        getCommand("hidemyplugins").setExecutor(new Hidemyplugins(this));
        getCommand("hmpa").setExecutor(new hmpa(this));


    }


    public void onTabEvent(){
        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    if (event.getPlayer().hasPermission("hidemyplugins.access")) {
                        return;
                    } else {
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
                        try {
                            if (plugin.getConfig().getBoolean("use-mysql")){
                                Connection con = getMySQL().getConnection();

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();

                                String name = event.getPlayer().getName();
                                String uuid = event.getPlayer().getUniqueId().toString();
                                String date = dtf.format(now);
                                String executedCommand = "TAB COMPLETION";

                                PreparedStatement create = con.prepareStatement("INSERT INTO `"
                                        + plugin.getConfig().getString("MySQL.table_name")
                                        + "` (`ID`, `UUID`, `USER`, `EXECUTED_COMMAND`, `DATE`) VALUES ('"
                                        + IDGenerator.getAlphaNumericString() + "', '"
                                        + uuid + "', '"
                                        + name + "', '" + executedCommand
                                        + "', '" + date
                                        + "');");

                                create.executeUpdate();

                            } else if (plugin.getConfig().getBoolean("use-sqlite")){
                                Database con = getRDatabase();

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();

                                String name = event.getPlayer().getName();
                                String uuid = event.getPlayer().getUniqueId().toString();
                                String date = dtf.format(now);
                                String executedCommand = "TAB COMPLETION";

                                PreparedStatement send = con.executeCommand(IDGenerator.getAlphaNumericString(),uuid,name,executedCommand,date);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        event.setCancelled(true);

                    }
                }
            }

        });
    }

    public void initDatabase() {
        if (this.getConfig().getBoolean("use-sqlite")){
            if (this.getConfig().getBoolean("use-mysql")) {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You cannot select &cMySQL &7and &cSQLite &7at the same time in the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
                Bukkit.shutdown();
                return;
            } else
            this.db = new SQLite(this);
            this.db.load();
        } else if (this.getConfig().getBoolean("use-mysql")) {
            if (this.getConfig().getBoolean("use-sqlite")) {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You cannot select &cMySQL &7and &cSQLite &7at the same time in the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
                Bukkit.shutdown();
                return;
            } else
            this.msql = new MySQL(this);
            this.msql.initMySQLDatabase();
        } else {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You need to select at least one database method on the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
            Bukkit.shutdown();
        }
    }

    public Database getRDatabase() {
        return this.db;
    }

    public MySQL getMySQL() {
        return this.msql;
    }


    public void loadConfig(){
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }
    }



    public void reloadConfiguration(){
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
