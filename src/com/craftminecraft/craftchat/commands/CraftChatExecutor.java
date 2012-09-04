package com.craftminecraft.craftchat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.craftminecraft.craftchat.CraftChat;
import com.craftminecraft.craftchat.conversations.Channel;
import com.craftminecraft.craftchat.ChatManager;

public class CraftChatExecutor implements CommandExecutor {
    private final CraftChat plugin;
    private final ChatManager manager;
    public CraftChatExecutor(CraftChat plugin) {
       this.plugin = plugin;
       this.manager = plugin.chatManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return false;
        }
        if (!(player.isOnline())) {
            return false;
        }
        if (command.getName().equalsIgnoreCase("cc")) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.BOLD.toString() + ChatColor.RED.toString() + "Usage : ");
                    player.sendMessage(ChatColor.RED.toString() + "/cc join <channelname> : Join a channel or change focus to the channel.");
                    player.sendMessage(ChatColor.RED.toString() + "/cc leave <channelname> : Leave a channel.");
                    player.sendMessage(ChatColor.RED.toString() + "/cc listchannels : List the existing channels.");
                    player.sendMessage(ChatColor.RED.toString() + "/cc qm <channelname> <message> : Send a quick message to the channel.");
                } else if (args[0].equalsIgnoreCase("qm")) {
                    if (args.length < 2){
                        return false;
                    } else {
                        Channel qm = this.manager.getChannel(args[1]);
                        if (qm == null) {
                            return false;
                        }
                        int i = 0;
                        String chat = "";
                        for (String s : args) {
                            if (!(i < 2)) {
                                chat = chat + " " + s;
                            }
                            i = i + 1;
                        }
                        this.manager.chat(player, chat, qm);
                    }
                } else if (args[0].equalsIgnoreCase("listchannels")) {
                    player.sendMessage("Channels : ");
                    for (Channel chan : this.manager.getChannelList()) {
                        player.sendMessage(chan.getName() + " : " + chan.getNick());
                    } 
                    return true;
                } else if (args[0].equalsIgnoreCase("join")) { 
                    if (args.length != 2){ return false; }
                    if (this.manager.getChannel(args[1]) != null) {
                        this.manager.joinChannel(player, this.manager.getChannel(args[0]));
                        this.manager.setFocus(player, this.manager.getChannel(args[0]));
                        player.sendMessage(ChatColor.YELLOW.toString() + "Successfully joined channel.");
                        return true;
                    } else {
                       player.sendMessage(ChatColor.RED.toString() + "This channel does not exist !");
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    this.plugin.reloadConfig();
                    this.manager.reload();
                }
            }
        }
        return false;
    }
}
