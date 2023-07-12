package me.codingcookie.friendsplus.commands;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.utils.CommandFriendUtils;
import me.codingcookie.friendsplus.utils.MsgLevels;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFriends implements CommandExecutor, MsgLevels {

    private final FriendsPlus plugin;
    CommandFriendUtils utils;

    public CommandFriends(FriendsPlus plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("This command can only be run in game!");
            return true;
        }

        Player player = (Player) sender;
        utils = new CommandFriendUtils(plugin);

        if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("list"))){
            utils.sendFriendList(player);
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("add")){
                player.sendMessage(PENDING + "Please specify the player you want to friend.");
                return true;
            }if(args[0].equalsIgnoreCase("reject") ||
                    args[0].equalsIgnoreCase("decline") ||
                    args[0].equalsIgnoreCase("deny")){
                player.sendMessage(PENDING + "Please specify the player you want to reject.");
                return true;
            } else{
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                if(targetPlayer==null){
                    player.sendMessage(REJECT + "This player doesn't exist.");
                    return true;
                }
                utils.setFriendStatus(player.getUniqueId(), targetPlayer.getUniqueId(), "pending");
                return true;
            }
        }

        if(args.length == 2){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            if(targetPlayer==null){
                player.sendMessage(REJECT + "This player doesn't exist.");
                return true;
            }
            if(args[0].equalsIgnoreCase("add")){
                utils.setFriendStatus(player.getUniqueId(), targetPlayer.getUniqueId(), "pending");
                return true;
            }
            if(args[0].equalsIgnoreCase("reject") ||
                    args[0].equalsIgnoreCase("decline") ||
                    args[0].equalsIgnoreCase("deny")){
                plugin.getFriendDatabase().delFriend(player.getUniqueId().toString(), targetPlayer.getUniqueId().toString());

                //TODO: Implement blocking
                player.sendMessage(REJECT + "You rejected the friend request from " + targetPlayer.getName() + ".");
                if(targetPlayer.isOnline()){
                    Player targetOnline = Bukkit.getPlayer(args[1]);
                    targetOnline.sendMessage(REJECT + player.getName() + " rejected your friend request.");
                }
                return true;
            }
        }


        return true;
    }

}
