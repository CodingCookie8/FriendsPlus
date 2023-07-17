package me.codingcookie.friendsplus.utils.sqliteutil;


import me.codingcookie.friendsplus.FriendsPlus;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import static me.codingcookie.friendsplus.utils.sqliteutil.Error.close;


public abstract class Database {

    FriendsPlus plugin;
    Connection connection;

    public String table = "friend_list";


    public Database(FriendsPlus instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    public String getFriendListUUID(String playerUUIDString, String status){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid = '" + playerUUIDString + "' AND status = '" + status + "';");


            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("player_uuid").equalsIgnoreCase(playerUUIDString.toLowerCase())){
                    return rs.getString("friend_uuid");
                }
            }
            return Errors.noFriendsFound();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public String getFriendStatus(String playerUUIDString, String targetUUIDString){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid = '" + playerUUIDString + "' AND friend_uuid = '" + targetUUIDString + "';");

            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getString("player_uuid").equalsIgnoreCase(playerUUIDString.toLowerCase()) &&
                        rs.getString("friend_uuid").equalsIgnoreCase(targetUUIDString.toLowerCase())){
                    return rs.getString("status");
                }
            }
            return Errors.noFriendsFound();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public void setFriend(String playerUUIDString, String friendUUIDString, String status, String dateFriended){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if(status.equals("pending") || (status.equals("receivedrequest"))) {
                conn = getSQLConnection();
                ps = conn.prepareStatement("INSERT INTO " + table + " (player_uuid,friend_uuid,status,date_friended) VALUES(?,?,?,?)");
                ps.setString(1, playerUUIDString);
                ps.setString(2, friendUUIDString);
                ps.setString(3, status);
                ps.setString(4, dateFriended);
                ps.executeUpdate();
                return;
            }
            if(status.equals("accepted") || status.equals("rejected") || status.equals("blocked")){
                conn = getSQLConnection();
                ps = conn.prepareStatement("REPLACE INTO " + table + " (player_uuid,friend_uuid,status,date_friended) VALUES(?,?,?,?)");
                ps.setString(1, playerUUIDString);
                ps.setString(2, friendUUIDString);
                ps.setString(3, status);
                ps.setString(4, dateFriended);
                ps.executeUpdate();
                return;
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void delFriend(String playerUUIDString, String friendUUIDString){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM " + table + " WHERE player_uuid = " + playerUUIDString + " AND friend_uuid = " + friendUUIDString);
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }

}
