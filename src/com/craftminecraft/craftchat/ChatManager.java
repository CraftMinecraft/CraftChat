package com.craftminecraft.craftchat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.craftminecraft.craftchat.CraftChat;
import com.craftminecraft.craftchat.conversations.Channel;
import com.craftminecraft.craftchat.utils.Utils;
import com.craftminecraft.craftchat.utils.ConfigAccessor;

public class ChatManager {
    private CraftChat plugin;
    // Make those things thread-safe.
    private List<Channel> channelList;
    private ConcurrentHashMap<Player, List<Channel>> playerConvs;
    private ConcurrentHashMap<Player, Channel> playerConvFocus;
    private ConcurrentHashMap<String, String> formats;

    public ChatManager(CraftChat plugin) {
        this.plugin = plugin;
        this.playerConfig = new ConfigAccessor(this.plugin, "players.yml");
        this.playerConfig.getConfig();
        this.channelList = new ArrayList<Channel>();
        this.playerConvs = new ConcurrentHashMap<Player, List<Channel>>();
        this.playerConvFocus = new ConcurrentHashMap<Player, Channel>();
        this.formats = new ConcurrentHashMap<String, String>();
        // SETUP FORMATS //
        for (String key : this.plugin.getConfig().getConfigurationSection("formats").getKeys(false)) {
            formats.put(key, this.plugin.getConfig().getString("formats." + key));
        }
        
        this.playerConfig.saveDefaultConfig();

        File dir = new File(this.plugin.getDataFolder, "channels");
        for (File child : dir.listFiles()) {
            if (".".equals(child.getName()) || "..".equals(child.getName())) {
                continue;
            }
            Channel newChan = new Channel(child);
            this.channelList.add(newChan);
        }

        // SETUP CHANNEL CONFIG //
        this.chanListConfig.saveDefaultConfig();

        // SET UP CHANNEL LIST //
        Map<String,Object> chanList = chanListConfig.getConfig().getDefaultSection().getValues(false);
        for (Map.Entry<String, Object> chan : chanList.entrySet()) {
            ConfigurationSection chanConfig = (ConfigurationSection) chan.getValue();
            this.channelList.add(new Channel(chanConfig));
        }
    }

    public void save() {
        for (Channel chan : this.channelList) {
            this.chanListConfig.getConfig().set(chan.getName() + ".nick", chan.getNick());
            this.chanListConfig.getConfig().set(chan.getName() + ".color", chan.getColor());
            this.chanListConfig.getConfig().set(chan.getName() + ".format", chan.getFormat());
        }
        this.chanListConfig.saveConfig();
    }

    public void reload() {
        this.playerConfig.save();
        // reload channelList.
        this.chanListConfig.reloadConfig();
        this.channelList.clear();
        // This should get loaded back by player config. //
        this.playerConvs.clear();
        this.playerConvFocus.clear();
        Map<String,Object> chanList = chanListConfig.getConfig().getDefaultSection().getValues(false);
        for (Map.Entry<String, Object> chan : chanList.entrySet()) {
            ConfigurationSection chanConfig = (ConfigurationSection) chan.getValue();
            this.channelList.add(new Channel(chanConfig));
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            player.autoJoinChannel(player);
        }
    }

    public void save() {
        this.playerConfig.save();
        this.chanListConfig.save();
    }

    public void autoJoinChannels(Player p) {
        for(Channel channel : channelList) {
            if (p.hasPermission("cmc.auto.join." + channel.getName())) {
                joinChannel(p, channel);
            }
        }
        if (this.playerConfig.getConfig().getStringList(p.getName() + ".channels") == null) {
            this.playerConfig.getConfig().set(p.getName() + ".channels", new ArrayList<String>());
        }
        List<String> tempList = this.playerConfig.getConfig().getStringList(p.getName() + ".channels");
        for (String chanName : tempList) {
            if (this.getChannel(chanName) != null) {
                this.joinChannel(p, this.getChannel(chanName));
            }
        }
        this.setFocus(p, this.getChannel(this.playerConfig.getConfig().getString(p.getName() + ".focus", this.plugin.getConfig().getString("defaultChannel"))));
    }

    public void autoLeaveChannels(Player p) {
        for (Channel channel : this.playerConvs.get(p)) {
            channel.leave(p);
        }
        this.playerConvs.remove(p);
        this.playerConvFocus.remove(p);
    }

    public boolean joinChannel(Player p, Channel joinChan) {
        List<Channel> convList = playerConvs.get(p.getName());
        if (convList == null) {
            convList = new ArrayList<Channel>();
        }
        convList.add(joinChan);
        if (this.playerConfig.getConfig().getStringList(p.getName() + ".channels") == null) {
            this.playerConfig.getConfig().set(p.getName() + ".channels", new ArrayList<String>());
        }
        List<String> tempList = this.playerConfig.getConfig().getStringList(p.getName() + ".channels");
        tempList.add(joinChan.getName());
        this.playerConfig.getConfig().set(p.getName() + ".channels", tempList);
        this.playerConvs.put(p, convList);
        // also put in participents of channel.
        joinChan.join(p);
        return true;
    }

    public void leaveChannel(Player p, String leaveChan) {
        for (Channel channel : channelList) {
            if (channel.getName() == leaveChan) {
                this.leaveChannel(p, channel);
                break;
            }
        }
    }

    public void leaveChannel(Player p, Channel leaveChan) {
        List<Channel> convList = playerConvs.get(p.getName());
        if (convList != null) {
            convList.remove(leaveChan);
            this.playerConvs.put(p, convList);
        }
        if (this.playerConfig.getConfig().getStringList(p.getName() + ".channels") == null) {
            this.playerConfig.getConfig().set(p.getName() + ".channels", new ArrayList<String>());
        }
        List<String> tempList = this.playerConfig.getConfig().getStringList(p.getName() + ".channels");
        if (tempList.contains(leaveChan.getName())) {
            tempList.remove(leaveChan.getName());
            this.playerConfig.getConfig().set(p.getName() + ".channels", tempList);
        }
        leaveChan.leave(p);
    }

    public void setFocus(Player p, Channel channel) {
        playerConvFocus.put(p, channel);
        this.playerConfig.getConfig().set(p.getName() + ".focus", channel.getName());
    }

    public Channel getFocus(Player p) {
        return playerConvFocus.get(p);
    }

    
    public Channel getChannel(String name) {
        for (Channel channel : channelList) {
            if ((channel.getName().equalsIgnoreCase(name)) || (channel.getNick().equalsIgnoreCase(name))) {
                return channel;
            }
        }
        return null;
    }

    public String formatString(Player player) {
        return formatString(player, getFocus(player));
    }

    public String formatString(Player player, Channel channel) {
        String format = channel.getFormat();
        for (String formatname : formats.keySet()) {
            format = format.replaceAll("(?i)" + Pattern.quote("{" + formatname + "}"), formats.get(formatname));
        }
        return Utils.formatString(format, player, channel);
    }

    public void chat(Player sender, String message, Channel channel) {
        String formatString = formatString(sender, channel);
        message = String.format(formatString, sender.getDisplayName(), message);
        for (Player player : channel.getParticipants()) {
            player.sendMessage(message);
        }
    }

    public List<Channel> getChannelList() {
        return channelList;
    }
}
