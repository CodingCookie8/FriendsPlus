package me.codingcookie.friendsplus;

import co.aikar.commands.BukkitCommandManager;
import me.codingcookie.friendsplus.commands.CommandFriends;
import me.codingcookie.friendsplus.events.PlayerJoinEvent;
import me.codingcookie.friendsplus.events.PlayerKickEvent;
import me.codingcookie.friendsplus.events.PlayerQuitEvent;
import me.codingcookie.friendsplus.utils.sqliteutil.Database;
import me.codingcookie.friendsplus.utils.sqliteutil.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendsPlus extends JavaPlugin {

    private Database db;
    private CommandFriends commandFriends;

    @Override
    public void onEnable(){

        commandFriends = new CommandFriends(this);
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(commandFriends);

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
