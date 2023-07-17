package me.codingcookie.friendsplus.events;

import me.codingcookie.friendsplus.FriendsPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerKickEvent implements Listener {

    private final FriendsPlus plugin;
    public PlayerKickEvent(FriendsPlus plugin){
        this.plugin = plugin;
    }

    PlayerJoinEvent pJE;

    @EventHandler
    public void onPlayerKick(org.bukkit.event.player.PlayerKickEvent event){
        Player player = event.getPlayer();
        pJE = new PlayerJoinEvent(plugin);

        pJE.notifyFriends(player, false);
    }

}
