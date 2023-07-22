package me.codingcookie.friendsplus.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import static org.bukkit.ChatColor.*;

import org.bukkit.entity.Player;

public class LinkUtil {

     public void acceptRejectBlock(Player player, String pretext, String args){
         TextComponent pre = new TextComponent(pretext);
         TextComponent accept = new TextComponent(GREEN + " " + BOLD + "ACCEPT");
         TextComponent reject = new TextComponent(RED + " " + BOLD + "REJECT");

         accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + args));
         accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(GREEN + "ACCEPT")));

         reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend reject " + args));
         reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(RED + "REJECT")));

         BaseComponent message = new TextComponent("");
         message.addExtra(pre);
         message.addExtra(accept);
         message.addExtra(reject);

         player.spigot().sendMessage(message);
    }

}
