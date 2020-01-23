package pro.gamerexde.hidemyplugins.Events;

import pro.gamerexde.hidemyplugins.HideMyPlugins;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pro.gamerexde.hidemyplugins.Utils.Reflection;

import java.lang.reflect.Constructor;
import java.util.Objects;

import static pro.gamerexde.hidemyplugins.Utils.Reflection.getNMSClass;

public class Blocker implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final PlayerCommandPreprocessEvent event) {
        final String[] msg = event.getMessage().split(" ");
        final Player player = event.getPlayer();
        final FileConfiguration config = HideMyPlugins.getInstance().getConfig();
        final FileConfiguration msgconfig = HideMyPlugins.getInstance().getMsgConfig();
        for (final String command : HideMyPlugins.getInstance().getBlockedCommands()) {
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.access")) {
                    if (Objects.requireNonNull(msgconfig.getString("grantMessage")).equalsIgnoreCase("none")) {
                        return;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("grantMessage"))));
                    try {
                        Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                        Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("titleGrantMessage"))) + "\"}");

                        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                        Object packet = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                        Reflection.sendPacket(event.getPlayer(), packet);
                    }

                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return;
                }
                else {
                    event.setCancelled(true);
                    if (Objects.requireNonNull(msgconfig.getString("blockMessage")).equalsIgnoreCase("none")) {
                        return;
                    }
                }
            }
            else {
                if (!msg[0].toLowerCase().contains(command) || !msg[0].toLowerCase().contains(":")) {
                    continue;
                }
                event.setCancelled(true);
                if (Objects.requireNonNull(msgconfig.getString("blockMessage")).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("blockMessage"))));
            }
        }
        for (final String command : HideMyPlugins.getInstance().getBlockedCommands()) {
            if (msg[0].toLowerCase().equals("/" + command)) {
                if (player.hasPermission("hidemyplugins.notify.message")) {
                    return;
                }
                event.setCancelled(true);
                if (Objects.requireNonNull((String)Objects.requireNonNull(msgconfig.getString("blockMessage"))).equalsIgnoreCase("none")) {
                    return;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("blockMessage"))));
                try {
                    Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                    Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', (String)Objects.requireNonNull(msgconfig.getString("titleBlockMessage"))) + "\"}");

                    Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                    Object packet = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);

                    Reflection.sendPacket(event.getPlayer(), packet);
                }

                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
