package com.craftminecraft.craftchat.utils;

import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;

import com.craftminecraft.craftchat.CraftChat;

public class Utils {
    public static String getPrefix(Player p) {
        String prefix = "";
        if (CraftChat.getInstance().chat != null) {
            Chat chat = CraftChat.getInstance().chat;
            // For all the ignorants out there (including myself) : //
            // http://www.terminally-incoherent.com/blog/2006/04/21/java-operator/ //
            // this explains the following line //
            prefix = (chat.getPlayerPrefix(p) != null) ? chat.getPlayerPrefix(p) : "";
            for (String group : chat.getPlayerGroups(p)) {
                if (prefix == "") {
                    prefix = chat.getGroupPrefix(p.getWorld(), group);
                }
            }
        }
        return prefix;
    }

    public static String getSuffix(Player p) {
        String suffix = "";
        if (CraftChat.getInstance().chat != null) {
            Chat chat = CraftChat.getInstance().chat;
            // For all the ignorants out there (including myself) : //
            // http://www.terminally-incoherent.com/blog/2006/04/21/java-operator/ //
            // this explains the following line //
            suffix = (chat.getPlayerSuffix(p) != null) ? chat.getPlayerSuffix(p) : "";
            for (String group : chat.getPlayerGroups(p)) {
                if (suffix == "") {
                    suffix = chat.getGroupSuffix(p.getWorld(), group);
                }
            }
        }
        return suffix;
    }

    public static String formatString(String message, Player player){
        message = message.replace("{WORLDNAME}", player.getWorld().getName());
        message = message.replace("{PREFIX}", getPrefix(player));
        message = message.replace("{SUFFIX}", getSuffix(player));
        message = message.replace("{PREFIX}", "");
        message = message.replace("{SUFFIX}", "");
        message = message.replace("{REALNAME}", player.getName());
        message = message.replace("{DISPNAME}", "%1$s");
        message = message.replace("{CHANNEL}", "G");
        message = message.replace("{MESSAGE}", "%2$s");
        return message;
    } 
}
