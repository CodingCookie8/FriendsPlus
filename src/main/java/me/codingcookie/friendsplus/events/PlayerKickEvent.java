package me.codingcookie.friendsplus.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerKickEvent implements Listener {

    @EventHandler
    public void onPlayerKick(org.bukkit.event.player.PlayerKickEvent event){
        Player player = event.getPlayer();
    }

}
