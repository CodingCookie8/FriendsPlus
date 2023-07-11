package me.codingcookie.friendsplus.commands;

import me.codingcookie.friendsplus.FriendsPlus;
import me.codingcookie.friendsplus.commands.subcommands.SubCommandFriendAdd;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAddFriend implements CommandExecutor {

    private final FriendsPlus plugin;
    public CommandAddFriend(FriendsPlus plugin){
        this.plugin = plugin;
    }

    SubCommandFriendAdd subCommandFriendAdd;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("This command can only be run in game!");
            return true;
        }

        Player player = (Player) sender;
        subCommandFriendAdd = new SubCommandFriendAdd(plugin);

        if(args.length == 0){
            player.sendMessage("Who do you want to add as a friend?");
        }
        if(args.length == 1){
            subCommandFriendAdd.checkAndAddFriend(player, args[0], "pending");
        }
        return true;
    }
}
