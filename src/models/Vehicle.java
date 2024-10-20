
package models;
import dbpkg.DB;
import java.sql.*;
import java.util.HashMap;

public class Vehicle {
    
    private HashMap<String,Object> params;
    private static String errorMessage;
    
    public Vehicle(HashMap<String,Object> params) {
        this.params = params;
    }
    
    public static String getErrorMessage() {
       return errorMessage;
    }
    
    public static ResultSet find(String vehicleNo) {        
        try {
            Connection conn = DB.createConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM Vehicle WHERE vehicle_no = ?");
            st.setString(1, vehicleNo);
            
            ResultSet rs = st.executeQuery();
            return rs;
            
        } catch (SQLException e) {
            errorMessage = e.getMessage();         
        }
        
        return null;        
    }
    
    public static ResultSet all(String status) {
        String sql;
        
        if (status.equals("All")) {
            sql = "SELECT * FROM Vehicle ORDER BY id DESC";
            
        } else {
            sql = "SELECT * FROM Vehicle WHERE status = '" + status + "' ORDER BY id DESC";
        }
        
        try {
            Statement st = DB.createConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
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
    
    public static boolean update(HashMap<String,Object> params) {        
        String vehicleNo = params.get("vehicle_no").toString();
        params.remove("vehicle_no");
        
        try {
            Connection conn = DB.createConnection();
            PreparedStatement st = conn.prepareStatement("UPDATE Vehicle SET " + String.join("=?, ", params.keySet()) + "=? WHERE vehicle_no = ?");

            int i = 0;
            
            for (String k : params.keySet()) {
                i++;
                
                switch (params.get(k).getClass().getSimpleName()) {
                    case "String" -> st.setString(i, params.get(k).toString());
                        
                    case "Integer" -> st.setInt(i, Integer.parseInt(params.get(k).toString()));
                        
                    case "Double" -> st.setDouble(i, Double.parseDouble(params.get(k).toString()));
                }
            }
            
            st.setString(i+1, vehicleNo);
            return st.executeUpdate() > 0;
            
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            
        } finally {
            DB.closeConnection();           
        }
        
        return false;         
    }
}
