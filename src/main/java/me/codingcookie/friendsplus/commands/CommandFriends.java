package me.codingcookie.friendsplus.commands;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.commands.subcommands.SubCommandFriendAdd;
import me.codingcookie.friendsplus.commands.subcommands.SubCommandFriendList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFriends implements CommandExecutor {

    private final FriendsPlus plugin;
    SubCommandFriendList subCommandFriendList;
    SubCommandFriendAdd subCommandFriendAdd;

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
        subCommandFriendList = new SubCommandFriendList(plugin);
        subCommandFriendAdd = new SubCommandFriendAdd(plugin);

        if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("list"))){
            subCommandFriendList.sendFriendList(player);
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("add")){
            player.sendMessage("Who do you want to add as a friend?");
        }else if(args.length == 1){
            subCommandFriendAdd.checkAndAddFriend(player, args[0], "pending");
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("add")){
            subCommandFriendAdd.checkAndAddFriend(player, args[1], "pending");
        }

        return true;
    }

}
