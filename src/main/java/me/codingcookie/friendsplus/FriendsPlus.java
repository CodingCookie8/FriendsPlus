package me.codingcookie.friendsplus;

import me.codingcookie.friendsplus.commands.CommandAddFriend;
import me.codingcookie.friendsplus.commands.CommandFriends;
import me.codingcookie.friendsplus.commands.CommandRejectFriend;
import me.codingcookie.friendsplus.events.PlayerJoinEvent;
import me.codingcookie.friendsplus.events.PlayerKickEvent;
import me.codingcookie.friendsplus.events.PlayerQuitEvent;
import me.codingcookie.friendsplus.utils.sqliteutil.Database;
import me.codingcookie.friendsplus.utils.sqliteutil.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendsPlus extends JavaPlugin {

    private Database db;

    @Override
    public void onEnable(){
        CommandExecutor fExecutor = new CommandFriends(this);
        CommandExecutor afExecutor = new CommandAddFriend(this);
        CommandExecutor rfExecutor = new CommandRejectFriend(this);
        getCommand("friends").setExecutor(fExecutor);
        getCommand("addfriend").setExecutor(afExecutor);
        getCommand("rejectfriend").setExecutor(rfExecutor);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKickEvent(this), this);

        this.db = new SQLite(this);
        this.db.load();

    }

    @Override
    public void onDisable(){

    }

    public Database getFriendDatabase() {
        return this.db;
    }
}
