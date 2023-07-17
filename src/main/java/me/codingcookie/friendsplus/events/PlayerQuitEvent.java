package me.codingcookie.friendsplus.events;

import me.codingcookie.friendsplus.FriendsPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    private final FriendsPlus plugin;
    public PlayerQuitEvent(FriendsPlus plugin){
        this.plugin = plugin;
    }

    PlayerJoinEvent pJE;

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event){
        Player player = event.getPlayer();
        pJE = new PlayerJoinEvent(plugin);

        pJE.notifyFriends(player, false);
    }

}