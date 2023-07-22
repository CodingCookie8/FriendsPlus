package me.codingcookie.friendsplus.utils.sqliteutil;

import me.codingcookie.friendsplus.FriendsPlus;
import org.bukkit.Bukkit;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public abstract class Database {

    //TODO: Data Normalization
    /*
    TABLE 1
    PLAYER_ID       PLAYER_UUID
    1               uuid1
    2               uuid2
    3               uuid3

    TABLE 2
    PLAYER_ID       FRIEND_UUID
    1               frienduuid1
    1               frienduuid2
    2               frienduuid1
    2               frienduuid2
    3               frienduuid1
     */

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

    public UUID getFriendListUUID(UUID playerUUID, String status){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid =? AND status =?;");
            ps.setBytes(1, asBytes(playerUUID));
            ps.setString(2, status);

            rs = ps.executeQuery();
            while(rs.next()){
                if(Arrays.equals(rs.getBytes("player_uuid"), asBytes(playerUUID))){
                    UUID u = asUuid(rs.getBytes("friend_uuid"));
                    return u;
                }
            }
            return new UUID( 0 , 0 );
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

    public String getFriendStatus(UUID playerUUID, UUID targetUUID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid =? AND friend_uuid =?;");
            ps.setBytes(1, asBytes(playerUUID));
            ps.setBytes(2, asBytes(targetUUID));
            rs = ps.executeQuery();

            while(rs.next()){
                if(Arrays.equals(rs.getBytes("player_uuid"), asBytes(playerUUID))
                        && Arrays.equals(rs.getBytes("friend_uuid"), asBytes(targetUUID))){
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

    public void setFriend(UUID playerUUID, UUID friendUUID, String status, String dateFriended){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if(status.equals("pending") || (status.equals("receivedrequest"))) {
                conn = getSQLConnection();
                ps = conn.prepareStatement("INSERT INTO " + table + " (player_uuid,friend_uuid,status,date_friended) VALUES(?,?,?,?)");
                ps.setBytes(1, asBytes(playerUUID));
                ps.setBytes(2, asBytes(friendUUID));
                ps.setString(3, status);
                ps.setString(4, dateFriended);
                ps.executeUpdate();
                return;
            }
            if(status.equals("accepted") || status.equals("rejected") || status.equals("blocked")){
                conn = getSQLConnection();
                ps = conn.prepareStatement("REPLACE INTO " + table + " (player_uuid,friend_uuid,status,date_friended) VALUES(?,?,?,?)");
                ps.setBytes(1, asBytes(playerUUID));
                ps.setBytes(2, asBytes(friendUUID));
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

    public void delFriend(UUID playerUUID, UUID friendUUID){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM " + table + " WHERE player_uuid =? AND friend_uuid =?;");
            ps.setBytes(1, asBytes(playerUUID));
            ps.setBytes(2, asBytes(friendUUID));
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

    static UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}