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
            utils.sendFriendList(player, true, true);
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
            } else {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                utils.sendFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
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
                utils.sendFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
                return true;
            }
            if(args[0].equalsIgnoreCase("reject") ||
                    args[0].equalsIgnoreCase("decline") ||
                    args[0].equalsIgnoreCase("deny")){
                utils.rejectFriendRequest(player.getUniqueId(), targetPlayer.getUniqueId());
                return true;
            }
        }


        return true;
    }

}
