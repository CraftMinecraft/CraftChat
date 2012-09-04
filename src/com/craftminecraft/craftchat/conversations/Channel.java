package com.craftminecraft.craftchat.conversations;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

public class Channel {
    private Set<Player> participants;
    private String name;
    private String nick;
    private String format;
    private String color;

    public Channel (ConfigurationSection config) {
        this.participants = Collections.synchronizedSet(new HashSet());
        this.name = config.getName();
        this.nick = config.getString("nick", "");
        this.format = config.getString("format", "{DEFAULT}");
        this.color = config.getString("color", "&f");
    }

    public void join(Player p) {
        if (!(this.participants.contains(p))) {
            this.participants.add(p);
            for (Player player : this.participants) {
                if (player.isOnline()) {
                    player.sendMessage(p.getDisplayName() + " has joined " + this.getName() + ".");
                }
            }
        }
    }

    public void leave(Player p) {
        if (this.participants.contains(p)) {
            this.participants.remove(p);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getNick() {
        return this.nick;
    }

    public String getFormat() {
        return this.format;
    }

    public String getColor() {
        return this.color;
    }

    public Set<Player> getParticipants() {
        return this.participants;
    } 
}
