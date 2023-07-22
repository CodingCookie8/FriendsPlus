package me.codingcookie.friendsplus.events;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.FriendUtil;
import me.codingcookie.friendsplus.utils.Messages;
import me.codingcookie.friendsplus.utils.sqliteutil.Errors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerJoinEvent implements Listener {

    private final FriendsPlus plugin;
    public PlayerJoinEvent(FriendsPlus plugin){
        this.plugin = plugin;
    }

    FriendUtil utils;

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        Player player = event.getPlayer();
        utils = new FriendUtil(plugin);

        utils.sendFriendList(player, true, false);

        notifyFriends(player, true);
    }

    public void notifyFriends(Player player, boolean joined){
        ArrayList<UUID> friendListUUID = new ArrayList<>();
        friendListUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId(), "accepted"));

        UUID uuidBlank = new UUID( 0 , 0 );

        if(friendListUUID.contains(uuidBlank)){
            return;
        }

        for (UUID friendList : friendListUUID) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friendList);
            if(offlinePlayer.isOnline()){
                Player friend = Bukkit.getPlayer(offlinePlayer.getUniqueId());
                if(joined) {
                    Messages.FRIEND_JOINED.sendMessage(friend, player.getName());
                } else {
                    Messages.FRIEND_QUIT.sendMessage(friend, player.getName());
                }
            } else {
                continue;
            }
        }

        friendListUUID.clear();
    }

}
