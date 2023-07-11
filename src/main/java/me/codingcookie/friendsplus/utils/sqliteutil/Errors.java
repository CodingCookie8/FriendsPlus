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

}
