package me.codingcookie.friendsplus.utils.sqliteutil;

import me.codingcookie.friendsplus.FriendsPlus;

import java.util.logging.Level;

public class Error {

    public static void execute(FriendsPlus plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(FriendsPlus plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }

}
