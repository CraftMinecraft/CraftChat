package com.craftminecraft.craftchat.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.Bukkit;

import com.craftminecraft.craftchat.CraftChat;

public class ChatListener implements Listener {

    private CraftChat plugin;

    public ChatListener(CraftChat plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChatEvent(final PlayerChatEvent event){
        if(event.isCancelled()){
            return;
        } else {
            String format = this.plugin.getConfig().getString("craftchat.format");
            format.replace("{WORLDNAME}", event.getPlayer().getWorld().getName());
            format.replace("{PREFIX}", CraftChat.chat.getPlayerPrefix(event.getPlayer()));
            format.replace("{SUFFIX}", CraftChat.chat.getPlayerSuffix(event.getPlayer()));
            format.replace("{REALNAME}", event.getPlayer().getName());
            format.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName());
            format.replace("{CHANNEL}", "G");
            event.setFormat(format);
        }
    }
}
