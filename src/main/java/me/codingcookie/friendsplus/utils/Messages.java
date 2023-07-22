package me.codingcookie.friendsplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public enum Messages{
    RUN_IN_GAME("This command must be run in game."),
    NO_PERMISSION(GRAY + "> " + RED + "You don't have permission to run this command."),
    NO_PLAYER(GRAY + "> " + RED + "This player doesn't exist."),
    CANNOT_FRIEND(GRAY + "> " + RED + "You are unable to friend this player."),
    NEVER_JOINED(GRAY + "> " + RED + "You can't friend players that haven't joined the server before."),
    SPECIFY_PLAYER(GRAY + "> " + YELLOW + "Please specify the player you want to %placeholder%."),
    ALREADY_FRIENDS(GRAY + "> " + GREEN + "You are already friends with %placeholder%."),
    ALREADY_SENT_FR(GRAY + "> " + YELLOW + "You have already sent a friend request to %placeholder%."),
    FR_SENT(GRAY + "> " + YELLOW + "You have sent a friend request to %placeholder%."),
    NOT_FRIENDS(GRAY + "> " + GREEN + "You are not friends with %placeholder%."),
    NO_PENDING_FR(GRAY + "> " + RED + "There is no pending friend request between you and %placeholder%."),
    REMOVE_FRIEND(GRAY + "> " + RED + "You removed %placeholder% as a friend."),
    FR_REJECT(GRAY + "> " + RED + "You rejected %placeholder%'s friend request."),
    FR_ACCEPT(GRAY + "> " + GREEN + "You are now with friends with %placeholder%."),
    FRIEND_QUIT(GRAY + "> " + RED + "Your friend, %placeholder%, quit the game."),
    FRIEND_JOINED(GRAY + "> " + GREEN + "Your friend, %placeholder%, joined the game."),
    PENDING_FRIENDS_HEADER(YELLOW + "" + BOLD + "PENDING FRIEND REQUESTS:"),
    CURRENT_FRIENDS_HEADER(GREEN + "" + BOLD + "CURRENT FRIENDS:"),
    SQL_NO_EXECUTE("Couldn't execute MySQL statement: "),
    SQL_NO_CLOSE("Failed to close MySQL connection: "),
    SQL_NO_RETRIEVE("Unable to retreive MYSQL connection: "),
    SQL_NO_TABLE("Database Error: No Table Found");

    private String message;

    Messages(String message){
        this.message = message;
    }

    public void sendMessage(Player player){
        player.sendMessage(message);
    }

    public void sendMessage(CommandSender sender){
        sender.sendMessage(message);
    }

    public void sendMessage(Player player, String placeholder){
        player.sendMessage(message.replace("%placeholder%", placeholder));
    }

    public void broadcastMessage(){
        Bukkit.broadcastMessage(message);
    }

    public void logMessage(){
        System.out.println(message);
    }
}
