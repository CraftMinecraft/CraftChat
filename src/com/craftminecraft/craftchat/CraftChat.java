package com.craftminecraft.craftchat;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.event.HandlerList;

import net.milkbowl.vault.chat.Chat;

import com.craftminecraft.craftchat.listeners.ChatListener;
import com.craftminecraft.craftchat.ChatManager;
import com.craftminecraft.craftchat.commands.CraftChatExecutor;

public class CraftChat extends JavaPlugin {
    public static Chat chat = null;
    public static CraftChat instance;
    public ChatManager chatManager;

    @Override
    public void onEnable() {
        CraftChat.instance = this;
        loadConfiguration();
        this.chatManager = new ChatManager(this);
        // TODO : Should probably map the config and set the defaults dynamically, JIC someone screws up their config file.
        getLogger().info("Enabling CraftChat version " + getDescription().getVersion());
        setupVault();
        setupCommands();
        setupEvents();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling CraftChat");
        HandlerList.unregisterAll(this);
    }

    private Boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
           getServer().getPluginManager().disablePlugin(this); 
        }
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) chat = chatProvider.getProvider();
        return (chat != null);
    }

    private void loadConfiguration(){
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void setupCommands(){
        getCommand("cc").setExecutor(new CraftChatExecutor(this));
    }

    private void setupEvents(){
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    public static CraftChat getInstance() {
        return CraftChat.instance;
    }
}
