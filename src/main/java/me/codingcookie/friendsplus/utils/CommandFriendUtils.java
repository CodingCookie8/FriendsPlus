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

import static org.bukkit.ChatColor.*;

public class CommandFriendUtils {

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
                Messages.ALREADY_FRIENDS.sendMessage(player, targetPlayer.getName());
                return;
            }
            if(playerStatus.equals("pending")){
                Messages.ALREADY_SENT_FR.sendMessage(player, targetPlayer.getName());
                return;
            }
        }

        if(checkIfPendingRequest(targetUUID, playerUUID)){
            acceptFriend(playerUUID, targetUUID);
        } else {
            plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "pending", getTime());
            plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "receivedrequest", getTime());
            Messages.FR_SENT.sendMessage(player, targetPlayer.getName());
            if(targetPlayer.isOnline()){
                Player targetOnline = Bukkit.getPlayer(targetUUID);
                Messages.FR_RECEIVED.sendMessage(targetOnline, player.getName());
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
            Messages.NO_PENDING_FR.sendMessage(player, targetPlayer.getName());
            return;
        }
        if (playerStatus.equals("accepted") && targetStatus.equals("accepted")){
            Messages.ALREADY_FRIENDS.sendMessage(player, targetPlayer.getName());
            Messages.REMOVE_FRIEND.sendMessage(player);
            return;
        }

        plugin.getFriendDatabase().setFriend(playerUUID.toString(), targetUUID.toString(), "rejected", getTime());
        plugin.getFriendDatabase().setFriend(targetUUID.toString(), playerUUID.toString(), "rejected", getTime());

        Messages.FR_REJECT.sendMessage(player, targetPlayer.getName());
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

        Messages.FR_ACCEPT.sendMessage(player, targetPlayer.getName());
        if(targetPlayer.isOnline()){
            Player targetOnline = Bukkit.getPlayer(targetUUID);
            Messages.FR_ACCEPT.sendMessage(targetOnline, player.getName());
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
                Messages.PENDING_FRIENDS_HEADER.sendMessage(player);
                for (String friendPendingList : friendListPendingStringUUID) {
                    OfflinePlayer offlinePendingPlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendPendingList));
                    player.sendMessage(GRAY + offlinePendingPlayer.getName());
                }
                player.sendMessage("");
            }
        }

        if(includeCurrent) {
            if(friendListStringUUID.contains(Errors.noFriendsFound())){
                player.sendMessage(Errors.noFriendsFound());
                friendListStringUUID.clear();
                return;
            }
            Messages.CURRENT_FRIENDS_HEADER.sendMessage(player);
            for (String friendList : friendListStringUUID) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendList));
                if (offlinePlayer.isOnline()) {
                    player.sendMessage(GREEN + offlinePlayer.getName());
                } else {
                    player.sendMessage(RED + offlinePlayer.getName());
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
            Messages.NO_PLAYER.sendMessage(player);
            return false;
        }

        if(targetPlayer.isBanned()){
            Messages.CANNOT_FRIEND.sendMessage(player);
            return false;
        }
        if(player == targetPlayer){
            Messages.CANNOT_FRIEND.sendMessage(player);
            return false;
        }
        if(!targetPlayer.hasPlayedBefore()){
            Messages.NEVER_JOINED.sendMessage(player);
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
