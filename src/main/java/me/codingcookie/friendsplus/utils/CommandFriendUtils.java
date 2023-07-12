package me.codingcookie.friendsplus.utils;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.sqliteutil.Errors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;

public class CommandFriendUtils implements MsgLevels{

    private final FriendsPlus plugin;
    public CommandFriendUtils(FriendsPlus plugin){
        this.plugin = plugin;
    }

    public void setFriendStatus(UUID playerUUID, UUID targetUUID, String futureStatus){
        if(!checks(playerUUID, targetUUID)){
            return;
        }
        String currentStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID.toString(), targetUUID.toString());
        Player playerO = Bukkit.getPlayer(playerUUID);
        OfflinePlayer targetO = Bukkit.getOfflinePlayer(targetUUID);

        //TODO: Something is wrong here. I think. Looking into it...

        if(currentStatus.equals("pending")){
            String targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString());
            if(currentStatus.equals(targetStatus)){
                plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "accepted", getTime());
                plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "accepted", getTime());
                playerO.sendMessage(ACCEPT + "You are now friends with " + targetO.getName() + ".");
                if(targetO.isOnline()){
                    Player targetOnline = Bukkit.getPlayer(targetUUID);
                    targetOnline.sendMessage(ACCEPT + "You are now friends with " + playerO.getName() + ".");
                }
                return;
            } else {
                if(plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString()).contains("pending")){
                    playerO.sendMessage(PENDING + "You already have a pending request with " + targetO.getName() + ".");
                    return;
                }else{
                    plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), futureStatus, getTime());
                    return;
                }
            }
        }

        if(currentStatus.equals(futureStatus)){
            if(currentStatus.equals("accepted")){
                playerO.sendMessage(ACCEPT + "You are already friends with " + targetO.getName() + ".");
                return;
            }
            if(currentStatus.equals("blocked")){
                playerO.sendMessage(REJECT + "You cannot add " + targetO.getName() + " as a friend.");
                return;
            }
        }
        plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), futureStatus, getTime());
        plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerO.getUniqueId().toString(), futureStatus, getTime());

    }

    public void sendFriendList(Player player){
        ArrayList<String> friendListStringUUID = new ArrayList<>();
        friendListStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted"));

        ArrayList<String> friendListPendingStringUUID = new ArrayList<>();
        friendListPendingStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "pending"));

        if(friendListStringUUID.contains(Errors.noFriendsFound())){
            player.sendMessage(Errors.noFriendsFound());
            friendListStringUUID.clear();
            return;
        }

        player.sendMessage(PENDING + "Pending requests:");
        for(String friendPendingList : friendListPendingStringUUID){
            OfflinePlayer offlinePendingPlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendPendingList));
            player.sendMessage(PENDING + offlinePendingPlayer.getName());
        }

        player.sendMessage(ACCEPT + "Current friends:");
        for(String friendList : friendListStringUUID){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendList));
            if(offlinePlayer.isOnline()) {
                player.sendMessage(ACCEPT + "" + ChatColor.GREEN + offlinePlayer.getName());
            }else{
                player.sendMessage(ACCEPT + offlinePlayer.getName());
            }
        }

        friendListStringUUID.clear();
    }

    public boolean checks(UUID playerUUID, UUID targetUUID){
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null){
            Bukkit.getLogger().log(Level.SEVERE, Errors.playerReturnedNull());
            return false;
        }
        if(targetPlayer == null){
            player.sendMessage(REJECT + "This player doesn't exist.");
            return false;
        }

        if(targetPlayer.isBanned()){
            player.sendMessage(REJECT + "Unfortunately, the player you are trying to friend is banned.");
            return false;
        }
        if(!targetPlayer.hasPlayedBefore()){
            player.sendMessage(REJECT + "You can't friend players that haven't joined this server before.");
            return false;
        }
        return true;
    }

    public String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
