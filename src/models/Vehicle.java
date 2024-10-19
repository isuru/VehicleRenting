
package models;
import dbpkg.DB;
import java.sql.*;
import java.util.HashMap;

public class Vehicle {
    
    private HashMap<String,Object> params;
    private static String errorMessage;
    
    public static String getErrorMessage() {
       return errorMessage;
    }
    
    public static ResultSet all() {
        ResultSet rs;
        String sql = "SELECT * FROM Vehicle ORDER BY id DESC";
        
        try {
            Statement st = DB.createConnection().createStatement();
            rs = st.executeQuery(sql);
            return rs;
            
        } catch (SQLException e) {
            errorMessage = e.getMessage();            
        }
        
        return null;
    }
    
    public static boolean delete(String vehicleNo) {
        try {
            Connection conn = DB.createConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM Vehicle WHERE vehicle_no = ?");
            st.setString(1, vehicleNo);
            
            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            
        } finally {
            DB.closeConnection();           
        }
        
        return false;            
    }
    
    public Vehicle(HashMap<String,Object> params) {
        this.params = params;
    }
    
    public boolean create() {
        try {
            Connection conn = DB.createConnection();

            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO Vehicle(" + String.join(", ", params.keySet()) + ") " + 
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            int i = 0;
            
            for (String k : params.keySet()) {
                i++;
                
                switch (params.get(k).getClass().getSimpleName()) {
                    case "String" -> st.setString(i, params.get(k).toString());
                        
                    case "Integer" -> st.setInt(i, Integer.parseInt(params.get(k).toString()));
                        
                    case "Double" -> st.setDouble(i, Double.parseDouble(params.get(k).toString()));
                }
            }                 

            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            
        } finally {
            DB.closeConnection();           
        }
        
        return false;
    }
}
