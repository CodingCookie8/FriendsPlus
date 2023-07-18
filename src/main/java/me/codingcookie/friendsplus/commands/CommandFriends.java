package me.codingcookie.friendsplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.CommandFriendUtils;
import me.codingcookie.friendsplus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("friends|friend|f")
public class CommandFriends extends BaseCommand {

    private final FriendsPlus plugin;
    CommandFriendUtils utils;

    public CommandFriends(FriendsPlus plugin){
        this.plugin = plugin;
    }

    @Default
    @CommandPermission("friend.list")
    public void onDefault(Player player){
        utils = new CommandFriendUtils(plugin);
        utils.sendFriendList(player, true, true);
    }

    @Subcommand("add")
    @CommandAlias("fadd|addfriend|friendadd")
    @CommandPermission("friend.add")
    public void onAdd(Player player, Player target, String[] args){
        utils = new CommandFriendUtils(plugin);
        if(args.length == 0){
            Messages.SPECIFY_PLAYER.sendMessage(player, "friend");
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("add")){
            Messages.SPECIFY_PLAYER.sendMessage(player, "friend");
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
    public void onReject(Player player, Player target, String[] args){
        utils = new CommandFriendUtils(plugin);
        if(args.length == 0){
            Messages.SPECIFY_PLAYER.sendMessage(player, "reject");
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("reject")){
            Messages.SPECIFY_PLAYER.sendMessage(player, "reject");
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

}
