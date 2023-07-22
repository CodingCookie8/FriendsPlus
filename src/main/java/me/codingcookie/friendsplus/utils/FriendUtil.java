package me.codingcookie.friendsplus.utils;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.sqliteutil.Errors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.ChatColor.*;

public class FriendUtil {

    private final FriendsPlus plugin;
    public FriendUtil(FriendsPlus plugin){
        this.plugin = plugin;
    }

    private String playerStatus;
    private String targetStatus;

    public void sendFriendRequest(UUID playerUUID, UUID targetUUID) {
        if (!checks(playerUUID, targetUUID)) {
            return;
        }

        Player player = Bukkit.getPlayer(playerUUID);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);

        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID, targetUUID);
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID, playerUUID);

        if (targetStatus != null) {
            if (playerStatus.equals("accepted") && targetStatus.equals("accepted")) {
                Messages.ALREADY_FRIENDS.sendMessage(player, targetPlayer.getName());
                return;
            }
            if (playerStatus.equals("pending")) {
                Messages.ALREADY_SENT_FR.sendMessage(player, targetPlayer.getName());
                return;
            }
        }

        if(checkIfPendingRequest(targetUUID, playerUUID)){
            acceptFriend(playerUUID, targetUUID);
        } else {
            plugin.getFriendDatabase().setFriend(playerUUID, targetUUID, "pending", getTime());
            plugin.getFriendDatabase().setFriend(targetUUID, playerUUID, "receivedrequest", getTime());
            Messages.FR_SENT.sendMessage(player, targetPlayer.getName());
            if (targetPlayer.isOnline()) {
                Player targetOnline = Bukkit.getPlayer(targetUUID);
                LinkUtil linkUtil = new LinkUtil();
                linkUtil.acceptRejectBlock(targetOnline, GRAY + "> " + YELLOW + player.getName() + " sent you a friend request.", player.getName());
            }
        }
    }

     void acceptFriend(UUID playerUUID, UUID targetUUID){
        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID, targetUUID);
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID, playerUUID);

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        Player player = Bukkit.getPlayer(playerUUID);

        plugin.getFriendDatabase().setFriend(playerUUID, targetUUID, "accepted", getTime());
        plugin.getFriendDatabase().setFriend(targetUUID, playerUUID, "accepted", getTime());

        Messages.FR_ACCEPT.sendMessage(player, targetPlayer.getName());
        if(targetPlayer.isOnline()){
            Player targetOnline = Bukkit.getPlayer(targetUUID);
            Messages.FR_ACCEPT.sendMessage(targetOnline, player.getName());
        }
    }

    public void rejectFriendRequest(UUID playerUUID, UUID targetUUID){
        if(!checks(playerUUID, targetUUID)){
            return;
        }

        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID, targetUUID);
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID, playerUUID);

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

        plugin.getFriendDatabase().delFriend(playerUUID, targetUUID);
        plugin.getFriendDatabase().delFriend(targetUUID, playerUUID);
        Messages.FR_REJECT.sendMessage(player, targetPlayer.getName());
    }

    private boolean checkIfPendingRequest(UUID playerUUID, UUID targetUUID){
        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID, targetUUID);
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID, playerUUID);

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

    public void removeFriend(UUID playerUUID, UUID targetUUID){
        playerStatus = plugin.getFriendDatabase().getFriendStatus(playerUUID, targetUUID);
        targetStatus = plugin.getFriendDatabase().getFriendStatus(targetUUID, playerUUID);

        if(!checks(playerUUID, targetUUID)){
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
        Player player = Bukkit.getPlayer(playerUUID);

        if(playerStatus == null || targetStatus == null){
            Messages.NOT_FRIENDS.sendMessage(player, targetPlayer.getName());
            return;
        }

        plugin.getFriendDatabase().delFriend(playerUUID, targetUUID);
        plugin.getFriendDatabase().delFriend(targetUUID, playerUUID);
        Messages.REMOVE_FRIEND.sendMessage(player, targetPlayer.getName());

    }

    public void sendFriendList(Player player, boolean includePending, boolean includeCurrent){
        ArrayList<UUID> friendListUUID = new ArrayList<>();
        friendListUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId(), "accepted"));

        ArrayList<UUID> friendListPendingUUID = new ArrayList<>();
        friendListPendingUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId(), "receivedrequest"));

        UUID uuidBlank = new UUID( 0 , 0 );

        if(includePending) {
            if(friendListPendingUUID.contains(uuidBlank)){
                friendListPendingUUID.clear();
            }else {
                Messages.PENDING_FRIENDS_HEADER.sendMessage(player);
                for (UUID friendPendingList : friendListPendingUUID) {
                    OfflinePlayer offlinePendingPlayer = Bukkit.getOfflinePlayer(friendPendingList);
                    player.sendMessage(GRAY + offlinePendingPlayer.getName());
                }
                player.sendMessage("");
            }
        }

        if(includeCurrent) {
            if(friendListUUID.contains(uuidBlank)){
                player.sendMessage(Errors.noFriendsFound());
                friendListUUID.clear();
                return;
            }
            player.sendMessage(" ");
            Messages.CURRENT_FRIENDS_HEADER.sendMessage(player);
            for (UUID friendList : friendListUUID) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friendList);
                if (offlinePlayer.isOnline()) {
                    player.sendMessage(GRAY + "> " + GREEN + offlinePlayer.getName());
                } else {
                    player.sendMessage(GRAY + "> " + offlinePlayer.getName());
                }
            }
        }

        friendListUUID.clear();
        friendListPendingUUID.clear();
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
