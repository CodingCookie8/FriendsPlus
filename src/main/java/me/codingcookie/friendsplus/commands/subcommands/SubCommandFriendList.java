package me.codingcookie.friendsplus.commands.subcommands;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.sqliteutil.Errors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SubCommandFriendList {

    private final FriendsPlus plugin;
    public SubCommandFriendList(FriendsPlus plugin){
        this.plugin = plugin;
    }

    public void sendFriendList(Player player){

        ArrayList<String> friendListStringUUID = new ArrayList<>();
        friendListStringUUID.add(plugin.getFriendDatabase().getFriendListUUID(player.getUniqueId().toString(), "accepted"));

        if(friendListStringUUID.contains(Errors.noFriendsFound())){
            player.sendMessage(Errors.noFriendsFound());
            friendListStringUUID.clear();
            return;
        }

        ArrayList<UUID> friendListUUID = new ArrayList<>();
        for(String stringUUID : friendListStringUUID) {
            friendListUUID.add(UUID.fromString(stringUUID));
        }

        ArrayList<OfflinePlayer> friendOfflinePlayer = new ArrayList<>();
        for(UUID friendUUID : friendListUUID){
            friendOfflinePlayer.add(Bukkit.getOfflinePlayer(friendUUID));
        }

        for(OfflinePlayer friendList : friendOfflinePlayer){
            String name = friendList.getName();
            player.sendMessage(name);
        }

        friendListStringUUID.clear();
        friendListUUID.clear();
        friendOfflinePlayer.clear();

        /*
        map = new FriendMap();

        if(map.getFriendMap(player.getUniqueId()) == null){
            player.sendMessage("You don't have any friends!");
            return;
        }
        if(map.getFriendMap(player.getUniqueId()).isEmpty()){
            player.sendMessage("You don't have any friends!");
            return;
        }

        ArrayList<OfflinePlayer> friendUUIDList = new ArrayList<>();

        for(UUID friendUUIDs : map.getFriendMap(player.getUniqueId())){
            friendUUIDList.add(Bukkit.getOfflinePlayer(friendUUIDs));
        }

        //TODO: Still have to use this though, lol
        for(OfflinePlayer friendList : friendUUIDList) {
            String name = friendList.getName();
            player.sendMessage(name);
        }*/

    }
}
