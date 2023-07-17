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

    private String playerStatus;
    private String targetStatus;

    public void sendFriendRequest(UUID playerUUID, UUID targetUUID){
        if(!checks(playerUUID, targetUUID)){
            return;
        }

        Player player = Bukkit.getPlayer(playerUUID);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);

        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID.toString(), targetUUID.toString());
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString());

        if(targetStatus != null){
            if (playerStatus.equals("accepted") && targetStatus.equals("accepted")){
                player.sendMessage(ACCEPT + "You are already friends with " + targetPlayer.getName() + ".");
                return;
            }
            if(playerStatus.equals("pending")){
                player.sendMessage(ACCEPT + "You have already sent a friend request to " + targetPlayer.getName() + ".");
                return;
            }
        }

        if(checkIfPendingRequest(targetUUID, playerUUID)){
            acceptFriend(playerUUID, targetUUID);
        } else {
            plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "pending", getTime());
            plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "receivedrequest", getTime());
            player.sendMessage(PENDING + "You have sent a friend request to " + targetPlayer.getName() + ".");
            if(targetPlayer.isOnline()){
                Player targetOnline = Bukkit.getPlayer(targetUUID);
                targetOnline.sendMessage(PENDING + player.getName() + " has sent you a friend request.");
            }
        }
    }

    public void rejectFriendRequest(UUID playerUUID, UUID targetUUID){
        if(!checks(playerUUID, targetUUID)){
            return;
        }

        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID.toString(), targetUUID.toString());
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString());

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        Player player = Bukkit.getPlayer(playerUUID);

        if(!checkIfPendingRequest(targetUUID, playerUUID)){
            player.sendMessage(REJECT + "There is no pending friend request between you and " + targetPlayer.getName() + ".");
            return;
        }
        if (playerStatus.equals("accepted") && targetStatus.equals("accepted")){
            player.sendMessage(PENDING + "You are currently friends with " + targetPlayer.getName() + ". To remove them, please use the command /friend remove <name>.");
            return;
        }

        plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "rejected", getTime());
        plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "rejected", getTime());

        player.sendMessage(REJECT + "You rejected " + targetPlayer.getName() + "'s friend request.");
    }

    private boolean checkIfPendingRequest(UUID playerUUID, UUID targetUUID){
        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID.toString(), targetUUID.toString());
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString());

        if(playerStatus == null){
            return false;
        }

        if(targetStatus == null){
            return false;
        }

        if(playerStatus.contains("pending") && targetStatus.contains("receivedrequest")){
            return true;
        } else {
            return false;
        }
    }

    private void acceptFriend(UUID playerUUID, UUID targetUUID){
        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID.toString(), targetUUID.toString());
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID.toString(), playerUUID.toString());

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        Player player = Bukkit.getPlayer(playerUUID);

        plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "accepted", getTime());
        plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "accepted", getTime());

        player.sendMessage(ACCEPT + "You are now friends with " + targetPlayer.getName() + ".");
        if(targetPlayer.isOnline()){
            Player targetOnline = Bukkit.getPlayer(targetUUID);
            targetOnline.sendMessage(ACCEPT + "You are now friends with " + player.getName() + ".");
        }
    }

    public void sendFriendList(Player player, boolean includePending, boolean includeCurrent){
        ArrayList<String> friendListStringUUID = new ArrayList<>();
        friendListStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted"));

        ArrayList<String> friendListPendingStringUUID = new ArrayList<>();
        friendListPendingStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "receivedrequest"));

        if(includePending) {
            if(friendListPendingStringUUID.contains(Errors.noFriendsFound())){
                friendListPendingStringUUID.clear();
            }else {
                player.sendMessage(PENDING + "Pending friend requests:");
                for (String friendPendingList : friendListPendingStringUUID) {
                    OfflinePlayer offlinePendingPlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendPendingList));
                    player.sendMessage(PENDING + offlinePendingPlayer.getName());
                }
            }
        }

        if(includeCurrent) {
            if(friendListStringUUID.contains(Errors.noFriendsFound())){
                player.sendMessage(Errors.noFriendsFound());
                friendListStringUUID.clear();
                return;
            }
            player.sendMessage(ACCEPT + "Current friends:");
            for (String friendList : friendListStringUUID) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendList));
                if (offlinePlayer.isOnline()) {
                    player.sendMessage(ACCEPT + "" + ChatColor.GREEN + offlinePlayer.getName());
                } else {
                    player.sendMessage(ACCEPT + offlinePlayer.getName());
                }
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
