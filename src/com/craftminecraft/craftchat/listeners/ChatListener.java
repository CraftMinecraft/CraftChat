package com.craftminecraft.craftchat.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.Bukkit;

import net.milkbowl.vault.chat.Chat;

public class ChatListener implements Listener {

    private FileConfiguration config;
    private Chat chat;
    public ChatListener(FileConfiguration config){
        this.config = config;
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        this.chat = rsp.getProvider();

    }

    @EventHandler
    public void onPlayerChatEvent(final PlayerChatEvent event){
        if(event.isCancelled()){
            return;
        } else {
            String format = this.config.getString("craftchat.format");
            format.replace("{WORLDNAME}", event.getPlayer().getWorld().getName());
            format.replace("{PREFIX}", chat.getPlayerPrefix(event.getPlayer()));
            format.replace("{SUFFIX}", chat.getPlayerSuffix(event.getPlayer()));
            format.replace("{REALNAME}", event.getPlayer().getName());
            format.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName());
            format.replace("{CHANNEL}", "G");
            event.setFormat(format);
        }
    }
}
