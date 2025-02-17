package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO {
    /*
     * inserts account into database using requested username and password
     * returns whether insertion was successful
     */
    public boolean insertAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated == 1;
        } catch(SQLException e) {   
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
     * finds account based on unique username and correct associated password
     * returns Account including account_id or null if unsuccessful 
     */
    public Account getAccountByUsernamePassword(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new Account(account_id, username, password);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * inserts message with requested message_text, time_posted_epoch and posted_by Account id 
     * returns message_id of inserted message or -1 if unsuccessful
     */
    public int insertMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int message_id = rs.getInt("message_id");
                return message_id;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /*
     * queries database to find all messages 
     * returns list of Messages that is empty if none are found
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        try {
            Statement s = conn.createStatement();
            String sql = "SELECT * FROM message";
            ResultSet rs = s.executeQuery(sql);

            List<Message> messages = new ArrayList<>();

            while(rs.next()) {
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }

            return messages;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * uses input message_id to find a particular Message
     * returns found Message or null if unsuccessful
     */
    public Message getMessageByID(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                return new Message(message_id, posted_by, message_text, time_posted_epoch);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * uses message_id to delete a particular message 
     * returns whether deletion is successful
     */
    public boolean deleteMessageByID(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated == 1;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
     * find particular Message by message_id and updates it with input message_text
     * returns whether update was successful
     */
    public boolean updateMessageByID(String message_text, int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated == 1;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
     * uses account_id to find list of messages associated with user
     * return list of Messages that is empty if none are found
     */
    public List<Message> getMessagesByAccountID(int account_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message m JOIN account a ON m.posted_by = a.account_id WHERE a.account_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            List<Message> messages = new ArrayList<>();

            while(rs.next()) {
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                messages.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }

            return messages;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
