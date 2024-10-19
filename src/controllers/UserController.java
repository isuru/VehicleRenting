
package controllers;

import dbpkg.DB;
import models.User;
import javax.swing.DefaultListModel;
import java.sql.*;

public class UserController {
    public static Integer currentUserId;
    public static String currentUser;
    public static String ERROR_MESSAGE;
    
    public static boolean login(String un, String pw) {
        User user = User.findBy("username", un);
        
        System.out.println(un);

        if (null == user) {
            ERROR_MESSAGE = "Couldn't find the Username";
            
        } else if (user.authenticate(pw)) {
            currentUserId = user.getId();
            currentUser = user.getUsername(); 
            return true;
            
        } else {
            ERROR_MESSAGE = "Invalid password!";
        }
        
        return false;
    }
    
    public static void logout() {
        currentUserId = 0;
    }
    
    public static boolean changePassword(String un, String newpw) {
        User user = User.findBy("username", un);
        return user.changePassword(newpw);
    }
    
    public static String[] loadUser(String un) {
        String[] record = new String[3];
        User user = User.findBy("username", un);
        
        if (null != user) {
            record[0] = user.getUsername();
            record[1] = user.getCreatedAt();
            record[2] = user.getLastLoginAt();

            return record;
        }
        
        return null;
    }
    
    public static DefaultListModel getAllUsers() {
        DefaultListModel model = new DefaultListModel();
        ResultSet rs = User.all();
        
        try {
            while(rs.next()) {
                model.addElement(rs.getString("username"));
            }
        
        } catch (SQLException e) {
            ERROR_MESSAGE = e.getMessage();
            
        } finally {
            DB.closeConnection();
        }
        
        return model;
    }

    public static boolean addNewUser(String un, String pw, String rpw) {
        User user = new User(un, pw);
        return user.create();
    }
}
