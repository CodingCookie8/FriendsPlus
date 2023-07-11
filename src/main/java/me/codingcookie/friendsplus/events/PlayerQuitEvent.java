package me.codingcookie.friendsplus.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event){
        Player player = event.getPlayer();

    }


}