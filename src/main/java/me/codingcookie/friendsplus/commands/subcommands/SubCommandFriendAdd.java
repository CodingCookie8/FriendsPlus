package me.codingcookie.friendsplus.commands.subcommands;

import me.codingcookie.friendsplus.FriendsPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class SubCommandFriendAdd {

    private final FriendsPlus plugin;
    public SubCommandFriendAdd(FriendsPlus plugin){
        this.plugin = plugin;
    }

    public void checkAndAddFriend(Player player, String friendString, String status){
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(friendString);
        if(targetPlayer == null){
            player.sendMessage(ChatColor.RED + "> " + ChatColor.GRAY + "This player doesn't exist.");
            return;
        }
        addFriend(player, targetPlayer, status);
    }

    public void addFriend(Player player, OfflinePlayer targetedOfflinePlayer, String status){
        UUID targetUUID = targetedOfflinePlayer.getUniqueId();

        if(targetedOfflinePlayer.isBanned()){
            player.sendMessage("Unfortunately, the player you are trying to friend is banned.");
            return;
        }
        if(!targetedOfflinePlayer.hasPlayedBefore()){
            player.sendMessage("You can't friend players that haven't joined this server before.");
            return;
        }
        if(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "pending").contains(targetUUID.toString())){
            player.sendMessage("You've already sent a request to " + targetedOfflinePlayer.getName() + ".");
            return;
        }
        if(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted").contains(targetUUID.toString())){
            player.sendMessage("You are already friends with " + targetedOfflinePlayer.getName() + ".");
            return;
        }

        plugin.getFriendDatabase().setFriend(
                player.getUniqueId().toString(),
                targetUUID.toString(),
                status,
                getTime()
                );

        player.sendMessage("You've sent a friend request to " + targetedOfflinePlayer.getName() + "!");

    }

    public String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
