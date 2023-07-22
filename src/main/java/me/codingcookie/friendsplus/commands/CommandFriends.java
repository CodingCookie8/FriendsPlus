package me.codingcookie.friendsplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.FriendUtil;
import me.codingcookie.friendsplus.utils.LinkUtil;
import me.codingcookie.friendsplus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("friends|friend|f")
public class CommandFriends extends BaseCommand {

    private final FriendsPlus plugin;
    FriendUtil utils;

    public CommandFriends(FriendsPlus plugin){
        this.plugin = plugin;
    }

    @Default
    @CommandPermission("friend.list")
    public void onDefault(Player player){
        utils = new FriendUtil(plugin);
        utils.sendFriendList(player, true, true);
    }

    @Subcommand("add")
    @CommandAlias("fadd|addfriend|friendadd")
    @CommandPermission("friend.add")
    public void onAdd(Player player, String[] args){
        utils = new FriendUtil(plugin);
        if(!specifyPlayer(player, args, "friend", "add")){
            return;
        }
        if(args.length == 1){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            utils.sendFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
            return;
        }
        if(args.length == 2){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            utils.sendFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
        }
    }

    @Subcommand("reject")
    @CommandAlias("freject|rejectfriend|friendreject")
    @CommandPermission("friend.reject")
    public void onReject(Player player, String[] args){
        utils = new FriendUtil(plugin);
        if(!specifyPlayer(player, args, "reject", "reject")){
            return;
        }
        if(args.length == 1){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            utils.rejectFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
            return;
        }
        if(args.length == 2){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            utils.rejectFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
        }
    }

    @Subcommand("remove")
    @CommandAlias("fremove|removefriend|friendremove")
    @CommandPermission("friend.remove")
    public void onRemove(Player player, String[] args){
        utils = new FriendUtil(plugin);
        if(!specifyPlayer(player, args, "remove", "remove")){
            return;
        }
        if(args.length == 1){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            utils.removeFriend(player.getUniqueId(), targetPlayer.getUniqueId());
            return;
        }
        if(args.length == 2){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            utils.removeFriend(player.getUniqueId(), targetPlayer.getUniqueId());
        }
    }

    boolean specifyPlayer(Player player, String[] args, String placeholder, String command){
        if(args.length == 0){
            Messages.SPECIFY_PLAYER.sendMessage(player, placeholder);
            return false;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase(command)){
            Messages.SPECIFY_PLAYER.sendMessage(player, placeholder);
            return false;
        }
        return true;
    }

}
