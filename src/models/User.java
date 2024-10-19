
package models;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import dbpkg.DB;
import java.sql.*;


public class User {
    private int id;
    private String username, password;
    private String createdAt, lastLoginAt;
    
    public static int currentUserId=1;
    public static String ERROR_MESSAGE;
    
    public User(String un) {
        this.username = un;
    }
    
    public User(String un, String pw) {
        this.username = un;
        this.password = pw;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getCreatedAt() {
        return this.createdAt;
    }
    
    public String getLastLoginAt() {
        return this.lastLoginAt;
    }    
    
    public boolean create() {
        try {
            Connection conn = DB.createConnection();
            PreparedStatement st = conn.prepareStatement("INSERT INTO User(username, password, created_user_id) VALUES(?, ?, ?)");
            st.setString(1, this.username);
            st.setString(2, toHash(this.password));
            st.setInt(3, currentUserId);
            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
        }
        
        return false;
    }
    
    public static ResultSet find(int id) {
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE id = " + id;
        
        try {
            Statement st = DB.createConnection().createStatement();
            rs = st.executeQuery(sql);
            return rs;
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            
        } finally {
            DB.closeConnection();
        }
        
        return null;
    }
    
    public static User findBy(String field, String val) {
        String sql = "SELECT * FROM User WHERE " + field + " = '" + val + "'";
        
        try {
            Statement st = DB.createConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);         
            
            if (rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("password"));
                user.id = rs.getInt("id");
                user.username = rs.getString("username");
                user.createdAt = " ...";
                user.lastLoginAt = " ...";
                return user;
            }
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            System.err.println(e.getMessage());
            
        }
        
        return null;
    }
    
    public static ResultSet all() {
        ResultSet rs;
        String sql = "SELECT * FROM User";
        
        try {
            Statement st = DB.createConnection().createStatement();
            rs = st.executeQuery(sql);
            return rs;
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            System.err.println(e.getMessage());
            
        }
        
        return null;
    }    
    
    public static ResultSet where(String condition) {
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE " + condition;
        
        try {
            Statement st = DB.createConnection().createStatement();
            rs = st.executeQuery(sql);
            return rs;
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            
        }
        
        return null;
    }
    
    public boolean authenticate(String pw) {
        return this.password.equals(toHash(pw));
    }
    
    public boolean changePassword(String newpw) {
        try {
            Connection conn = DB.createConnection();
            PreparedStatement st = conn.prepareStatement("UPDATE User SET password = ? WHERE username = ?");
            st.setString(1, toHash(newpw));
            st.setString(2, this.username);
            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
        }
        
        return false;
    }
    
    public static String toHash(String pw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(pw.getBytes());

            byte[] resultByteArray = digest.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return "";
    }
            
}
