package com.craftminecraft.craftchat.listeners;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.craftminecraft.craftchat.CraftChat;


// IMPORTANT NOTE : UPON COMPILATION, Either onPlayerChatEvent or onAsyncPlayerChatEvent SHOULD BE commented out, depending on if we want version 1.2.5 or version 1.3.1. Will find a way to do this.

public final class ChatListener implements Listener {

    private final CraftChat plugin;

    public ChatListener(CraftChat instance){
        this.plugin = instance;
    }

/*    @EventHandler
    public void onPlayerChatEvent(final PlayerChatEvent event){
        if(event.isCancelled()){
            return;
        } else {
            // Get chat format from config
            String format = this.plugin.getConfig().getString("format");
            event.setFormat(parseAndReplace(format, event.getPlayer()));
        }
    }*/

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent event){
        this.plugin.getLogger().info("Chat handled");
        //this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
        //     public void run() {
                String format = ChatListener.parseAndReplace(CraftChat.getInstance().getConfig().getString("format"), event.getPlayer());
                CraftChat.getInstance().getLogger().info(format);
                event.setFormat(format);
                CraftChat.getInstance().getLogger().info(event.getFormat());
        //    }
        //});
    }
    public static String parseAndReplace(String format, Player p) {
        format = format.replace("{WORLDNAME}", p.getWorld().getName());
        // Verify that the prefix is not null
        if (CraftChat.chat != null) {
            if (CraftChat.chat.getPlayerPrefix(p) != null) {
                format = format.replace("{PREFIX}", CraftChat.chat.getPlayerPrefix(p));
            }
            if (CraftChat.chat.getPlayerSuffix(p) != null) {
                format = format.replace("{SUFFIX}", CraftChat.chat.getPlayerPrefix(p));
            }
        }
        format = format.replace("{PREFIX}", "");
        format = format.replace("{SUFFIX}", "");
        format = format.replace("{REALNAME}", p.getName());
        format = format.replace("{DISPNAME}", "%1$s");
        format = format.replace("{CHANNEL}", "G");
        format = format.replace("{MESSAGE}", "%2$s");
        return format;
    }
}
