package pro.gamerexde.hidemyplugins.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pro.gamerexde.hidemyplugins.Database.Database;
import pro.gamerexde.hidemyplugins.HideMyPlugins;
import pro.gamerexde.hidemyplugins.Utils.DateGenerator;
import pro.gamerexde.hidemyplugins.Utils.IDGenerator;
import pro.gamerexde.hidemyplugins.Utils.Reflection;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static pro.gamerexde.hidemyplugins.Utils.Reflection.getNMSClass;

public class Blocker implements Listener {
    HideMyPlugins plugin;
    public Blocker(HideMyPlugins instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final PlayerCommandPreprocessEvent event) {
        final String[] msg = event.getMessage().split(" ");
        final Player player = event.getPlayer();
        final FileConfiguration config = HideMyPlugins.getInstance().getConfig();
        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();
        for (final String command : HideMyPlugins.getInstance().getBlockedCommands()) {
            for (final String whitelistedCommands : HideMyPlugins.getInstance().getWhitelistCommands()) {
                if (msg[0].toLowerCase().equals("/" + whitelistedCommands)) {
                    return;
                } else {
                    continue;
                }
            }
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.access")) {
                    return;
                } else {
                    event.setCancelled(true);
                    if (Objects.requireNonNull(msgconfig.getString("deny.block_message")).equalsIgnoreCase("none")) {
                        return;
                    }
                }
            } else {
                if (!msg[0].toLowerCase().contains(command) || !msg[0].toLowerCase().contains(":")) {
                    continue;
                }
                event.setCancelled(true);
                if (Objects.requireNonNull(msgconfig.getString("deny.block_message")).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("deny.block_message"))));

                if (plugin.getConfig().getBoolean("sounds.enable-sounds")) {
                    String[] arr = HideMyPlugins.getInstance().getSoundsArray().toArray(new String[0]);

                    Random random = new Random();
                    int randomSelect = random.nextInt(arr.length);

                    player.playSound(player.getLocation(), Sound.valueOf(arr[randomSelect]), 10f, 1f);
                }

                if (plugin.getConfig().getBoolean("blockedCommandAlert")) {
                    try {
                        Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                        Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("deny.title_block_message"))) + "\"}");

                        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                        Object packetReflection = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                        Reflection.sendPacket(event.getPlayer(), packetReflection);
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    Database con = plugin.getRDatabase();

                    String name = event.getPlayer().getName();
                    String uuid = event.getPlayer().getUniqueId().toString();
                    String executedCommand = msg[0];

                    PreparedStatement send = con.executeCommand(IDGenerator.getAlphaNumericString(),uuid,name,executedCommand,DateGenerator.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.hasPermission("hidemyplugins.notify.message")) {
                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = msgconfig.getString("admin_messages.block_admin_notify_message");
                            message = message.replace("{USER}", event.getPlayer().getName());

                            players.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{USER}", event.getPlayer().getName())));
                        }
                    }
                }
                break;
            }
        }

        for (final String command : HideMyPlugins.getInstance().getBlockedCommands()) {
            for (final String whitelistedCommands : HideMyPlugins.getInstance().getWhitelistCommands()) {
                if (msg[0].toLowerCase().equals("/" + whitelistedCommands)) {
                    return;
                } else {
                    continue;
                }
            }
            if (msg[0].toLowerCase().equals("/" + command)) {
                List<Player> output = new ArrayList<Player>();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.hasPermission("hidemyplugins.notify.message")) {
                        if (plugin.getConfig().getBoolean("adminNotify")) {
                            String message = msgconfig.getString("admin_messages.block_admin_notify_message");
                            message = message.replace("{USER}", event.getPlayer().getName());

                            players.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{USER}", event.getPlayer().getName())));
                        }
                    }
                }
                if (Objects.requireNonNull((String) Objects.requireNonNull(msgconfig.getString("deny.block_message"))).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("deny.block_message"))));

                if (plugin.getConfig().getBoolean("sounds.enable-sounds")) {
                    String[] arr = HideMyPlugins.getInstance().getSoundsArray().toArray(new String[0]);

                    Random random = new Random();
                    int randomSelect = random.nextInt(arr.length);

                    player.playSound(player.getLocation(), Sound.valueOf(arr[randomSelect]), 10f, 1f);
                }

                try {
                    Database con = plugin.getRDatabase();

                    String name = event.getPlayer().getName();
                    String uuid = event.getPlayer().getUniqueId().toString();
                    String executedCommand = msg[0];

                    PreparedStatement send = con.executeCommand(IDGenerator.getAlphaNumericString(),uuid,name,executedCommand,DateGenerator.getDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (plugin.getConfig().getBoolean("blockedCommandAlert")) {
                    try {
                        Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                        Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String) Objects.requireNonNull(msgconfig.getString("deny.title_block_message"))) + "\"}");

                        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                        Object packetReflection = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                        Reflection.sendPacket(event.getPlayer(), packetReflection);
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
