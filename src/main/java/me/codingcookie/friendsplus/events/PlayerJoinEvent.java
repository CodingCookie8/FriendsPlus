package me.codingcookie.friendsplus.events;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.CommandFriendUtils;
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

    CommandFriendUtils utils;

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        Player player = event.getPlayer();
        utils = new CommandFriendUtils(plugin);

        utils.sendFriendList(player, true, false);

        notifyFriends(player, true);
    }

    public void notifyFriends(Player player, boolean joined){
        ArrayList<String> friendListStringUUID = new ArrayList<>();
        friendListStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted"));

        if(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted").contains(Errors.noFriendsFound())){
            return;
        }

        for (String friendList : friendListStringUUID) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(friendList));
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

        friendListStringUUID.clear();
    }

}
