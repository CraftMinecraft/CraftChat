package com.craftminecraft.craftchat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import com.craftminecraft.craftchat.CraftChat;
import com.craftminecraft.craftchat.conversations.Channel;

public class ChatManager {
    private CraftChat plugin;
    // Make those things thread-safe.
    private List<Channel> channelList;
    private HashMap<Player, List<String>> playerConvs;
    private HashMap<Player, String> playerConvFocus;

    public ChatManager(CraftChat plugin) {
        this.plugin = plugin;
        this.channelList = new ArrayList<Channel>();

        // SETUP CHANNEL CONFIG //
        File chanListFile = new File(this.plugin.getDataFolder(), "channels.yml");
        YamlConfiguration chanListConfig = YamlConfiguration.loadConfiguration(chanListFile);
        InputStream defChanListStream = this.plugin.getResource("channels.yml");
        if (defChanListStream != null) {
            YamlConfiguration defChanList = YamlConfiguration.loadConfiguration(defChanListStream);
            chanListConfig.setDefaults(defChanList);
        }
        // SET UP CHANNEL LIST //
        Map<String,Object> chanList = chanListConfig.getDefaultSection().getValues(false);
        for (Map.Entry<String, Object> chan : chanList.entrySet()) {
            ConfigurationSection chanConfig = (ConfigurationSection) chan.getValue();
            this.channelList.add(new Channel(chanConfig));
        }
    }

    public void autoJoinChannels(Player p) {
        for(Channel channel : channelList) {
            if (p.hasPermission("cmc.join." + channel.getName())) {
                joinChannel(p, channel);
            }
        }
    }

    public void joinChannel(Player p, String joinChan) { 
        for (Channel channel : channelList) {
            if (channel.getName() == joinChan) {
                this.joinChannel(p, channel);
                break;
            }
        }
    }

    public void joinChannel(Player p, Channel joinChan) {
        List<String> convList = playerConvs.get(p.getName());
        if (convList == null) {
            convList = new ArrayList<String>();
        }
        convList.add(joinChan.getName());
        this.playerConvs.put(p, convList);

        // also put in participents of channel.
        joinChan.join(p);
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
        List<String> convList = playerConvs.get(p.getName());
        if (convList != null) {
            convList.remove(leaveChan);
            this.playerConvs.put(p, convList);
        }
        leaveChan.leave(p);
    }

    public void setFocus(Player p, String channel) {
        playerConvFocus.put(p, channel);
    }

    public void getFocus(Player p) {
        playerConvFocus.get(p);
    }

    public void getChannelList() {
        return;
    }
}
