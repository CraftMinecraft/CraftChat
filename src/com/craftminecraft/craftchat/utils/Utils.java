package com.craftminecraft.craftchat.utils;

import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;

import com.craftminecraft.craftchat.CraftChat;
import com.craftminecraft.craftchat.conversations.Channel;

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

    public static String formatString(String message, Player player, Channel channel){
        message = message
                  .replaceAll("(?i){WORLDNAME}", player.getWorld().getName())
                  .replaceAll("(?i){PREFIX}", getPrefix(player))
                  .replaceAll("(?i){SUFFIX}", getSuffix(player))
                  .replaceAll("(?i){REALNAME}", player.getName())
                  .replaceAll("(?i){DISPNAME}", "%1$s")
                  .replaceAll("(?i){CHANNELNAME}", channel.getName())
                  .replaceAll("(?i){CHANNELNICK}", channel.getNick())
                  .replaceAll("(?i){MESSAGE}", "%2$s");
        return message;
    } 
}
