package me.codingcookie.friendsplus.utils.sqliteutil;

import org.bukkit.ChatColor;

public class Errors {

    public static String sqlConnectionExecute(){
        return "Couldn't execute MySQL statement: ";
    }
    public static String sqlConnectionClose(){
        return "Failed to close MySQL connection: ";
    }
    public static String noSQLConnection(){
        return "Unable to retreive MYSQL connection: ";
    }
    public static String noTableFound(){
        return "Database Error: No Table Found";
    }

    public static String noFriendsFound(){
        //TODO: Make this customizable
        return ChatColor.RED + "> " + ChatColor.GRAY + "You don't have any friends!";
    }

    public static String noTargetPending() { return "Error 1: Player had a pending friend request but the target didn't. This shouldn't have happened, but the issue has been fixed."; }

    public static String playerReturnedNull() { return "Error 2: Player returned null."; }
}
