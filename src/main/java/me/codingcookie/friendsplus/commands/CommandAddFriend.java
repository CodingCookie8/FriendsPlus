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

public class CommandAddFriend implements CommandExecutor, MsgLevels {

    private final FriendsPlus plugin;
    public CommandAddFriend(FriendsPlus plugin){
        this.plugin = plugin;
    }

    CommandFriendUtils utils;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("This command can only be run in game!");
            return true;
        }

        Player player = (Player) sender;
        utils = new CommandFriendUtils(plugin);

        if(args.length == 0){
            player.sendMessage(PENDING + "Who do you want to add as a friend?");
        }
        if(args.length == 1){
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            if(targetPlayer==null){
                player.sendMessage(REJECT + "This player doesn't exist.");
                return true;
            }
            utils.setFriendStatus(player.getUniqueId(), targetPlayer.getUniqueId(), "pending");
        }
        return true;
    }
}
