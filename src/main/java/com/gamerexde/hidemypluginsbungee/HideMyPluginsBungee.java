package com.gamerexde.hidemypluginsbungee;

import com.gamerexde.hidemypluginsbungee.Commands.HideMyPlugins;
import com.gamerexde.hidemypluginsbungee.Commands.hmpa;
import com.gamerexde.hidemypluginsbungee.Database.MySQL;
import com.gamerexde.hidemypluginsbungee.Event.Blocker;
import com.gamerexde.hidemypluginsbungee.Event.onTabEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

public class HideMyPluginsBungee extends Plugin {
    private MySQL msql;

    private File configFile;
    private Configuration config;

    private File msgConfigFile;
    private Configuration msgConfig;

    private List<String> blockedCommands;

    String version = "2.3.2-SNAPSHOT";

    @Override
    public void onEnable() {
        this.loadConfig();
        this.loadMsgConfig();

        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new onTabEvent(this));
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new Blocker(this));

        this.getProxy().getPluginManager().registerCommand(this, new HideMyPlugins());
        this.getProxy().getPluginManager().registerCommand(this, new hmpa(this));

        getLogger().info("\n");
        getLogger().info(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Ver: " + getVersion() + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Status: " + ChatColor.GREEN + "ON" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GREEN + "");
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7The plugin has loaded correctly!"));

        this.initDatabase();
    }

    public void loadConfig(){
        this.configFile = new File(this.getDataFolder(), "config.yml");

        if (!this.configFile.exists()) {
            if (!this.configFile.getParentFile().exists() && !this.configFile.getParentFile().mkdirs()) {
                throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not create plugin's folder. Verify system permissions."));
            }
            try (final InputStream inputStream = this.getResourceAsStream("config.yml")) {
                if (inputStream == null) {
                    throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not find plugin's folder."));
                }
                Files.copy(inputStream, Paths.get(this.configFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException exception) {
                throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not create config file."), exception);
            }
        }
        try {
            this.config = ConfigurationProvider.getProvider((Class) YamlConfiguration.class).load(this.configFile);
        }
        catch (IOException exception) {
            throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not load config file."));
        }
        this.blockedCommands = (List<String>)this.config.getStringList("blockedCommands");
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public Configuration getConfig() {
        return this.config;
    }

    public List<String> getBlockedCommands() {
        return Collections.unmodifiableList((List<? extends String>)this.blockedCommands);
    }

    public void loadMsgConfig(){
        this.msgConfigFile = new File(this.getDataFolder(), "messages.yml");

        if (!this.msgConfigFile.exists()) {
            if (!this.configFile.getParentFile().exists() && !this.msgConfigFile.getParentFile().mkdirs()) {
                throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not create plugin's folder. Verify system permissions."));
            }
            try (final InputStream inputStream = this.getResourceAsStream("messages.yml")) {
                if (inputStream == null) {
                    throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not find plugin's folder."));
                }
                Files.copy(inputStream, Paths.get(this.msgConfigFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException exception) {
                throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not create messages file."), exception);
            }
        }
        try {
            this.msgConfig = ConfigurationProvider.getProvider((Class) YamlConfiguration.class).load(this.msgConfigFile);
        }
        catch (IOException exception) {
            throw new RuntimeException(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Could not load messages file."));
        }
    }

    public File getMsgConfigFile() {
        return this.configFile;
    }

    public Configuration getMsgConfig() {
        return this.msgConfig;
    }

    public void initDatabase() {
        if (this.getConfig().getBoolean("use-sqlite")) {
            if (this.getConfig().getBoolean("use-mysql")) {
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You cannot select &cMySQL &7and &cSQLite &7at the same time in the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
                ProxyServer.getInstance().stop();
                return;
            } else
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7SQLite is not supported on BungeeCord, you need use MySQL."));
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7For security reasons the server will shutdown until you set up the config correctly..."));
                ProxyServer.getInstance().stop();
        } else if (this.getConfig().getBoolean("use-mysql")) {
            if (this.getConfig().getBoolean("use-sqlite")) {
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You cannot select &cMySQL &7and &cSQLite &7at the same time in the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
                ProxyServer.getInstance().stop();
                return;
            } else
                this.msql = new MySQL(this);
            this.msql.initMySQLDatabase();
        } else {
            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7You need to select at least one database method on the config.yml, for security reasons the server will shutdown until you set up the config correctly..."));
            ProxyServer.getInstance().stop();
        }
    }

    public MySQL getMySQL() {
        return this.msql;
    }

    @Override
    public void onDisable() {
        getLogger().info("\n");
        getLogger().info(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Ver: " + getVersion() + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Status: " + ChatColor.RED + "OFF" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GREEN + "");
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7The plugin has unloaded correctly!"));

    }

    @Override
    public void onLoad() {
        getLogger().info("\n");
        getLogger().info(ChatColor.YELLOW + "  ___ ___    _____ __________ " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " /   |   \\  /     \\\\______   \\" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "/    ~    \\/  \\ /  \\|     ___/" + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "\\    Y    /    Y    \\    |    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + " \\___|_  /\\____|__  /____|    " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.YELLOW + "       \\/         \\/          " + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Ver: " + getVersion() + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GOLD + "       Status: " + ChatColor.YELLOW + "Loading..." + ChatColor.DARK_GRAY);
        getLogger().info(ChatColor.GREEN + "");
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e&lHideMyPlugins> &7Starting up..."));

    }

    public boolean equalsIgnoreCase(final List<String> list, final String searchString) {
        if (list == null || searchString == null) {
            return false;
        }
        if (searchString.isEmpty()) {
            return true;
        }
        for (final String string : list) {
            if (string == null) {
                continue;
            }
            if (string.equalsIgnoreCase(searchString)) {
                return true;
            }
        }
        return false;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
