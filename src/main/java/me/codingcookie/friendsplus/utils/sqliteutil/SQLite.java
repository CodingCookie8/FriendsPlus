package me.codingcookie.friendsplus.utils.sqliteutil;

import me.codingcookie.friendsplus.FriendsPlus;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {

    String dbname;

    public SQLite(FriendsPlus instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "friend_list");
    }

    public String SQLiteCreateFriendsTable = "CREATE TABLE IF NOT EXISTS friend_list (" + // make sure to put your table name in here too.
            "`player_uuid` binary(16) NOT NULL," +
            "`friend_uuid` binary(16) NOT NULL," +
            "`status` varchar(32) NOT NULL," +
            "`date_friended` varchar(32) NOT NULL," +
            "PRIMARY KEY (`player_uuid`)" +  // This is creating 3 colums Player, Kills, Total. Primary key is what you are going to use as your indexer. Here we want to use player so
            ");"; // we can search by player, and get kills and total. If you some how were searching kills it would provide total and player.


    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.getParentFile().exists()) {
            dataFolder.getParentFile().mkdirs();
        }
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "No SQLite JBDC library.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateFriendsTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

}
